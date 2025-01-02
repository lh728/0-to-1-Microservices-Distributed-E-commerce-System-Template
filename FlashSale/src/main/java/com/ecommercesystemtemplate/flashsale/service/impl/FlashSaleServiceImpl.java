package com.ecommercesystemtemplate.flashsale.service.impl;

import com.ecommercesystemtemplate.flashsale.service.FlashSaleService;
import org.springframework.stereotype.Service;

@Service
public class FlashSaleServiceImpl implements FlashSaleService {


    @Override
    public void uploadFlashSaleSkuLatest30Days() {
        // scan all active flash sale (past 3 days)

    }
}
