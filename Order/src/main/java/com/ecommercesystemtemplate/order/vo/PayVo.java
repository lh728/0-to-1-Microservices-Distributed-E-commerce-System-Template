package com.ecommercesystemtemplate.order.vo;

import lombok.Data;

@Data
public class PayVo {
    private String out_trade_no; // Merchant Order Number Required
    private String subject; // Order name Required
    private String total_amount;  // Payment Amount Required
    private String body; // Product Description Optional
}
