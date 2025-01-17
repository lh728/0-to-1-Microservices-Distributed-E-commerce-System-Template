package com.ecommercesystemtemplate.flashsale.service;

import com.ecommercesystemtemplate.flashsale.to.FlashSaleSkuRedisTo;

import java.util.List;

public interface FlashSaleService {
    void uploadFlashSaleSkuLatest30Days();

    List<FlashSaleSkuRedisTo> getCurrentFlashSaleSkus();
}
