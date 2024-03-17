package com.ecommercesystemtemplate.elsearch.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lhjls
 */
@Data
public class SearchParam {

    /**
     * full-text search keyword
     */
    private String keyword;

    /**
     * third-level catalog1Id
     */
    private Long catalog3Id;

    /**
     * sort = sale_count asc/ sale_count desc/ skuPrice:asc / skuPrice:desc / hotScore:asc / hotScore:desc
     */
    private String sort;

    /**
     * filter conditions:
     * hasStock=0/1
     * skuPrice=1_500/_500/500_
     * brandId=1
     * attrs=1_5寸:8寸
     */
    private Integer hasStock;
    private String skuPrice;
    private List<Long> brandId;
    private List<String> attrs;
    private Integer pageNum = 1;

}
