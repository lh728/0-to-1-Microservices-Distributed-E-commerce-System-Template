package com.ecommercesystemtemplate.cart.service.impl;

import com.ecommercesystemtemplate.cart.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartServiceImpl implements CartService{

    final StringRedisTemplate stringRedisTemplate;

    public CartServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


}



