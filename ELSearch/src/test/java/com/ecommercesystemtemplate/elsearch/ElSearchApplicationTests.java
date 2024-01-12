package com.ecommercesystemtemplate.elsearch;

import com.alibaba.fastjson.JSON;
import com.ecommercesystemtemplate.elsearch.config.ElasticConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ElSearchApplicationTests {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void contextLoads() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void indexData() throws IOException {
        IndexRequest users = new IndexRequest("users");
        users.id("1");
        User user = new User();
        user.setAge(18);
        user.setName("the first user");
        user.setGender("male");
        String json = JSON.toJSONString(user);
        users.source(json, XContentType.JSON);
        // save operation
        IndexResponse index = restHighLevelClient.index(users, ElasticConfig.COMMON_OPTIONS);
        // get response
        System.out.println(index);

    }
    @Data
    class User{
        private String name;
        private String gender;
        private Integer age;
    }





}
