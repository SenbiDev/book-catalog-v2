package com.subrutin.catalog.security.filter;

import com.subrutin.catalog.security.model.AnonymousAuthentication;
import com.subrutin.catalog.security.model.JwtAuthenticationToken;
import com.subrutin.catalog.security.model.RawAccessJwtToken;
import com.subrutin.catalog.util.TokenExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final TokenExtractor tokenExtractor;

    private final AuthenticationFailureHandler failureHandler;

    public JwtAuthProcessingFilter(
            RequestMatcher requiresAuthenticationRequestMatcher,
            TokenExtractor tokenExtractor,
            AuthenticationFailureHandler failureHandler
    ) {
        super(requiresAuthenticationRequestMatcher);
        this.tokenExtractor = tokenExtractor;
        this.failureHandler = failureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String authorizationHeader = request.getHeader("Authorization");
        String jwt = tokenExtractor.extract(authorizationHeader);
        RawAccessJwtToken rawToken = new RawAccessJwtToken(jwt);
        return this.getAuthenticationManager().authenticate(new JwtAuthenticationToken(rawToken));
    }

    // aksi yang dilakukan ketika proses otentikasi sukses
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // buat securitycontext untuk menyimpan session dari otentikasinya tadi
        SecurityContext context = SecurityContextHolder.createEmptyContext(); // kosong holdernya dengan method createEmptyContext
        context.setAuthentication(authResult); // simpan otentikasinya dengan setsetAuthentication()
        SecurityContextHolder.setContext(context); // simpan lagi kedalam holder
        chain.doFilter(request, response);
        // disini tidak menggunakan handler seperti kode dalam unsuccessfulAuthentication-
        // karena akan langsung diteruskan ke filter berikutnya atau ke bagian servlet.
    }

    // aksi yang dilakukan ketika proses otentikasi gagal, misalnya jwt-nya expired / tidak valid
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext(); // bersihkan dulu context-nya
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthentication());
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
