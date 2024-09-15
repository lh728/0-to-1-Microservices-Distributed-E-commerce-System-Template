package com.ecommercesystemtemplate.common.to.mq;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockLockedTo implements Serializable {

    private Long id;
    private StockDetailTo detail;
}
