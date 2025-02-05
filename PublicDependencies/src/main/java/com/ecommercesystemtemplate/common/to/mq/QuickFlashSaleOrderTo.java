package com.ecommercesystemtemplate.common.to.mq;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuickFlashSaleOrderTo {

    private String OrderSn;
    private Long promotionSessionId;
    private Long skuId;
    private Integer num;
    private BigDecimal seckillPrice;
    private Long memberId;

}
