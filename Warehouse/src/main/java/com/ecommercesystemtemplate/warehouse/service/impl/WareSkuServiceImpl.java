package com.ecommercesystemtemplate.warehouse.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.exception.NoStockException;
import com.ecommercesystemtemplate.common.to.mq.StockDetailTo;
import com.ecommercesystemtemplate.common.to.mq.StockLockedTo;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.warehouse.dao.WareSkuDao;
import com.ecommercesystemtemplate.warehouse.entity.WareOrderTaskDetailEntity;
import com.ecommercesystemtemplate.warehouse.entity.WareOrderTaskEntity;
import com.ecommercesystemtemplate.warehouse.entity.WareSkuEntity;
import com.ecommercesystemtemplate.warehouse.feign.OrderFeignService;
import com.ecommercesystemtemplate.warehouse.feign.ProductFeignService;
import com.ecommercesystemtemplate.warehouse.service.WareOrderTaskDetailService;
import com.ecommercesystemtemplate.warehouse.service.WareOrderTaskService;
import com.ecommercesystemtemplate.warehouse.service.WareSkuService;
import com.ecommercesystemtemplate.warehouse.vo.OrderItemVo;
import com.ecommercesystemtemplate.warehouse.vo.OrderVo;
import com.ecommercesystemtemplate.warehouse.vo.SkuHasStockVo;
import com.ecommercesystemtemplate.warehouse.vo.WareSkuLockVo;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service("wareSkuService")
@AllArgsConstructor
@RabbitListener(queues = "stock.release.stock.queue")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    final WareSkuDao wareSkuDao;
    final ProductFeignService productFeignService;
    final RabbitTemplate rabbitTemplate;
    final WareOrderTaskDetailService wareOrderTaskDetailService;
    final WareOrderTaskService wareOrderTaskService;
    final OrderFeignService orderFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)) {
            wrapper.eq("sku_id", skuId);
        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            wrapper.eq("ware_id", wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params), wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        List<WareSkuEntity> entities =
                wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (entities.isEmpty()) {
            WareSkuEntity entity = new WareSkuEntity();
            entity.setSkuId(skuId);
            entity.setWareId(wareId);
            entity.setStock(skuNum);
            entity.setStockLocked(0);
            // remote call to get sku info
            try {
                R info = productFeignService.info(skuId);
                if (info.getCode() == 0) {
                    Map<String, Object> data = (Map<String, Object>) info.get("skuInfo");
                    entity.setSkuName((String) data.get("skuName"));
                }
            } catch (Exception e) {
                log.error("Remote call to get sku info failed");
            }
            wareSkuDao.insert(entity);
        } else {
            wareSkuDao.addStock(skuId, wareId, skuNum);
        }
    }

    @Override
    public List<SkuHasStockVo> getSkusHaveStock(List<Long> skuIds) {

        List<SkuHasStockVo> list = skuIds.stream().map(skuId -> {
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            Long count = baseMapper.getSkuHasStock(skuId);
            skuHasStockVo.setSkuId(skuId);
            skuHasStockVo.setHasStock(count != null && count > 0);
            return skuHasStockVo;
        }).toList();
        return list;
    }

    /**
     * lock stock for order
     *
     * @param vo
     * @return if lock
     */
    @Override
    @Transactional
    public Boolean orderLockStock(WareSkuLockVo vo) {
        // 0. create order task detail
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(vo.getOrderSn());
        wareOrderTaskService.save(wareOrderTaskEntity);

        // 1. according to address, get warehouse nearest to lock stock
        List<OrderItemVo> orderItemVoList = vo.getOrderItemVoList();
        List<SkuWareHasStock> lockStockResultVos = orderItemVoList.stream().map(item -> {
            SkuWareHasStock skuWareHasStock = new SkuWareHasStock();
            skuWareHasStock.setSkuId(item.getSkuId());
            skuWareHasStock.setNum(item.getCount());
            // Check where this product is in stock
            List<Long> wareIds = wareSkuDao.listWareIdHasSku(item.getSkuId());
            skuWareHasStock.setWareIds(wareIds);
            return skuWareHasStock;
        }).toList();

        Boolean allLock = true;
        // 2. lock stock
        for (SkuWareHasStock skuWareHasStock : lockStockResultVos) {
            Boolean skuStocked = false;
            Long skuId = skuWareHasStock.getSkuId();
            List<Long> wareIds = skuWareHasStock.getWareIds();
            if (wareIds == null || wareIds.isEmpty()) {
                throw new NoStockException(skuId);
            }
            for (Long wareId : wareIds) {
                // 1 successes, 0 fail
                Long count = wareSkuDao.lockStock(skuId, wareId, skuWareHasStock.getNum());
                if (count == 1) {
                    skuStocked = true;
                    // tell MQ lock success
                    WareOrderTaskDetailEntity entity = new WareOrderTaskDetailEntity(null, skuId, "", skuWareHasStock.getNum(), wareOrderTaskEntity.getId(), wareId, 1);;
                    wareOrderTaskDetailService.save(entity);
                    StockLockedTo stockLockedTo = new StockLockedTo();
                    stockLockedTo.setId(wareOrderTaskEntity.getId());
                    // used to rollback data
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtils.copyProperties(entity,stockDetailTo);
                    stockLockedTo.setDetail(stockDetailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange", "stock.locked", stockLockedTo);
                    break;
                } else{
                    // current warehouse has no stock. try another

                }
            }
            if (!skuStocked) {
                // current product has no stock for all warehouses
                throw new NoStockException(skuId);
            }
        }
        return true;
    }

    /**
     * 1. stock auto unlock
     *      order success, stock locked success; if transaction rollback, unlock stock
     * 2. order fail
     *      stock lock fail(for example, one of warehouses has no stock)
     *
     * @param to
     * @param message
     */
    @RabbitHandler
    public void handleStockUnlocked(StockLockedTo to, Message message, Channel channel) throws IOException {
        StockDetailTo detail = to.getDetail();
        Long detailId = detail.getId();
        // 1. query database about this order locked detail
        WareOrderTaskDetailEntity byId = wareOrderTaskDetailService.getById(detailId);
        if (byId != null) {
            Long id = to.getId();
            WareOrderTaskEntity task = wareOrderTaskService.getById(id);
            String orderSn = task.getOrderSn();
            R r = orderFeignService.getOrderStatus(orderSn);
            if (r.getCode() == 0) {
                OrderVo data = r.getData(new TypeReference<OrderVo>() {
                });
                if (data == null || data.getStatus() == 4) {
                    // 1.1 if database has this detail, locked success
                    // 1.1.1 if database has not this order, that means must unlock
                    unlockStock(detail);
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    // 1.1.2 if database has this order, that means do not need unlock, order status change(cancel - unlock, not cancel - still locked)
                }
            }else{
                channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            }


        } else{
            // 1.2 if database has not this detail, that means locked fail, stock rollback, do not need unlock
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }


    }

    private void unlockStock(StockDetailTo vo) {
        wareSkuDao.unlockStock(vo.getSkuId(), vo.getWareId(), vo.getSkuNum(), vo.getId());
    }



    @Data
    class SkuWareHasStock{

        private Long skuId;
        private Integer num;
        private List<Long> wareIds;
    }

}
