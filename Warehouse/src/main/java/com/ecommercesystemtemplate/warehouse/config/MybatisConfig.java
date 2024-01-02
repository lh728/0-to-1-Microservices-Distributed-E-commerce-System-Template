package com.ecommercesystemtemplate.warehouse.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("com.ecommercesystemtemplate.warehouse.dao")
public class MybatisConfig {

    // import page plugin
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // set page limit
        paginationInterceptor.setLimit(1000);
        // set page overflow
        paginationInterceptor.setOverflow(true);
        return paginationInterceptor;
    }
}
