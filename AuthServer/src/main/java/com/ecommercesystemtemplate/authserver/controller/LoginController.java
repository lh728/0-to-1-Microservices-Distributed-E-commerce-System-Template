package com.ecommercesystemtemplate.authserver.controller;

import com.ecommercesystemtemplate.authserver.feign.ThirdPartyFeignService;
import com.ecommercesystemtemplate.common.utils.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
public class LoginController {

    final ThirdPartyFeignService thirdPartyFeignService;

    public LoginController(ThirdPartyFeignService thirdPartyFeignService) {
        this.thirdPartyFeignService = thirdPartyFeignService;
    }

    @GetMapping("/sms/sendCode")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone){

        String code = UUID.randomUUID().toString().substring(0, 5);

        thirdPartyFeignService.sendCode(phone,code);

        return R.ok();

    }

}
