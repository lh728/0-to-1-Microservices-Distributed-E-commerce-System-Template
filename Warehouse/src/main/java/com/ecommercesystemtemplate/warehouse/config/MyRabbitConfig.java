package com.ecommercesystemtemplate.warehouse.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRabbitConfig {

    final
    RabbitTemplate rabbitTemplate;

    public MyRabbitConfig(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
}
