package com.subrutin.catalog.security.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.security.Key;

public class RawAccessJwtToken implements Token {

    private String token;

    public RawAccessJwtToken(String token) {
        super();
        this.token = token;
    }

    // digunakan pada com.subrutin.catalog.security.provider.JwtAuthenticationProvider.authenticate()
    public Jws<Claims> parseClaims(Key signingKey) {
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
    }

    @Override
    public String getToken() {
        return this.token;
    }
}
