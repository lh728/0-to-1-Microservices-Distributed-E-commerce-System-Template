package com.ecommercesystemtemplate.flashsale.feign;

import com.ecommercesystemtemplate.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lhjls
 */
@FeignClient("coupon")
public interface CouponFeignService {

    @GetMapping("/coupon/seckillsession/getLatest3DaySession")
    R getLatest3DaySession();
}
