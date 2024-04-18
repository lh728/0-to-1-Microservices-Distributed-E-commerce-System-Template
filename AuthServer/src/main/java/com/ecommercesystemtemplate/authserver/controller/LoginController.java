package com.ecommercesystemtemplate.authserver.controller;

import com.ecommercesystemtemplate.authserver.feign.ThirdPartyFeignService;
import com.ecommercesystemtemplate.common.constant.AuthServerConstant;
import com.ecommercesystemtemplate.common.exception.BizCodeEnume;
import com.ecommercesystemtemplate.common.utils.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController {

    final ThirdPartyFeignService thirdPartyFeignService;
    final StringRedisTemplate stringRedisTemplate;

    public LoginController(ThirdPartyFeignService thirdPartyFeignService, StringRedisTemplate stringRedisTemplate) {
        this.thirdPartyFeignService = thirdPartyFeignService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @GetMapping("/sms/sendCode")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone){
        // TODO 1. Interface anti-brushing verification


        // 2. avoid same phone send code again within 60s
        String redisCode = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (StringUtils.isNotEmpty(redisCode)){
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - l < 60000){
                // reject sending verification code within 60s
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(), BizCodeEnume.SMS_CODE_EXCEPTION.getMessage());
            }
        }
        // 3. ckeck code( redis )
        String code = UUID.randomUUID().toString().substring(0, 5) + "_" + System.currentTimeMillis();
        stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX +phone,code,10, TimeUnit.MINUTES);

        thirdPartyFeignService.sendCode(phone,code);
        return R.ok();

    }

}
