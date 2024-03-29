package com.subrutin.catalog.config;

import com.subrutin.catalog.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // mendaftarkan Class LogInterceptor ke sini
        registry.addInterceptor(new LogInterceptor());
    }
}
