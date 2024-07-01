package com.ecommercesystemtemplate.order;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderApplicationTests {
    @Autowired
    AmqpAdmin amqpAdmin;

    @Test
    public void createExchange(){

    }


}
