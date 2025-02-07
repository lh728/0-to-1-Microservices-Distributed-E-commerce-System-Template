package com.ecommercesystemtemplate.flashsale.controller;

import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.flashsale.service.FlashSaleService;
import com.ecommercesystemtemplate.flashsale.to.FlashSaleSkuRedisTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
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
    @ResponseBody
    @GetMapping("/getCurrentFlashSaleSkus")
    public R getCurrentFlashSaleSkus() {
        List<FlashSaleSkuRedisTo> data = flashSaleService.getCurrentFlashSaleSkus();

        return R.ok().setData(data);
    }

    @GetMapping("/sku/flashSale/{skuId}")
    @ResponseBody
    public R getFlashSaleSkuInfo(@PathVariable("skuId") Long skuId) {
        FlashSaleSkuRedisTo data = flashSaleService.getFlashSaleSkuInfo(skuId);
        return R.ok().setData(data);
    }

    @GetMapping("/flashSale")
    public String flashSale(@RequestParam("flashSaleId") String flashSaleId,
                            @RequestParam("key") String key,
                            @RequestParam("num") Integer num,
                            Model model) {
        String orderSn = flashSaleService.flashSale(flashSaleId, key, num);
        model.addAttribute("orderSn",orderSn);
        return "success";
    }


}
