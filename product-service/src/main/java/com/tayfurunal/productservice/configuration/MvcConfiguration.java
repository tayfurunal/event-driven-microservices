package com.tayfurunal.productservice.configuration;

import com.tayfurunal.productservice.interceptor.RequestHeaderAttributesHandlerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestHeaderAttributesHandlerInterceptor(applicationName));
    }
}
