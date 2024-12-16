package com.ecommercesystemtemplate.member.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/memberOrder.html")
    public String memberOrderPage(){

        return "orderList";
    }
}
