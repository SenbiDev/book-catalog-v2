package com.subrutin.catalog.security.provider;

import com.subrutin.catalog.security.model.JwtAuthenticationToken;
import com.subrutin.catalog.security.model.RawAccessJwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Key key;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // authentication berisi nilai dari com.subrutin.catalog.security.filter.JwtAuthProcessingFilter
        // JwtAuthProcessingFilter menerima request header yang awalnya dibuat oleh com.subrutin.catalog.security.util.JwtTokenFactory lalu dikirim ke user
        RawAccessJwtToken token = (RawAccessJwtToken) authentication.getCredentials();
        // mendekode token dengan cara parsing, lalu ambil nilanya
        Jws<Claims> jwsClaims = token.parseClaims(key);
        String subject = jwsClaims.getBody().getSubject();
        List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
        // convert scopes bertipe List<Sting> ke tipe List<GrantedAuthority>
        List<GrantedAuthority> authorities = scopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return authorities;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return subject;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

        return new JwtAuthenticationToken(userDetails, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
