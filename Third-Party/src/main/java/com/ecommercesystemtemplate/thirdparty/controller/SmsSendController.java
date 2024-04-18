package com.ecommercesystemtemplate.thirdparty.controller;

import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.thirdparty.component.SmsComponent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsSendController {

    final SmsComponent smsComponent;

    public SmsSendController(SmsComponent smsComponent) {
        this.smsComponent = smsComponent;
    }


    /**
     * provide to other service to use
     * @param phone
     * @param code
     * @return
     */
    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phone,@RequestParam("code") String code){
        smsComponent.sendSms(phone,code);
        return R.ok();
    }
}
