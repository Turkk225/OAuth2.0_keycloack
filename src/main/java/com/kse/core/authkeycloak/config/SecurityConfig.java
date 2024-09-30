package com.kse.core.authkeycloak.config;

import com.kse.core.authkeycloak.exceptions.AuthExceptionHandler;
import com.kse.core.authkeycloak.exceptions.AccessDeniedExceptionHandler;
import com.kse.core.authkeycloak.tools.KeycloakJwtRolesConverterUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${keycloak.client-id}")
    private String kcClientId;

    @Value("${keycloak.token-issuer-url}")
    private String tokenIssuerUrl;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthExceptionHandler entryPoint,
            AccessDeniedExceptionHandler accessDenied) throws Exception {

        DelegatingJwtGrantedAuthoritiesConverter authoritiesConverter = new DelegatingJwtGrantedAuthoritiesConverter(
                new JwtGrantedAuthoritiesConverter(),
                new KeycloakJwtRolesConverterUtil());

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("auth/mobile/login").permitAll()
                        .requestMatchers("/test/admin").hasRole("ADMIN")
                        .requestMatchers("/test/user").hasRole("USER")
                        .requestMatchers("/v3/api-docs/**", "/configuration/**", "/swagger-ui/**", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(accessDenied))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter(
                                        authoritiesConverter))));

        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(tokenIssuerUrl);
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter(
            DelegatingJwtGrantedAuthoritiesConverter authoritiesConverter) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    @Bean
    CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
