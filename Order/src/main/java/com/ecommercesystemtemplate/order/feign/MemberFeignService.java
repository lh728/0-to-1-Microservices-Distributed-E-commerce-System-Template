package com.ecommercesystemtemplate.order.feign;

import com.ecommercesystemtemplate.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("member")
public interface MemberFeignService {

    @GetMapping("/member/memberreceiveaddress/{id}/address")
    List<MemberAddressVo> getAddress(@PathVariable("id") Long id );

}
