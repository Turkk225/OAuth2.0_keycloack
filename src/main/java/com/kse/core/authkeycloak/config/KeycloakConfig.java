package com.kse.core.authkeycloak.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${app.keycloak.admin.clientId}")
    private String clientId;
    @Value("${app.keycloak.admin.clientSecret}")
    private String clientSecret;
    @Value("${app.keycloak.realm}")
    private String realm;
    @Value("${app.keycloak.serverUrl}")
    private String serverUrl;

    @Value("${keycloak.client-id}")
    private String kcClientId;

    private String realm2 = "keycloak-trailer";

    @Value("${keycloak.client-secret}")
    private String kcClientSecret;

    @Value("${keycloak.token-url}")
    private String kcGetTokenUrl;

    @Value("${keycloak.logout-url}")
    private String kcLogoutUrl;

    @Value("${keycloak.revoke-token-url}")
    private String kcRevokeTokenUrl;

    @Value("${keycloak.admin-token-url}")
    private String kcAdminUrl;

    @Value("${keycloak.update-url}")
    private String kcUpdateUrl;


    @Bean
    public Keycloak keycloak(){

        return KeycloakBuilder.builder()
                .clientSecret(clientSecret)
                .clientId(clientId)
                .grantType("client_credentials")
                .realm(realm)
                .serverUrl(serverUrl)
                .build();
    }
//    @Bean
//    public Keycloak keycloak(){
//        return KeycloakBuilder.builder()
//                .serverUrl(serverUrl)
//                .realm(realm2)
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .build();
//    }

}
