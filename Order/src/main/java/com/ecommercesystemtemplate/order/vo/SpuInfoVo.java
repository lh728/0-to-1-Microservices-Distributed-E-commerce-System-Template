package com.ecommercesystemtemplate.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SpuInfoVo {

    private Long id;
    /**
     * spu_name
     */
    private String spuName;
    /**
     * spu_description
     */
    private String spuDescription;
    /**
     * catalog_id
     */
    private Long catalogId;
    /**
     * brand_id
     */
    private Long brandId;
    /**
     *
     */
    private BigDecimal weight;
    /**
     * Listing status [0 - Down, 1 - Up]
     */
    private Integer publishStatus;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date updateTime;


}
