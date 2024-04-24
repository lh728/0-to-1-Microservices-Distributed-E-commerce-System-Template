package com.ecommercesystemtemplate.authserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ecommercesystemtemplate.authserver.feign.MemberFeignService;
import com.ecommercesystemtemplate.authserver.vo.MemberResponseVo;
import com.ecommercesystemtemplate.authserver.vo.SocialUser;
import com.ecommercesystemtemplate.common.utils.HttpUtils;
import com.ecommercesystemtemplate.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@Slf4j
public class OAuth2Controller {

    final MemberFeignService memberFeignService;

    public OAuth2Controller(MemberFeignService memberFeignService) {
        this.memberFeignService = memberFeignService;
    }

    @GetMapping("/oauth2/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session, HttpServletResponse response) throws Exception {
        // 1. get accessToken
        HashMap<String, String> map = new HashMap<>();
        map.put("client_id","your_id");
        map.put("client_secret","your_secret");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://thellumall.com/oauth2/weibo/success");
        map.put("code",code);
        HttpResponse post = HttpUtils.doPost("api.weibo.com",
                "/oauth2/access_token", "POST", null, null, map);
        if(post.getStatusLine().getStatusCode() == 200){
            String s = EntityUtils.toString(post.getEntity());
            SocialUser socialUser = JSON.parseObject(s, SocialUser.class);
            // 1.1 if user first in, then register automatically
            R r = memberFeignService.oauthLogin(socialUser);
            if (r.getCode() == 0){
                MemberResponseVo data = r.getData("data", new TypeReference<MemberResponseVo>() {
                });
                // 1.2 first use session, save cookie
                session.setAttribute("loginUser", data);
                response.addCookie(MemberFeignService.setCookie("user-key", data.getId().toString()));

                // 2. if success, jump to front page
                return "redirect:http://thellumall.com";
            }else {
                return "redirect:http://auth.thellumall.com/login.html";
            }
        } else {
            return "redirect:http://auth.thellumall.com/login.html";
        }
    }


}
