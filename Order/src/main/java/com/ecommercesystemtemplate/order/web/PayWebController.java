package com.ecommercesystemtemplate.order.web;

import com.alipay.api.AlipayApiException;
import com.ecommercesystemtemplate.order.config.AlipayTemplate;
import com.ecommercesystemtemplate.order.service.OrderService;
import com.ecommercesystemtemplate.order.vo.PayVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PayWebController {
    private final AlipayTemplate alipayTemplate;
    private final OrderService orderService;

    public PayWebController(AlipayTemplate alipayTemplate, OrderService orderService) {
        this.alipayTemplate = alipayTemplate;
        this.orderService = orderService;
    }

    @GetMapping("/payOrder")
    @ResponseBody
    public String payOrder(@RequestParam("OrderSn") String orderSn) throws AlipayApiException {
        PayVo payVo = new PayVo();
        payVo.setOut_trade_no(orderSn);
        payVo.setTotal_amount("0.01");
        payVo.setSubject("hello");
        payVo.setBody("hello");
        String pay = alipayTemplate.pay(payVo);
        System.out.println(pay);
        return "hello";
    }
}
