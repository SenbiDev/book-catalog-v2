package com.subrutin.catalog.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
@Component
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Hello from Filter {}", request.getLocalAddr()); // filter ini digunakan untuk melacak request yang masuk
        chain.doFilter(request, response);
    }
}
