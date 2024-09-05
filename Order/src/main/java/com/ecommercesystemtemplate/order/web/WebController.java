package com.ecommercesystemtemplate.order.web;

import com.ecommercesystemtemplate.common.exception.NoStockException;
import com.ecommercesystemtemplate.order.entity.OrderEntity;
import com.ecommercesystemtemplate.order.service.OrderService;
import com.ecommercesystemtemplate.order.vo.OrderConfirmVo;
import com.ecommercesystemtemplate.order.vo.OrderSubmitVo;
import com.ecommercesystemtemplate.order.vo.SubmitOrderResponseVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Controller
public class WebController {
    final
    OrderService orderService;
    final RabbitTemplate rabbitTemplate;

    public WebController(OrderService orderService, RabbitTemplate rabbitTemplate) {
        this.orderService = orderService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/test/createOrder")
    @ResponseBody
    public String createOrderTest(){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(UUID.randomUUID().toString());
        orderEntity.setModifyTime(new Date());
        rabbitTemplate.convertAndSend("order-event-exchange", "order.create.order", orderEntity);
        return "ok";
    }


    @GetMapping("/{page}.html")
    public String listPage(@PathVariable("page") String page) {
        return page;
    }

    @GetMapping("/toTrade")
    public String toTrade(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = orderService.confirmOrder();
        model.addAttribute("orderConfirmData",orderConfirmVo);
        return "confirm";
    }

    /**
     * create order, check anti-resubmit token, check payPrice, check stock
     * @param orderSubmitVo
     * @return
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo orderSubmitVo, Model model, RedirectAttributes redirectAttributes) {
        try{

            SubmitOrderResponseVo submitOrderResponseVo = orderService.submitOrder(orderSubmitVo);
            if (submitOrderResponseVo.getStatusCode() == 0) {
                // 1. if success, go to payment page
                model.addAttribute("submitOrderResponse",submitOrderResponseVo);
                return "pay";
            } else {
                // 2. if fail, go back to confirm page
                String message = "Submit Order Fail, ";
                if (submitOrderResponseVo.getStatusCode() == 1) {
                    message += "Order info expired, please re-submit";
                } else if (submitOrderResponseVo.getStatusCode() == 2) {
                    message += "Order price updated, please check then re-submit";
                } else if (submitOrderResponseVo.getStatusCode() == 3) {
                    message += "Stock Check Fail, order stock is not enough";
                }
                model.addAttribute("message",message);
                redirectAttributes.addFlashAttribute("msg",message);
                return "redirect:http://order.thellumall.com/toTrade";
            }
        }catch (Exception e){
            if (e instanceof NoStockException){
                String message = e.getMessage();
                redirectAttributes.addFlashAttribute("msg",message);
            }
        }
        return "redirect:http://order.thellumall.com/toTrade";

    }
}
