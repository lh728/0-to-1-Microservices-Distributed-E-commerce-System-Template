package com.ecommercesystemtemplate.cart.controller;

import com.ecommercesystemtemplate.cart.interceptor.CartInterceptor;
import com.ecommercesystemtemplate.cart.service.CartService;
import com.ecommercesystemtemplate.cart.vo.CartItem;
import com.ecommercesystemtemplate.cart.vo.UserInfoTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ExecutionException;

@Controller
public class CartController {

    final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart.html")
    public String cartListPage(){
        // get userinfo fast
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        System.out.println(userInfoTo);

        return "cartList";
    }

    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num, Model model) throws ExecutionException, InterruptedException {
        CartItem cartItem = cartService.addToCart(skuId,num);
        model.addAttribute("item",cartItem);
        return "success";
    }

}
