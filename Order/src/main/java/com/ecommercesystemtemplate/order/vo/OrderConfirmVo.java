package com.ecommercesystemtemplate.order.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

public class OrderConfirmVo {

    /**
     * delivery address list
     */
    @Getter
    @Setter
    private List<MemberAddressVo> address;

    /**
     * order items
     */
    @Getter
    @Setter
    private List<OrderItemVo> orderItemVoList;

    /**
     * invoice info
     */

//    private InvoiceVo invoiceVo;

    /**
     * member points info
     */
    @Getter
    @Setter
    private Integer points;

    /**
     * orderToken
     * Prevent repetition
     */
    @Getter
    @Setter
    private String orderToken;


    public BigDecimal getTotalAmount() {
        BigDecimal totalAmount = new BigDecimal("0");
        if (orderItemVoList != null) {
            for (OrderItemVo orderItemVo : orderItemVoList) {
                BigDecimal multiply = orderItemVo.getPrice().multiply(new BigDecimal(orderItemVo.getCount().toString()));
                totalAmount = totalAmount.add(multiply);
            }
        }
        return totalAmount;
    }

    public BigDecimal getPayablePrice() {
        return getTotalAmount();
    }
}
