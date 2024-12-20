package com.ecommercesystemtemplate.order.listener;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lhjls
 * @date 2023/11/21
 * this class is used to listen order paid succeed (from notify_url)
 */
@RestController
public class OrderPaidListener {

    @PostMapping("/paid/notify")
    public String handleAlipayPaid(HttpServletRequest request){
        // if Alipay notify succeed, return "success", then Alipay will not send notify again

        return "success";
    }
}
