package com.subrutin.catalog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subrutin.catalog.security.filter.JwtAuthProcessingFilter;
import com.subrutin.catalog.security.filter.UsernamePasswordAuthProcessingFilter;
import com.subrutin.catalog.security.handler.UsernamePasswordAuthFailureHandler;
import com.subrutin.catalog.security.handler.UsernamePasswordAuthSuccessHandler;
import com.subrutin.catalog.security.provider.JwtAuthenticationProvider;
import com.subrutin.catalog.security.provider.UsernamePasswordAuthProvider;
import com.subrutin.catalog.security.util.JwtTokenFactory;
import com.subrutin.catalog.security.util.SkipPathRequestMatcher;
import com.subrutin.catalog.util.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

//import com.subrutin.catalog.service.AppUserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.crypto.password.PasswordEncoder;

//@EnableWebSecurity
////@EnableMethodSecurity
//@EnableGlobalMethodSecurity


//@Configuration
//public class SecurityConfig {
//
//    @Autowired
//    private AppUserService appUserService;
//
//    @Autowired
//    void registerProvider(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
//        auth.userDetailsService(appUserService).passwordEncoder(passwordEncoder);
//    }
//}

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //    @Autowired
    //    private AppUserService appUserService;

    // resource ini belum di buat di package com.subrutin.catalog.web
    // oleh karena itu diasumsikan ditangani secara otomatis oleh framework
    private final static String AUTH_URL = "/v1/login";
    private final static String V1_URL = "/v1/*";
    private final static String V2_URL = "/v2/*";

    private final static List<String> PERMIT_ENDPOINT_LIST = Arrays.asList(AUTH_URL);
    private final static List<String> AUTHENTICATED_ENDPOINT_LIST = Arrays.asList(V1_URL, V2_URL);

    @Autowired
    private UsernamePasswordAuthProvider usernamePasswordAuthProvider; //

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Bean
    public AuthenticationSuccessHandler successHandler(ObjectMapper objectMapper, JwtTokenFactory jwtTokenFactory) {
        return new UsernamePasswordAuthSuccessHandler(objectMapper, jwtTokenFactory);
    }

    @Bean
    public AuthenticationFailureHandler failureHandler(ObjectMapper objectMapper) {
        return new UsernamePasswordAuthFailureHandler(objectMapper);
    }

    //
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UsernamePasswordAuthProcessingFilter usernamePasswordAuthProcessingFilter(
            ObjectMapper objectMapper,
            AuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler,
            AuthenticationManager authenticationManager
    ) {
        // mendaftarkan usernamePasswordAuthProcessingFilter ke SecurityConfig
        // argumen pertama : path dari request yang ingin di-intercept

        // karena filter yang dibuat ini adalah filter kustom maka kita perlu memastikan bahwa-
        // filter yang kita buat ini menggunakan authenticationManager yang telah dideklarasikan pada baris 77-
        // caranya akses 'setAuthenticationManager()' dari filter lalu masukkan argument 'authenticationManager' yang-
        // telah diinject di baris kode 76
        UsernamePasswordAuthProcessingFilter filter = new UsernamePasswordAuthProcessingFilter(AUTH_URL, objectMapper, successHandler, failureHandler);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public JwtAuthProcessingFilter jwtAuthProcessingFilter(
            TokenExtractor tokenExtractor,
            AuthenticationFailureHandler failureHandler,
            AuthenticationManager authenticationManager
    ) {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(PERMIT_ENDPOINT_LIST, AUTHENTICATED_ENDPOINT_LIST);
        JwtAuthProcessingFilter filter = new JwtAuthProcessingFilter(matcher, tokenExtractor, failureHandler);

        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    //    @Override
    //    protected void configure(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
    //        auth.userDetailsService(appUserService).passwordEncoder(passwordEncoder);
    //    }

    @Autowired
    void registerProvider(AuthenticationManagerBuilder auth) throws Exception { // mendaftarkan authprovider
        // auth.authenticationProvider(usernamePasswordAuthProvider);
        auth.authenticationProvider(usernamePasswordAuthProvider).authenticationProvider(jwtAuthenticationProvider);
    }

    //    @Override
    //    protected void configure(HttpSecurity http) throws Exception {
    //        http.authorizeRequests().anyRequest().authenticated()
    //                .and()
    //                .csrf().disable()
    //                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //                .and()
    //                .httpBasic();
    //    }

    // mendaftarkan filter usernamePasswordAuthProcessingFilter yant telah dibuat diatas ke sini agar terbaca oleh securityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            UsernamePasswordAuthProcessingFilter usernamePasswordAuthProcessingFilter, // masukkan filter yang telah dibuat ke dependensi securityFilterChain
            JwtAuthProcessingFilter jwtAuthProcessingFilter
    ) throws Exception {
        http.authorizeRequests()
                //.anyRequest()
                //.requestMatchers(new SkipPathRequestMatcher(null, Arrays.asList(V1_URL, V2_URL)))
                .regexMatchers(V1_URL, V2_URL)
                .authenticated()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic();
        // masukkan filter yang telah dibuat ke argumen pertama
        // http.addFilterBefore(usernamePasswordAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(usernamePasswordAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class);
        return  http.build();
    }
}