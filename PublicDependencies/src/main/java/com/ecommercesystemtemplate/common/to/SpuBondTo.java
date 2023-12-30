package com.ecommercesystemtemplate.common.to;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpuBondTo {

    private Long spuId;

    private BigDecimal buyBounds;

    private BigDecimal growBounds;


}
