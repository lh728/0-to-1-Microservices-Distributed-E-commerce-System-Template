package com.ecommercesystemtemplate.flashsale.config;

import com.ecommercesystemtemplate.flashsale.interceptor.LoginUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class FlashSaleWebConfig implements WebMvcConfigurer {
    final
    LoginUserInterceptor loginUserInterceptor;

    public FlashSaleWebConfig(LoginUserInterceptor loginUserInterceptor) {
        this.loginUserInterceptor = loginUserInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginUserInterceptor).addPathPatterns("/**");
    }
}
