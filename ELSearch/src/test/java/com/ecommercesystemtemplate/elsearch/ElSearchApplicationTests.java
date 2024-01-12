package com.ecommercesystemtemplate.elsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ElSearchApplicationTests {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void contextLoads() {
        System.out.println(restHighLevelClient);
    }





}
