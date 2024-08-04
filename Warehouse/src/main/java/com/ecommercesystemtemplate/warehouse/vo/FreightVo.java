package com.ecommercesystemtemplate.warehouse.vo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class FreightVo {

    private MemberAddressVo address;
    private BigDecimal freight;
}
