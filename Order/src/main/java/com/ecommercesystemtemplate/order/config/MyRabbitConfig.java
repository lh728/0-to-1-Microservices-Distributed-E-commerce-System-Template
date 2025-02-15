package com.ecommercesystemtemplate.order.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MyRabbitConfig {
    final
    RabbitTemplate rabbitTemplate;

    public MyRabbitConfig(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init(){
        // confirm  callback
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                // client receive ack
                System.out.println("confirmCallback: " + correlationData + " ack: " + ack + " cause: " + cause);
            }
        } );

        // ack  callback (message arrived to queue but not consumed)
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                //TODO error, update db error status

                System.out.println(returnedMessage.toString());
            }
        } );

    }


}
