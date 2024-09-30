package com.kse.core.authkeycloak.tools;//package org.kyp.demotext.tools;
//
//import lombok.extern.slf4j.Slf4j;
//import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
//import org.keycloak.OAuth2Constants;
//import org.keycloak.admin.client.CreatedResponseUtil;
//import org.keycloak.admin.client.Keycloak;
//import org.keycloak.admin.client.KeycloakBuilder;
//import org.keycloak.admin.client.resource.RealmResource;
//import org.keycloak.admin.client.resource.RolesResource;
//import org.keycloak.admin.client.resource.UserResource;
//import org.keycloak.admin.client.resource.UsersResource;
//import org.keycloak.authorization.client.AuthzClient;
//import org.keycloak.authorization.client.Configuration;
//import org.keycloak.representations.AccessTokenResponse;
//import org.keycloak.representations.idm.CredentialRepresentation;
//import org.keycloak.representations.idm.UserRepresentation;
//import org.kyp.demotext.dto.UserDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.env.Environment;
//import org.springframework.http.*;
//import jakarta.ws.rs.core.Response;
//import org.springframework.stereotype.Component;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Slf4j
////@AllArgsConstructor
////@RequiredArgsConstructor
////@NoArgsConstructor
//public class KeycloakAdminTools {
//    @Autowired
//    Environment env;
//
//
//    @Value("${auth.server.url}")
//    private String authServerUrl;
//
//    @Value("${app.realm}")
//    private String realm;
//
//    @Value("${shipp.clientid}")
//    private String clientId;
//
//    @Value("${shipp.clientsecret}")
//    private String clientSecret;
//
//
//
//    @Value("${refresh-token.url}")
//    private String refreshTokenUrl;
//
//
//    RestTemplate restTemplate;
//
//    public String addUser(UserDto userDto){
//
//        try{
//            log.info("Ajout user keycloack : userinfos= {} ",userDto.toString());
//            String userId="";
//            Keycloak keycloak = getInstance();
//
//            keycloak.tokenManager().getAccessToken();
//            //keycloak.tokenManager().getAccessToken();
//
//            UserRepresentation user = buildUserRepresentation(userDto);
//            user.setEnabled(true);
//
//            // Get realm
//            RealmResource realmResource = keycloak.realm(realm);
//
//            UsersResource usersRessource = realmResource.users();
//
//            RolesResource roleRessource =  realmResource.roles();
//
//            Response response = usersRessource.create(user);
//
//            userDto.setStatusCode(response.getStatus());
//            userDto.setStatus(response.getStatusInfo().toString());
//
//            if (response.getStatus() == 201) {
//                userId = CreatedResponseUtil.getCreatedId(response);
//                log.info("Created userId {}", userId);
//
//                UserResource userResource = usersRessource.get(userId);
//                // create password credential
//                CredentialRepresentation passwordCred = this.createPasswordCredentials(userDto.getPassword());
//                // Set password credential
//                userResource.resetPassword(passwordCred);
//
//                //GroupRepresentation realmGroupUser= realmResource.groups().group("Admin").toRepresentation();
//
//            }
//            return userId;
//        }catch (Exception ex){
//            log.error("Erreur ajout userKeycloack : userinfos= {}; error={} ",userDto.toString(), ex.getMessage());
//            return null;
//        }
//
//
//    }
//
//    public void updateUser(String userId, UserDto userDto){
//        UserRepresentation user = buildUserRepresentation(userDto);
//        RealmResource realmResource = getInstance().realm(realm);
//        UsersResource usersRessource = realmResource.users();
//        usersRessource.get(userId).update(user);
//    }
//
//
//    public void resetPassword(String userId, String password){
//        try{
//            RealmResource realmResource = getInstance().realm(realm);
//            UsersResource usersRessource = realmResource.users();
//            UserResource userResource = usersRessource.get(userId);
//            CredentialRepresentation passwordCred = this.createPasswordCredentials(password);
//            // Set password credential
//            userResource.resetPassword(passwordCred);
//        }catch (Exception ex){
//            log.error("Error Keycloak reset password function : keycloakUserid={} | error={}", userId, ex.getMessage());
//        }
//    }
//
//
//
//
//    public void deleteUser(String userId){
//        RealmResource realmResource = getInstance().realm(realm);
//        UsersResource usersRessource = realmResource.users();
//        usersRessource.get(userId).remove();
//    }
//
//    public CredentialRepresentation createPasswordCredentials(String password) {
//        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
//        passwordCredentials.setTemporary(false);
//        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
//        passwordCredentials.setValue(password);
//        return passwordCredentials;
//    }
//
//    public  Keycloak getInstance(){
//        return KeycloakBuilder.builder().serverUrl(authServerUrl)
//                .grantType(OAuth2Constants.PASSWORD).realm("master").clientId("admin-cli")
//                .username("admin").password("admin")
//                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build()).build();
//    }
//
//
//
//    public UserRepresentation buildUserRepresentation(UserDto userDto){
//        UserRepresentation user = new UserRepresentation();
//        if(userDto.getUsername() != null){
//            user.setUsername(userDto.getUsername());
//        }
//        if(userDto.getFirstname() != null){
//            user.setFirstName(userDto.getFirstname());
//        }
//
//        if(userDto.getLastname() != null){
//            user.setLastName(userDto.getLastname());
//        }
//
//        if(userDto.getEmail() != null ){
//            user.setEmail(userDto.getEmail());
//        }
//        return user;
//    }
//
//    public AccessTokenResponse getTokenObject(String username, String password){
//        try{
//            log.info("Récupération token : username={}; password={}", username, password);
//            Map<String, Object> clientCredentials = new HashMap<>();
//            clientCredentials.put("secret", clientSecret);
//            clientCredentials.put("grant_type", "password");
//            log.debug("secret : "+clientSecret);
//            log.debug("authServerUrl : "+authServerUrl);
//            log.debug("realm : "+realm);
//            log.debug("clientId : "+clientId);
//            log.debug("==========Step 1===========");
//
//            Configuration configuration =
//                    new Configuration(authServerUrl, realm, clientId, clientCredentials, null);
//            log.debug("==========Step 2===========");
//
//            AuthzClient authzClient = AuthzClient.create(configuration);
//            log.debug("==========Step 3===========");
//
//            AccessTokenResponse response =
//                    authzClient.obtainAccessToken(username, password);
//            log.debug("==========Step 4===========");
//            log.debug("response : "+response);
//
//            return response;
//
//        }catch (Exception ex){
//            log.error("Erreur Récupération token: username={}; error={} ",username,ex.getMessage());
//            return null;
//        }
//
//    }
//
//
//    //Cette fonction permet de générer un nouveau token à partir du refresh token
//    public AccessTokenResponse getrefreshTokenObject(String refreshToken){
//        try{
//            log.info("Récupération token: refreshToken={};", refreshToken);
//            HttpHeaders headers = new HttpHeaders();
//
//            MultiValueMap<String, String> body = new LinkedMultiValueMap<String,String>();
//
//            body.put("client_secret", Collections.singletonList(clientSecret));
//            body.put("grant_type", Collections.singletonList("refresh_token"));
//            body.put("client_id", Collections.singletonList(clientId));
//            body.put("refresh_token", Collections.singletonList(refreshToken));
//
//            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//            HttpEntity<?> entity1 = new HttpEntity<>(body,headers);
//            ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(refreshTokenUrl, HttpMethod.POST, entity1, AccessTokenResponse.class);
//            AccessTokenResponse responseBody = response.getBody();
//            //log.debug("Liste des stocks "+stocks);
//            log.info("Récupération token terminée: refreshToken={} ", refreshToken);
//            return responseBody;
//
//        }catch (Exception ex){
//            log.error("Erreur Récupération token: refreshToken={}; error={} ",refreshToken,ex.getMessage());
//            return null;
//        }
//
//    }
//
//}
