package com.subrutin.catalog.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

// kelas custom untuk mengimplementasi Authentication pada method-
// com.subrutin.catalog.security.filter.attemptAuthentication()
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private RawAccessJwtToken rawAccessJwtToken;

    private UserDetails userDetails;

    // digunakan untuk membuat token yang-
    // nantinya dikirim dari filter ke provider sebelum proses otentikasi-nya itu dilakukan
    public JwtAuthenticationToken(RawAccessJwtToken rawAccessJwtToken) {
        super(null); // set otorisasinya ke null, karena belum di-otentikasi
        this.rawAccessJwtToken = rawAccessJwtToken;
        super.setAuthenticated(false);
    }

    // konstruktor ini digunakan ketika proses otentikasi-nya berhasil
    // digunakan oleh com.subrutin.catalog.security.provider.JwtAuthenticationProvider.authenticate()
    public JwtAuthenticationToken(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.userDetails = userDetails;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.rawAccessJwtToken;
    }

    @Override
    public Object getPrincipal() {
        return this.userDetails;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.rawAccessJwtToken = null;
    }
}
