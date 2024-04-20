package com.ecommercesystemtemplate.authserver.feign;

import com.ecommercesystemtemplate.authserver.vo.UserRegistVo;
import com.ecommercesystemtemplate.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("member")
public interface MemberFeignService {

    @PostMapping("/member/member/register")
    R register(@RequestBody UserRegistVo member);
}
