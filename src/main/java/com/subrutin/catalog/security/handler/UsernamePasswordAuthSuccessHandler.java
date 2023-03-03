package com.subrutin.catalog.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subrutin.catalog.security.model.AccessJwtToken;
import com.subrutin.catalog.security.util.JwtTokenFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class UsernamePasswordAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    private final JwtTokenFactory jwtTokenFactory;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        // mendapatkan username dan lainnya
        // nilai authentication dihasilkan dari com.subrutin.catalog.security.provider.UsernamePasswordAuthProvider.authenticate()
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // membuat raw token dan claims
        AccessJwtToken token = jwtTokenFactory.createAccessJWTToken(userDetails.getUsername(), userDetails.getAuthorities());
        // membuat map yang akan digunakan sebagai payload
        Map<String, String> resultMap = new HashMap<>();
        // resultMap.put("result", "OK");
        resultMap.put("result", token.getToken()); // token dijadikan nilai kembalian untuk di konsumsi di front-end
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), resultMap);
    }
}
