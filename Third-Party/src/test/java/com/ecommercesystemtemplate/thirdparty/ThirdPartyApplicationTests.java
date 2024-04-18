package com.ecommercesystemtemplate.thirdparty;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.ecommercesystemtemplate.thirdparty.component.SmsComponent;
import com.ecommercesystemtemplate.thirdparty.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ThirdPartyApplicationTests {

	@Resource
	OSSClient ossClient;

	@Resource
	SmsComponent smsComponent;

	@Test
	void contextLoads() {
	}

	@Test
	public void saveFile() {
		// download file to local
		ossClient.putObject("0-to-1-microservices-distributed-e-commerce-system-template",
				"test.png",
				new File("D:\\0-to-1-Microservices-Distributed-E-commerce-System-Template\\Static\\test.png"));
		ossClient.shutdown();
	}

	@Test
	public void testUpload() throws FileNotFoundException, com.aliyuncs.exceptions.ClientException {
		String endpoint = "oss-cn-beijing.aliyuncs.com";
		EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
		String bucketName = "0-to-1-microservices-distributed-e-commerce-system-template";
		String objectName = "test.png";
		String filePath= "D:\\0-to-1-Microservices-Distributed-E-commerce-System-Template\\Static\\test.png";

		OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);

		try {
			InputStream inputStream = new FileInputStream(filePath);
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
			PutObjectResult result = ossClient.putObject(putObjectRequest);
		} catch (OSSException oe) {
			System.out.println("Caught an OSSException, which means your request made it to OSS, "
					+ "but was rejected with an error response for some reason.");
			System.out.println("Error Message:" + oe.getErrorMessage());
			System.out.println("Error Code:" + oe.getErrorCode());
			System.out.println("Request ID:" + oe.getRequestId());
			System.out.println("Host ID:" + oe.getHostId());
		} catch (ClientException ce) {
			System.out.println("Caught an ClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with OSS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message:" + ce.getMessage());
		} finally {
			if (ossClient != null) {
				ossClient.shutdown();
			}
		}
	}

	@Test
	public void testSMS(){
		String host = "http://gyytz.market.alicloudapi.com";
		String path = "/sms/smsSend";
		String method = "POST";
		String appcode = "your own APPcode";
		Map<String, String> headers = new HashMap<String, String>();
		//header format: Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("mobile", "mobile");
		querys.put("param", "**code**:12345,**minute**:5");
		querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
		querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
		Map<String, String> bodys = new HashMap<String, String>();

		try {
			HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
			System.out.println(response.toString());
			//System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSendSms(){
		smsComponent.sendSms("your phone number", "12345");
	}

}
