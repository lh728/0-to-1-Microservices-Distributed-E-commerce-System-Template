package com.ecommercesystemtemplate.cart.service;

import com.ecommercesystemtemplate.cart.vo.Cart;
import com.ecommercesystemtemplate.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

public interface CartService {


    /**
     * add product to cart
     * @param skuId
     * @param num
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * get some product details from cart
     * @param skuId
     * @return
     */
    CartItem getCartItem(Long skuId);

    /**
     * get the whole cart
     * @return
     */
    Cart getCart() throws ExecutionException, InterruptedException;

    /**
     * clear cart
     * @param cartKey
     */
    void clearCart(String cartKey);

    /**
     * select checked box
     * @param skuId
     * @param check
     */
    void checkItem(Long skuId, Integer check);
}
