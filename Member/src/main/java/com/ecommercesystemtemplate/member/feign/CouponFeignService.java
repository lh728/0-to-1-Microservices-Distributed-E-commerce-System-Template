package com.ecommercesystemtemplate.member.feign;

import com.ecommercesystemtemplate.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("coupon")
@Service
public interface CouponFeignService {

    @RequestMapping("coupon/coupon/member/list")
    R memberCoupons();
}
