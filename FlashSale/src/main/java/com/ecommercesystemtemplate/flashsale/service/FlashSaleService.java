package com.ecommercesystemtemplate.flashsale.service;

import com.ecommercesystemtemplate.flashsale.to.FlashSaleSkuRedisTo;

import java.util.List;

public interface FlashSaleService {
    void uploadFlashSaleSkuLatest30Days();

    List<FlashSaleSkuRedisTo> getCurrentFlashSaleSkus();

    FlashSaleSkuRedisTo getFlashSaleSkuInfo(Long skuId);

    String flashSale(String flashSaleId, String key, Integer num);
}
