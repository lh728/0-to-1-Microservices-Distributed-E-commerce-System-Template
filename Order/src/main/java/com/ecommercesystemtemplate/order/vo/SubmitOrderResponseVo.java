package com.ecommercesystemtemplate.order.vo;

import com.ecommercesystemtemplate.order.entity.OrderEntity;
import lombok.Data;

@Data
public class SubmitOrderResponseVo {
    private OrderEntity order;

    /**
     * 0 success
     */
    private Integer StatusCode;
}
