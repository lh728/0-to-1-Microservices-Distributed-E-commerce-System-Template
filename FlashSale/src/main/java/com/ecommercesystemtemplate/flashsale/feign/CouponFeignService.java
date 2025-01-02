package com.ecommercesystemtemplate.flashsale.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("coupon")
public interface CouponFeignService {
}
