package com.ecommercesystemtemplate.order.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MyRabbitConfig {
    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * use JSON serialization
     * @return
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter(){

        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct
    public void init(){
        // confirm  callback
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("confirmCallback: " + correlationData + " ack: " + ack + " cause: " + cause);
            }
        } );

        // ack  callback (message arrived to queue but not consumed)
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                System.out.println(returnedMessage.toString());
            }
        } );

    }


}
