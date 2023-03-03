package com.subrutin.catalog.security.provider;

import com.subrutin.catalog.exception.BadRequestException;
import com.subrutin.catalog.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UsernamePasswordAuthProvider implements AuthenticationProvider {

    private final AppUserService appUserService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // mengekstrak username dan password
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        // mengambil data username dari database
        UserDetails userDetails = appUserService.loadUserByUsername(username);

        // mecocokkan password dari input dengan password dari database
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadRequestException("invalid.username.password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // untuk mengetahui apakah objek authentication cocok tidak dengan authentication provider yang digunakan
    // untuk menentukan apakah token yang dimasukkan oleh user itu didukung tidak dengan mekanisme otentikasi pada provider ini
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
