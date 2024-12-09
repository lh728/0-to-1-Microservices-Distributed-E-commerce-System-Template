package com.ecommercesystemtemplate.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.ecommercesystemtemplate.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //The id of the application created in Alipay
    private   String app_id = "2016092200568607";

    // Merchant private key, your RSA2 private key in PKCS8 format
    private  String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCSgX/nTQ0lD+S8ObaM5LGZ1hiz18GXnNpqPLhJCym4xOpn35FNPHrPkDGEoMKrZ5LJeA4cZulckD8AtpvBCpeyIkrj/i1WVmSg10hVX67MlVets4UecCHZv2hKAN0/iId76kozdqrd7Csp/YgXPquN9Np0NFotggTrmiBANk+vcpTF9SCGrDq/isOoCvClfbvVJjApfLLOel3yECe5K/SZ8puiWILVm1NxEXAqJ8z0ipPZVGrXsT6Bo0pEyCPcEL0SqaC9WT0zdWQzdUknCzZV9W2wKjEXBJG9hqxay5kPaKm9leBatSkDAaDxH/N5g36HRfY7BmklwRZsp17lHinxAgMBAAECggEAfnnfck35WBKFc90a9D0F+Xlzr+ZGEV3uzKIIsb46UXFlrzC5HoVkvEWOCiJCjHiIpvbGr8xED43TZgk/IwLC/JxQLM0kVJGWo6fWoSVOIP2YSLNe620APBvaq3BdkFiMJfSYBB+g2J7mkIR39SE8Nvu3j3QWmYzSNJbE2spINnwTzNBL1OPaB5h3hSjyI07KaUcOjhTBF0EZl83NlBDsxmQvy0NmuOIWAcIXXvGoIbwkA774J3LhwL+VS4W2FpQj4FlxvDlPu24GeNWN7oO66T3Jp9bweO120ObhuKwZQosDGkJq0975zVSJX5QtUWHMM/QDPO8Pk24n2AoPcACQcQKBgQDS6kqD+sK8dDBpkmxYopA1gJJATnur0RHFZJb5webOhnEZnePhB1hhhGvKFcrdY2hcYeQiUZkHMsnWItNUe9E9ccp4++m6KKG0iV/BQda7zx1zMTTZUMvSbO282Q31YnQu7Yz6BSk4f/U5Qbu61AK53Tv1ejSAgQhXt1Pwq8KD7QKBgQCx0pkqW4+53tY2o4iPqFGjKYI2yk5bAH5etmOvW51OZ4Slsq/aUJKBVG6fOpRVKkiXulHhrp5csZH0/C7kaj4Hy7TjgUKSWvwlv7i7jgN0dq/bhVJz82y+N9pENWvy5J0I8Kt67XH+6JDEGWjlV58auifMRSx5mRJNn5pM6qrFlQKBgFyZWm/JV1fv1xVyoLjlXlTvBsbO7kMH/jpgqFwtAk1n/x3VEShJ1kayIbTOjotWSopMvCFJG9tqM+0cyxWLatkELXWifAIsNpqRuYWah1FbZD2fu+kxLNtM0a+YyCUUvZeg2cUnIOraWupxbp9e13eMpvdmWMiWXfhM18CRWEwdAoGAUwT0l076EhgUQJwm1JML0jY94eCfpmLbnNJgRe1qysEPr+B1s2IslA7cOqC5we0kyRmmwsuoibQpZYwbRG7JmRAk2pZtgzDRSbpxv7a0rDoBLmbXMOU0Hraqw2+Bf3v2SMc79/9FWnIvrC4EyBYZZPwGOpsNAZRSdEUQX9qrceUCgYB99OOtFFt1ixzyTCyUj3Fuiw7BsPhdI3nuMSoNTPIDNpzRBp/KFXyv/FNJ2CjTAsX3OR3D6KmEYihqUfrYeb0P5zoybcQLMxbXxK+ec6F2o6U2iqFIq0MKwHUqsb9X3pj4qE0ZHbFgRtIHnL2/QGV5PFJdmIZIBKZcvB8fW6ztDA==";
    // Alipay public key, view address: https://openhome.alipay.com/platform/keyManage.htm Alipay public key corresponding to APPID。
    private  String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyQQceVUChTJGtF/a8SXufhSxDTKporieTq9NO7yDZSpDlAX1zVPT/nf0KWAlxq1TYappWMIYtyrOABhJyn6flNP6vuSBiM5lYsepHvYrtRHqlFiJruEkiaCgEZBKL5aCfBHYj0oqgQn9MpNV/PEH4cBYAVaiI4+VX8CBUQfeEGjgN6OkpLULZ3X0JUkmSnVvCNJ1m3PD68IIlbOfEZXJUKCqmZhzprGR5VWswjxA+g87cMwvijL4gdkSy/daG62Bz5vApcmmMkuX1k1fMWP4ajZCASVw8HD+MSLRhd8We9F97gd8CW0TavzbdR+mTS5H4yEgO8F9HRAsbkhV9yu0yQIDAQAB";
    // The server [asynchronous notification] page path must be a complete path in the http:// format.
    // Custom parameters such as ?id=123 cannot be added. It must be accessible from the external network.
    // Alipay will quietly send us a request to tell us that the payment was successful.
    private  String notify_url;

    // The page path for page jump synchronization notification needs to be a complete path in the http:// format. Custom parameters such as ?id=123 cannot be added. It must be accessible normally on the external network.The page path for page jump synchronization notification needs to be a complete path in the http:// format.
    // ustom parameters such as ?id=123 cannot be added. It must be accessible normally on the external network.
    //Synchronous notification, payment successful, usually jump to the success page
    private  String return_url;

    // sign_type
    private  String sign_type = "RSA2";

    // charset
    private  String charset = "utf-8";

    // Alipay gatewayUrl； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、Generate a payment client based on Alipay configuration
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        // 2、Create a payment request
        // Set request parameters
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        // Merchant order number, the only order number in the merchant website order system, required
        String out_trade_no = vo.getOut_trade_no();
        // Payment amount, required
        String total_amount = vo.getTotal_amount();
        // Order name, required
        String subject = vo.getSubject();
        // Product description, optional
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //You will receive a response from Alipay, which is a web page.
        // As long as the browser displays this page, you will automatically go to the Alipay cashier page.
        System.out.println("response："+result);

        return result;

    }
}
