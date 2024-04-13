package com.ecommercesystemtemplate.product.web;

import com.ecommercesystemtemplate.product.service.SkuInfoService;
import com.ecommercesystemtemplate.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {
    final
    SkuInfoService skuInfoService;

    public ItemController(SkuInfoService skuInfoService) {
        this.skuInfoService = skuInfoService;
    }

    /**
     * Get curr item page
     * @param skuId
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId) {

        SkuItemVo skuItemVo = skuInfoService.item(skuId);

        return "item";
    }


}
