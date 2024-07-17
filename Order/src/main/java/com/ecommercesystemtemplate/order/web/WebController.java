package com.ecommercesystemtemplate.order.web;

import com.ecommercesystemtemplate.order.service.OrderService;
import com.ecommercesystemtemplate.order.vo.OrderConfirmVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {
    final
    OrderService orderService;

    public WebController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{page}.html")
    public String listPage(@PathVariable("page") String page) {
        return page;
    }

    @GetMapping("/toTrade")
    public String toTrade(Model model){
        OrderConfirmVo orderConfirmVo = orderService.confirmOrder();
        model.addAttribute("orderConfirmData",orderConfirmVo);
        return "confirm";
    }
}
