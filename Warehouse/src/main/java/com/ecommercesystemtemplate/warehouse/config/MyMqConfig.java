package com.ecommercesystemtemplate.warehouse.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class MyMqConfig {

    @Bean
    public Exchange stockEventExchange() {
        return new TopicExchange("stock-event-exchange", true, false);
    }

    @Bean
    public Queue StockReleaseStockQueue() {
        return new Queue("stock.release.stock.queue", true, false,false);
    }

    @Bean
    public Queue StockDelayQueue() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", "stock-event-exchange");
        map.put("x-dead-letter-routing-key", "stock.release");
        map.put("x-message-ttl", 120000);
        Queue queue = new Queue("stock.delay.queue", true, false,false,map);
        return queue;
    }

    @Bean
    public Binding stockReleaseBinding() {
        return new Binding("stock.release.stock.queue", Binding.DestinationType.QUEUE,
                "stock-event-exchange", "stock.release.#", null);
    }

    @Bean
    public Binding stockLockBinding() {
        return new Binding("stock.delay.queue", Binding.DestinationType.QUEUE,
                "stock-event-exchange", "stock.locked", null);
    }


}
