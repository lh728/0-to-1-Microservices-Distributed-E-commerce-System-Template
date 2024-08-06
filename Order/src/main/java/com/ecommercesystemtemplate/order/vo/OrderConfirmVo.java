package com.ecommercesystemtemplate.order.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
     * Prevent resubmit token
     */
    @Getter
    @Setter
    private String orderToken;

    /**
     * stock info
     */
    @Getter
    @Setter
    private Map<Long,Boolean> stocks;

    private Integer count;
    private BigDecimal total;
    private BigDecimal payablePrice;


    public Integer getCount() {
        Integer count = 0;
        if (orderItemVoList != null) {
            for (OrderItemVo orderItemVo : orderItemVoList) {
                count += orderItemVo.getCount();
            }
        }
        return count;
    }


    public BigDecimal getTotal() {
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
        return getTotal();
    }
}
