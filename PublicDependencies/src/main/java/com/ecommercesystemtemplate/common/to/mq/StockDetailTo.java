package com.ecommercesystemtemplate.common.to.mq;

import lombok.Data;

@Data
public class StockDetailTo {

    private Long id;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * sku_name
     */
    private String skuName;
    /**
     * sku_num
     */
    private Integer skuNum;
    /**
     * task_id
     */
    private Long taskId;
    /**
     * ware_id
     */
    private Long wareId;
    /**
     * lock_status
     */
    private Integer lockStatus;
}
