package com.ecommercesystemtemplate.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.to.mq.OrderTo;
import com.ecommercesystemtemplate.common.to.mq.StockLockedTo;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.warehouse.entity.WareSkuEntity;
import com.ecommercesystemtemplate.warehouse.vo.SkuHasStockVo;
import com.ecommercesystemtemplate.warehouse.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * Commodity stocks
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkusHaveStock(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVo vo);

    void unlockStock(StockLockedTo to);

    void unlockStock(OrderTo orderTo);
}

