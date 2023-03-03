package com.subrutin.catalog.security.model;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessJwtToken implements Token {

    private String rawToken;

    private Claims claims;

    // method ini akan dijadikan response body di com.subrutin.catalog.security.handler.UsernamePasswordAuthSuccessHandler
    @Override
    public String getToken() {
        return this.getRawToken();
    }
}
