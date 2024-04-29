package com.ecommercesystemtemplate.cart.vo;


import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

public class Cart {

    @Getter
    private List<CartItem> items;

    private Integer countNum;

    private Integer countType;

    private BigDecimal totalAmount;

    @Getter
    private BigDecimal reduce = new BigDecimal("0.00");

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Integer getCountNum() {
        int count = 0;
        if (items != null && !items.isEmpty()){
            for (CartItem item : items) {
                count += item.getCount();
            }
        }
        return count;
    }

    public Integer getCountType() {
        int count = 0;
        if (items != null && !items.isEmpty()){
            for (CartItem item : items) {
                count += 1;
            }
        }
        return count;
    }


    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0.00");
        // 1. calculate total amount
        if (items != null && !items.isEmpty()){
            for (CartItem item : items) {
                BigDecimal totalPrice = item.getTotalPrice();
                amount = amount.add(totalPrice);
            }
        }
        // 2. calculate reduce
        BigDecimal subtract = amount.subtract(getReduce());

        return subtract;
    }


    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}
