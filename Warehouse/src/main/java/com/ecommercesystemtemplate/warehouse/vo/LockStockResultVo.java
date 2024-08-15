package com.ecommercesystemtemplate.warehouse.vo;

import lombok.Data;

@Data
public class LockStockResultVo {

    private Long skuId;
    private Boolean locked;
    private Integer stock;
}
