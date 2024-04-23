package com.ecommercesystemtemplate.authserver.feign;

import com.ecommercesystemtemplate.authserver.vo.SocialUser;
import com.ecommercesystemtemplate.authserver.vo.UserLoginVo;
import com.ecommercesystemtemplate.authserver.vo.UserRegisterVo;
import com.ecommercesystemtemplate.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("member")
public interface MemberFeignService {

    @PostMapping("/member/member/register")
    R register(@RequestBody UserRegisterVo member);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo );

    @PostMapping("/member/member/oauth2/login")
    R oauthLogin(@RequestBody SocialUser vo) throws  Exception;


}
