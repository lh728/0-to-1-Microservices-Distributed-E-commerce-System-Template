package com.ecommercesystemtemplate.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.exception.NoStockException;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.warehouse.dao.WareSkuDao;
import com.ecommercesystemtemplate.warehouse.entity.WareSkuEntity;
import com.ecommercesystemtemplate.warehouse.feign.ProductFeignService;
import com.ecommercesystemtemplate.warehouse.service.WareSkuService;
import com.ecommercesystemtemplate.warehouse.vo.OrderItemVo;
import com.ecommercesystemtemplate.warehouse.vo.SkuHasStockVo;
import com.ecommercesystemtemplate.warehouse.vo.WareSkuLockVo;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    final
    WareSkuDao wareSkuDao;
    final ProductFeignService productFeignService;

    public WareSkuServiceImpl(WareSkuDao wareSkuDao, ProductFeignService productFeignService) {
        this.wareSkuDao = wareSkuDao;
        this.productFeignService = productFeignService;
    }

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
    @Transactional(rollbackFor = NoStockException.class)
    public Boolean orderLockStock(WareSkuLockVo vo) {
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
                if (count == 0) {
                    // current warehouse has no stock. try another
                } else{
                    skuStocked = true;
                    break;
                }
            }
            if (!skuStocked) {
                // current product has no stock for all warehouses
                throw new NoStockException(skuId);
            }
        }
        return true;
    }
    @Data
    class SkuWareHasStock{

        private Long skuId;
        private Integer num;
        private List<Long> wareIds;
    }

}
