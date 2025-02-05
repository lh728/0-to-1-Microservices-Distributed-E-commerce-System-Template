package com.ecommercesystemtemplate.flashsale.to;

import com.ecommercesystemtemplate.flashsale.vo.SkuInfoVo;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FlashSaleSkuRedisTo {
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
    private Integer seckillLimit;
    /**
     * flash sort
     */
    private Integer seckillSort;
    /**
     * start time
     */
    private Long startTime;
    /**
     * end time
     */
    private Long endTime;
    /**
     * randomCode
     */
    private String randomCode;
    /**
     * sku detail
     */
    private SkuInfoVo skuInfoVo;

}
