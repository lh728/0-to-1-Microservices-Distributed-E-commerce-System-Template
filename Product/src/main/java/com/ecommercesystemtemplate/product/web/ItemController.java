package com.ecommercesystemtemplate.product.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {

    /**
     * Get curr item page
     * @param skuId
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(Long skuId) {
        return "item";
    }


}
