package com.ecommercesystemtemplate.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class OrderApplicationTests {
    @Autowired
    AmqpAdmin amqpAdmin;

    @Test
    public void createExchange(){
        DirectExchange directExchange = new DirectExchange("hello-exchange",true,false);
        amqpAdmin.declareExchange(directExchange);
        log.info("hello-exchange创建成功");
    }

    @Test
    public void createQueue(){
        Queue queue = new Queue("hello-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
        log.info("hello-queue创建成功","hello-java-queue");

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
        log.info("hello-java-queue绑定成功");
    }


}
