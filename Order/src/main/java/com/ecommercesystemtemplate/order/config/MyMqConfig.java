package com.ecommercesystemtemplate.order.config;

import com.ecommercesystemtemplate.order.entity.OrderEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;

@Configuration
public class MyMqConfig {

    @RabbitListener(queues = "order.release.queue")
    public void listener(OrderEntity orderEntity, Channel channel, Message message) throws IOException {

        System.out.println(orderEntity.getOrderSn());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
    @Bean
    public Queue orderDelayQueue() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", "order-event-exchange");
        map.put("x-dead-letter-routing-key", "order.release.order");
        map.put("x-message-ttl", 60000 );
        Queue queue = new Queue("order.delay.queue", true, false, false,map);
        return queue;
    }
    @Bean
    public Queue orderReleaseQueue() {
        return new Queue("order.release.queue", true, false, false);
    }

    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange("order-event-exchange", true, false);
    }

    @Bean
    public Binding orderCreateBinding() {
        return new Binding("order.delay.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.create.order", null);
    }
    @Bean
    public Binding orderReleaseBinding() {
        return new Binding("order.release.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.release.order", null);

    }

}
