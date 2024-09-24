package com.ecommercesystemtemplate.order.listener;

import com.ecommercesystemtemplate.order.entity.OrderEntity;
import com.ecommercesystemtemplate.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = "order.release.queue")
public class OrderCloseListener {

    final OrderService orderService;

    public OrderCloseListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitHandler
    public void listener(OrderEntity orderEntity, Channel channel, Message message) throws IOException {
//        System.out.println(orderEntity.getOrderSn());
        try{
            orderService.closeOrder(orderEntity);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

}
