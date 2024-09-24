package com.ecommercesystemtemplate.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.order.entity.OrderEntity;
import com.ecommercesystemtemplate.order.vo.OrderConfirmVo;
import com.ecommercesystemtemplate.order.vo.OrderSubmitVo;
import com.ecommercesystemtemplate.order.vo.SubmitOrderResponseVo;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * order
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 19:53:27
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * return correspondent data to order confirm page
     * @return
     */
    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo);

    OrderEntity getOrderStatus(String orderSn);

    void closeOrder(OrderEntity orderEntity);
}

