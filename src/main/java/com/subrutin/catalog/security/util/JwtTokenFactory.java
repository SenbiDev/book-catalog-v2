package com.subrutin.catalog.security.util;

import com.subrutin.catalog.security.model.AccessJwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JwtTokenFactory {

    private final Key secret;

    public AccessJwtToken createAccessJWTToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Claims claims = Jwts.claims().setSubject(username);
        // menambahkan claims yang lain dengan put()
        claims.put("scopes", authorities.stream().map(a -> a.getAuthority()).collect(Collectors.toList())); // mendapatkan role user
        // terkain expired time -nya
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiredTime = currentTime.plusMinutes(15); // memperpanjang atau memperpendek dari umur jwt-nya

        // convert currentTime & expiredTime ke kelas date
        Date currentTimeDate = Date.from(currentTime.atZone(ZoneId.of("Asia/Jakarta")).toInstant());
        Date expiredTimeDate = Date.from(expiredTime.atZone(ZoneId.of("Asia/Jakarta")).toInstant());

        // generate(buat) token dengan class Jwts
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://subrutin.com") // penerbit
                .setIssuedAt(currentTimeDate) // diterbitkan kapan
                .setExpiration(expiredTimeDate) // kapan expire-nya
                .signWith(secret, SignatureAlgorithm.HS256) // (secretKey, signature)
                .compact(); // generate token-nya lalu tampung di variable String token

        return new AccessJwtToken(token, claims);
    }
}
