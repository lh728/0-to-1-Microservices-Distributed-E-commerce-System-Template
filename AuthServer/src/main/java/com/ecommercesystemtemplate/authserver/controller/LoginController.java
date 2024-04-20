package com.ecommercesystemtemplate.authserver.controller;

import com.alibaba.fastjson.TypeReference;
import com.ecommercesystemtemplate.authserver.feign.MemberFeignService;
import com.ecommercesystemtemplate.authserver.feign.ThirdPartyFeignService;
import com.ecommercesystemtemplate.authserver.vo.UserRegistVo;
import com.ecommercesystemtemplate.common.constant.AuthServerConstant;
import com.ecommercesystemtemplate.common.exception.BizCodeEnume;
import com.ecommercesystemtemplate.common.utils.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    final ThirdPartyFeignService thirdPartyFeignService;
    final StringRedisTemplate stringRedisTemplate;
    final MemberFeignService memberFeignService;

    public LoginController(ThirdPartyFeignService thirdPartyFeignService, StringRedisTemplate stringRedisTemplate, MemberFeignService memberFeignService) {
        this.thirdPartyFeignService = thirdPartyFeignService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.memberFeignService = memberFeignService;
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
        String code = UUID.randomUUID().toString().substring(0, 5);
        String substring = code + "_" + System.currentTimeMillis();
        System.out.println("send code: " + code);
        stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX +phone,substring,10, TimeUnit.MINUTES);

        thirdPartyFeignService.sendCode(phone,code);
        return R.ok();

    }

    /**
     * register successfully then redirect to login page
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid UserRegistVo vo, BindingResult result, RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            Map<String,String> errors = result.getFieldErrors().stream().collect(
                    Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
            );
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.thellumall.com/register.html";
        }
        // 1. check verification code
        String code = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (StringUtils.isNotEmpty(code)) {
            if (code.split("_")[0].equals(vo.getCode())) {
                // delete verification code after verification
                stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                // register, use feign to call third party service
                R r = memberFeignService.register(vo);
                if (r.getCode() == 0) {

                    return "redirect:http://auth.thellumall.com/login.html";
                } else {
                    Map<String,String> errors = new HashMap<>();
                    errors.put("msg",r.getData("msg",new TypeReference<String>(){}));
                    redirectAttributes.addFlashAttribute("errors", errors);
                    return "redirect:http://auth.thellumall.com/register.html";
                }
            } else {
                Map<String,String> errors = new HashMap<>();
                errors.put("code","Verification code has expired or is incorrect, please try again" );
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.thellumall.com/register.html";
            }
        } else {
            // check failed redirect to register page
            Map<String,String> errors = new HashMap<>();
            errors.put("code","Verification code has expired or is incorrect, please try again" );
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.thellumall.com/register.html";
        }


    }

}
