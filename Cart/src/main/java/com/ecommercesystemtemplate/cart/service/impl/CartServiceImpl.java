package com.ecommercesystemtemplate.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ecommercesystemtemplate.cart.feign.ProductFeignService;
import com.ecommercesystemtemplate.cart.interceptor.CartInterceptor;
import com.ecommercesystemtemplate.cart.service.CartService;
import com.ecommercesystemtemplate.cart.vo.CartItem;
import com.ecommercesystemtemplate.cart.vo.SkuInfoVo;
import com.ecommercesystemtemplate.cart.vo.UserInfoTo;
import com.ecommercesystemtemplate.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@Slf4j
public class CartServiceImpl implements CartService{

    final StringRedisTemplate stringRedisTemplate;
    final ProductFeignService productFeignService;
    final ThreadPoolExecutor threadPoolExecutor;

    private final String CART_PREFIX = "thellumall:cart:";

    public CartServiceImpl(StringRedisTemplate stringRedisTemplate, ProductFeignService productFeignService, ThreadPoolExecutor threadPoolExecutor) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.productFeignService = productFeignService;
        this.threadPoolExecutor = threadPoolExecutor;
    }


    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {

        BoundHashOperations cartOps = getCartOps();
        CartItem cartItem = new CartItem();
        CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
            // 1. remote query curr product info
            R info = productFeignService.info(skuId);
            SkuInfoVo skuInfo = info.getData("skuInfo", new TypeReference<SkuInfoVo>() {
            });
            // 2. add product to cart
            cartItem.setCheck(true);
            cartItem.setCount(num);
            cartItem.setImage(skuInfo.getSkuDefaultImg());
            cartItem.setTitle(skuInfo.getSkuTitle());
            cartItem.setSkuId(skuId);
            cartItem.setPrice(skuInfo.getPrice());
        }, threadPoolExecutor);

        // 3. remote query sku attr
        CompletableFuture<Void> getSkuSaleAttrValuesTask = CompletableFuture.runAsync(() -> {
            List<String> skuSaleAttrValues = productFeignService.getSkuSaleAttrValues(skuId);
            cartItem.setSkuAttr(skuSaleAttrValues);
        }, threadPoolExecutor);

        CompletableFuture.allOf(getSkuInfoTask, getSkuSaleAttrValuesTask).get();

        cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
        return cartItem;
    }

    /**
     * get cart that we need to operate
     * @return
     */
    private BoundHashOperations getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey = "";
        if (userInfoTo.getUserId() != null){
            // 1. has logged in
           cartKey = CART_PREFIX + userInfoTo.getUserId();
        } else {
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }
        BoundHashOperations<String, Object, Object> hashOperations = stringRedisTemplate.boundHashOps(cartKey);
        return hashOperations;
    }
}



