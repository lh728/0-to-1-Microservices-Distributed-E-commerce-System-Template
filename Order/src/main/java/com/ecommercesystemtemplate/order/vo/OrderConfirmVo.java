package com.ecommercesystemtemplate.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderConfirmVo {

    /**
     * delivery address list
     */
    private List<MemberAddressVo> address;

    /**
     * order items
     */
    private List<OrderItemVo> orderItemVoList;

    /**
     * invoice info
     */
//    private InvoiceVo invoiceVo;

    /**
     * member points info
     */
    private Integer points;

    /**
     * total amount
     */
    private BigDecimal totalAmount;

    /**
     * Price payable
     */
    private BigDecimal payablePrice;

}
