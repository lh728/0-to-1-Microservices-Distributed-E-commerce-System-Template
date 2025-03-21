package com.ecommercesystemtemplate.flashsale.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FlashSaleSkuVo {
    private Long id;
    /**
     * promotion_id
     */
    private Long promotionId;
    /**
     * promotion_session_id
     */
    private Long promotionSessionId;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * flash price
     */
    private BigDecimal seckillPrice;
    /**
     * flash count
     */
    private BigDecimal seckillCount;
    /**
     * flash limit
     */
    private BigDecimal seckillLimit;
    /**
     * flash sort
     */
    private Integer seckillSort;

}
