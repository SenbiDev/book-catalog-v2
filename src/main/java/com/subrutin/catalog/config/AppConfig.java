package com.subrutin.catalog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subrutin.catalog.security.util.JwtTokenFactory;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
public class AppConfig {

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Key key() {
        byte[] keyBytes = Decoders.BASE64.decode("23324hj5f23ghjk3f4jh5f23hj45fkj234h5fhj23f54kj23f5hj23f4hk53452hf523jk");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Bean
    public JwtTokenFactory jwtTokenFactory(Key key) {
        return new JwtTokenFactory(key);
    }
}
