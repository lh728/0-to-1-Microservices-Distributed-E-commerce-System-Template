package com.ecommercesystemtemplate.member.web;

import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.member.feign.OrderFeignService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebController {
    final
    OrderFeignService orderFeignService;

    public WebController(OrderFeignService orderFeignService) {
        this.orderFeignService = orderFeignService;
    }

    @GetMapping("/memberOrder.html")
    public String memberOrderPage(@RequestParam(defaultValue = "1", value = "pageNum" ) Integer pageNum,
                                  Model model){
        Map<String, Object> page = new HashMap<>();
        page.put("page", pageNum);
        R r = orderFeignService.listWithItems(page);
        model.addAttribute("orders", r);

        return "orderList";
    }
}
