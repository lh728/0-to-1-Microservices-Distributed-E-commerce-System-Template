package com.ecommercesystemtemplate.authserver.controller;

import com.ecommercesystemtemplate.common.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
public class OAuth2Controller {

    @GetMapping("/oauth2/weibo/success")
    public String weibo(@RequestParam("code") String code) throws Exception {
        // 1. get accessToken
        HashMap<String, String> map = new HashMap<>();
        map.put("client_id","your_id");
        map.put("client_secret","your_secret");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://thellumall.com/oauth2/weibo/success");
        map.put("code",code);
        HttpResponse post = HttpUtils.doPost("api.weibo.com",
                "/oauth2/access_token", "POST", null, null, map);



        // 2. if success, jump to front page


        return "redirect:http://thellumall.com";
    }


}
