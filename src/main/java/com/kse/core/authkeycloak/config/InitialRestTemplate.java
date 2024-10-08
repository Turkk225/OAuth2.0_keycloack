package com.kse.core.authkeycloak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class InitialRestTemplate {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
