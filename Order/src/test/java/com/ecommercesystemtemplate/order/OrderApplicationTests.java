package com.ecommercesystemtemplate.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class OrderApplicationTests {
    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void send(){
        rabbitTemplate.convertAndSend("hello-exchange","hello.java","hello java");
        log.info("send message success");
    }

    @Test
    public void createExchange(){
        DirectExchange directExchange = new DirectExchange("hello-exchange",true,false);
        amqpAdmin.declareExchange(directExchange);
        log.info("hello-exchange create success");
    }

    @Test
    public void createQueue(){
        Queue queue = new Queue("hello-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
        log.info("hello-queue create success","hello-java-queue");

    }

    @Test
    public void createBinding(){
        amqpAdmin.declareBinding(
                new Binding("hello-queue",
                        Binding.DestinationType.QUEUE,
                        "hello-exchange",
                        "hello.java",
                        null
                )
        );
        log.info("hello-java-queue bind hello-exchange success");
    }


}
