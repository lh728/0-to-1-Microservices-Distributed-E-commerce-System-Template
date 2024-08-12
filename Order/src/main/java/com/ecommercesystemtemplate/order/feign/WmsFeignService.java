package com.ecommercesystemtemplate.order.feign;

import com.ecommercesystemtemplate.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("warehouse")
public interface WmsFeignService {

    @PostMapping("/warehouse/waresku/hasStock")
    R getSkusHaveStock(@RequestBody List<Long> skuIds);

    @GetMapping("/warehouse/wareinfo/getFreight")
    R getFreight(@RequestParam("addrId") Long addrId);
}
