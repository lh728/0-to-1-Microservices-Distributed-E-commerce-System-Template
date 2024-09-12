package com.ecommercesystemtemplate.warehouse.listener;

import com.ecommercesystemtemplate.common.to.mq.StockLockedTo;
import com.ecommercesystemtemplate.warehouse.service.WareSkuService;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = "stock.release.stock.queue")
@AllArgsConstructor
public class StockReleaseListener {

    final WareSkuService wareSkuService;


    /**
     * 1. stock auto unlock
     *      order success, stock locked success; if transaction rollback, unlock stock
     * 2. order fail
     *      stock lock fail(for example, one of warehouses has no stock)
     *
     * @param to
     * @param message
     */
    @RabbitHandler
    public void handleStockUnlocked(StockLockedTo to, Message message, Channel channel) throws IOException {
        try{
            wareSkuService.unlockStock(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }


    }




}
