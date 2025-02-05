package com.ecommercesystemtemplate.order.listener;

import com.ecommercesystemtemplate.common.to.mq.QuickFlashSaleOrderTo;
import com.ecommercesystemtemplate.order.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = "order.flashSale.order.queue")
@Slf4j
public class OrderFlashSaleListener {

    final
    OrderService orderService;

    public OrderFlashSaleListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitHandler
    public void listener(QuickFlashSaleOrderTo orderEntity, Channel channel, Message message) throws IOException {
        try{
            log.info("ready to create flash sale order detail info");
            orderService.createFlashSaleOrder(orderEntity);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
