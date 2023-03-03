package com.subrutin.catalog.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subrutin.catalog.dto.LoginRequestDTO;
import com.subrutin.catalog.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsernamePasswordAuthProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    public UsernamePasswordAuthProcessingFilter(
            String defaultFilterProcessesUrl,
            ObjectMapper objectMapper,
            AuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler
    ) {
        super(defaultFilterProcessesUrl);
        this.objectMapper = objectMapper;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    // Authentication adalah interface yang merepresentasikan token untuk melakukan request process authentication
    // kelas yang mengimplementasi Authentication akan disimpan dalam SecurityContext yang akan dikelola oleh security context holder
    // buka AppUserImpl, maka kelas yang mengimplementasi Authentication bisa diakses di kelas itu.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // convert payload dari user 'request.getReader()' menjadi object LoginRequestDTO
        LoginRequestDTO dto = objectMapper.readValue(request.getReader(), LoginRequestDTO.class); // mendapatkan payload yang dikirim user
        if (StringUtils.isBlank(dto.username()) || StringUtils.isBlank(dto.password())) { // memvalidasi payload jika kosong
            throw new BadRequestException("username.password.should.be.provided");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        // delegasikan ke AuthenticationManager() untuk melakukan proses otentikasi
        // kode ini merujuk ke com.subrutin.catalog.security.provider.UsernamePasswordAuthProvider.authenticate(Authentication authentication)
        return this.getAuthenticationManager().authenticate(token);
    }

    // membuat proses ketika otentikasi sukses
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // method onAuthenticationSuccess diimplementasi oleh com.subrutin.catalog.security.handler UsernamePasswordAuthSuccessHandler
        this.successHandler.onAuthenticationSuccess(request, response, chain, authResult);
    }

    // membuat proses ketika otentikasi gagal
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // method onAuthenticationFailure diimplementasi oleh com.subrutin.catalog.security.handler UsernamePasswordAuthFailureHandler
        this.failureHandler.onAuthenticationFailure(request, response, failed);
    }


}
