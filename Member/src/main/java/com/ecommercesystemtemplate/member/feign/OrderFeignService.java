package com.ecommercesystemtemplate.member.feign;

import com.ecommercesystemtemplate.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
@FeignClient("order")
public interface OrderFeignService {

    @GetMapping("/order/order/listWithItems")
    R listWithItems(@RequestBody Map<String, Object> params);

}



