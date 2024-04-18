package com.ecommercesystemtemplate.thirdparty.component;

import com.ecommercesystemtemplate.thirdparty.utils.HttpUtils;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
public class SmsComponent {

    private String host;
    private String path;
    private String smsSignId;
    private String templateId;
    private String appcode;
    public void sendSms(String phoneNumber, String message) {
        String method = "POST";
        String appcode = "your own APPcode";
        Map<String, String> headers = new HashMap<String, String>();
        //header format: Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phoneNumber);
        // message : "**code**:12345,**minute**:5"
        querys.put("param", message);
        querys.put("smsSignId", smsSignId);
        querys.put("templateId", templateId);
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
