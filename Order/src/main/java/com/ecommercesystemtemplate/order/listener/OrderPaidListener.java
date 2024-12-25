package com.ecommercesystemtemplate.order.listener;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ecommercesystemtemplate.order.config.AlipayTemplate;
import com.ecommercesystemtemplate.order.service.OrderService;
import com.ecommercesystemtemplate.order.vo.PayAsyncVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lhjls
 * @date 2023/11/21
 * this class is used to listen order paid succeed (from notify_url)
 */
@RestController
public class OrderPaidListener {

    final
    OrderService orderService;
    final AlipayTemplate alipayTemplate;

    public OrderPaidListener(OrderService orderService, AlipayTemplate alipayTemplate) {
        this.orderService = orderService;
        this.alipayTemplate = alipayTemplate;
    }

    @PostMapping("/paid/notify")
    public String handleAlipayPaid(HttpServletRequest request, PayAsyncVo payAsyncVo) throws UnsupportedEncodingException, AlipayApiException {
        // if Alipay notify succeed, return "success", then Alipay will not send notify again
        // check signature
        Map<String,String> params = new HashMap<>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = request.getParameterValues(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // solve the problem of Chinese character
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name,valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1( params, alipayTemplate.getAlipay_public_key(),
                alipayTemplate.getCharset(), alipayTemplate.getSign_type());
        if (!signVerified) {
            return "error";
        } else{
            return orderService.handlePayResult(payAsyncVo);
        }
    }
}
