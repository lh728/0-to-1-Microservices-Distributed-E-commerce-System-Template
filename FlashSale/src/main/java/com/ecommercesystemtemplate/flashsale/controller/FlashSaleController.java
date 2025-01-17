package com.ecommercesystemtemplate.flashsale.controller;

import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.flashsale.service.FlashSaleService;
import com.ecommercesystemtemplate.flashsale.to.FlashSaleSkuRedisTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FlashSaleController {

    final
    FlashSaleService flashSaleService;

    public FlashSaleController(FlashSaleService flashSaleService) {
        this.flashSaleService = flashSaleService;
    }

    /**
     * return current flash sale skus
     * @return
     */
    @GetMapping("/getCurrentFlashSaleSkus")
    public R getCurrentFlashSaleSkus() {
        List<FlashSaleSkuRedisTo> data = flashSaleService.getCurrentFlashSaleSkus();

        return R.ok().setData(data);
    }
}
