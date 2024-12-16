package com.ecommercesystemtemplate.member.config;

import com.ecommercesystemtemplate.member.interceptor.LoginUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MemberWebConfig implements WebMvcConfigurer {

    final
    LoginUserInterceptor loginUserInterceptor;

    public MemberWebConfig(LoginUserInterceptor loginUserInterceptor) {
        this.loginUserInterceptor = loginUserInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginUserInterceptor).addPathPatterns("/**");
    }
}
