package com.ecommercesystemtemplate.flashsale.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FlashSaleSessionsWithSku {

    /**
     * id
     */
    private Long id;
    /**
     * name
     */
    private String name;
    /**
     * start_time
     */
    private Date startTime;
    /**
     * end_time
     */
    private Date endTime;
    /**
     * status
     */
    private Integer status;
    /**
     * create_time
     */
    private Date createTime;

    private List<FlashSaleSkuVo> flashSaleSkuVos;

}
