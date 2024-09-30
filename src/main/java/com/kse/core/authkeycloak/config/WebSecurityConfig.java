//package com.kse.core.authkeycloak.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    private final JwtAuthConverter jwtAuthConverter;
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring()
//                .requestMatchers(HttpMethod.POST, "api/client/signup", "api/client/test","api/client/login","api/client/loginV2")
//                .requestMatchers(HttpMethod.GET, "api/client/**", "api/client/roles/{id}","api/client/{id}/roles")
//                .requestMatchers(HttpMethod.DELETE, "/public/**", "/users/{id}")
//                .requestMatchers(HttpMethod.PUT, "/public/**", "/users/{id}/send-verification-email", "/users/forgot-password")
//                .requestMatchers(HttpMethod.OPTIONS, "/**")
//                .requestMatchers("/v3/api-docs/**", "/configuration/**", "/swagger-ui/**", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/api-docs/**");
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
//                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .build();
//    }
//}