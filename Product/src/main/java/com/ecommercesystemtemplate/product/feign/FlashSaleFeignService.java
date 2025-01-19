package com.ecommercesystemtemplate.product.feign;

import com.ecommercesystemtemplate.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient("flashSale")
public interface FlashSaleFeignService {

    @GetMapping("/sku/flashSale/{skuId}")
    R getFlashSaleSkuInfo(@PathVariable("skuId") Long skuId);
}
