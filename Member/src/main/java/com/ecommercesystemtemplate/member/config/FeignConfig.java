package com.ecommercesystemtemplate.member.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor(){

            @Override
            public void apply(RequestTemplate requestTemplate) {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (requestAttributes != null) {
                    HttpServletRequest request = requestAttributes.getRequest();
                    if (request != null) {
                        // sync headers
                        requestTemplate.header("Cookie", request.getHeader("Cookie"));
                    }
                }

            }
        };
    }
}
