package com.ecommercesystemtemplate.cart.service;

import com.ecommercesystemtemplate.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

public interface CartService {


    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;
}
