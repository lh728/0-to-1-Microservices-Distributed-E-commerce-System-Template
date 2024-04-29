package com.ecommercesystemtemplate.cart.controller;

import com.ecommercesystemtemplate.cart.interceptor.CartInterceptor;
import com.ecommercesystemtemplate.cart.vo.UserInfoTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {

    @GetMapping("/cart.html")
    public String cartListPage(){
        // get userinfo fast
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();


        return "cartList";
    }

}
