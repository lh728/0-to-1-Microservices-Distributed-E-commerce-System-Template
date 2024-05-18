package com.ecommercesystemtemplate.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ecommercesystemtemplate.cart.feign.ProductFeignService;
import com.ecommercesystemtemplate.cart.interceptor.CartInterceptor;
import com.ecommercesystemtemplate.cart.service.CartService;
import com.ecommercesystemtemplate.cart.vo.Cart;
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

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        // check if the product is already in the cart
        String cartItemJson = (String) cartOps.get(skuId.toString());

        if (cartItemJson != null){
            // 1. product already in the cart, increase the quantity
            CartItem cartItem = JSON.parseObject(cartItemJson, CartItem.class);
            cartItem.setCount(cartItem.getCount() + num);
            cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
            return cartItem;
        } else{
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

    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String s = (String) cartOps.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(s, CartItem.class);
        return cartItem;
    }

    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        Cart cart = new Cart();
        if (userInfoTo.getUserId() != null){
            // 1. has logged in
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            BoundHashOperations<String, Object, Object> hashOperations = stringRedisTemplate.boundHashOps(cartKey);
            // 2. if temporary user has cart
            String tempCartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> tempCartItems = getCartItems(tempCartKey);
            if (tempCartItems != null && !tempCartItems.isEmpty()){
                for (CartItem tempCartItem : tempCartItems) {
                    addToCart(tempCartItem.getSkuId(), tempCartItem.getCount());
                }
//                // 3. merge temporary cart to logged in cart
//                tempCartItems.forEach((item) -> {
//                    hashOperations.put(item.getSkuId().toString(), JSON.toJSONString(item));
//                });
                // 4. clear temporary cart
                clearCart(tempCartKey);
            } else {
                List<CartItem> cartItems = getCartItems(cartKey);
                cart.setItems(cartItems);
            }

        } else {
            String cartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);
        }
        return cart;
    }

    /**
     * get cart that we need to operate
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
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

    private List<CartItem> getCartItems(String cartKey) {
        BoundHashOperations<String, Object, Object> hashOperations = stringRedisTemplate.boundHashOps(cartKey);
        List<Object> values = hashOperations.values();
        if (values != null && !values.isEmpty()) {
            List<CartItem> collect = values.stream().map((obj) -> {
                CartItem cartItem = JSON.parseObject((String) obj, CartItem.class);
                return cartItem;
            }).toList();
            return collect;
        }
        return null;
    }

    @Override
    public void clearCart(String cartKey) {
        stringRedisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer check) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(check == 1);
        String s = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(), s);
    }

    @Override
    public void changeItemCount(Long skuId, Integer num) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(),JSON.toJSONString(cartItem));
    }

    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }
}



