package com.ecommercesystemtemplate.cart.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuInfoVo {
    private Long skuId;
    /**
     * spuId
     */
    private Long spuId;
    /**
     * sku_name
     */
    private String skuName;
    /**
     * sku_desc
     */
    private String skuDesc;
    /**
     * catalog_id
     */
    private Long catalogId;
    /**
     * brand_id
     */
    private Long brandId;
    /**
     * sku_default_img
     */
    private String skuDefaultImg;
    /**
     * sku_title
     */
    private String skuTitle;
    /**
     * sku_subtitle
     */
    private String skuSubtitle;
    /**
     * price
     */
    private BigDecimal price;
    /**
     * sale_count
     */
    private Long saleCount;
}
