package com.kse.core.authkeycloak.tools;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.kse.core.authkeycloak.dto.LoginRequestDto;
import com.kse.core.authkeycloak.dto.TokenDto;
import com.kse.core.authkeycloak.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class KeycloakUtil {

   public TokenDto getAccessToken(LoginRequestDto requestLogin, RestTemplate restTemplate,
                               String GRANT_TYPE_PASSWORD,
                               String kcClientId, String kcClientSecret, String kcGetTokenUrl) {
    // Vérifier que les champs username et password ne sont pas null ou vides
    if (requestLogin.getUsername() == null || requestLogin.getUsername().isEmpty()) {
        log.error("Le champ username est null ou vide");
        throw new IllegalArgumentException("Le champ username ne peut pas être null ou vide");
    }
    if (requestLogin.getPassword() == null || requestLogin.getPassword().isEmpty()) {
        log.error("Le champ password est null ou vide");
        throw new IllegalArgumentException("Le champ password ne peut pas être null ou vide");
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    // Prépare le corps de la requête avec les informations de connexion et les détails du client
    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("grant_type", GRANT_TYPE_PASSWORD);
    requestBody.add("client_id", kcClientId);
    requestBody.add("client_secret", kcClientSecret);
    requestBody.add("username", requestLogin.getUsername());
    requestBody.add("password", requestLogin.getPassword());
    requestBody.add("scope", "openid");

    log.info("Request body: {}", requestBody);
    // Envoie la requête à Keycloak et obtient la réponse
       ResponseEntity<TokenDto> response = null;
       try {
           response = restTemplate.postForEntity(kcGetTokenUrl,
                   new HttpEntity<>(requestBody, headers), TokenDto.class);
       } catch (RestClientException e) {
           throw new UnauthorizedException("votre nom d'utilisateur ou mot de passe est incorrect");
       }

       // Retourne les détails du token
    return response.getBody();
}

    public TokenDto getRefreshToken(String refreshToken, RestTemplate restTemplate,
                                    String GRANT_TYPE_REFRESH_TOKEN,
                                    String kcClientId, String kcClientSecret, String kcGetTokenUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", GRANT_TYPE_REFRESH_TOKEN);
        requestBody.add("refresh_token", refreshToken);
        requestBody.add("client_id", kcClientId);
        requestBody.add("client_secret", kcClientSecret);

        ResponseEntity<TokenDto> response = restTemplate.postForEntity(kcGetTokenUrl,
                new HttpEntity<>(requestBody, headers), TokenDto.class);

        return response.getBody();
    }

    @SuppressWarnings("null")
    public String getAdminAccessToken(String GRANT_TYPE_PASSWORD, RestTemplate restTemplate, String kcAdminUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", GRANT_TYPE_PASSWORD);
        requestBody.add("client_id", "admin-cli");
        // requestBody.add("client_secret", kcClientSecret);
        requestBody.add("username", "admin");
        requestBody.add("password", "admin");
        requestBody.add("scope", "openid");

        ResponseEntity<TokenDto> response = restTemplate.postForEntity(
                kcAdminUrl,
                new HttpEntity<>(requestBody, headers), TokenDto.class);
        String adminAccessToken = response.getBody().getAccess_token();
        return adminAccessToken;
    }

    public String getUserSub(String username, HttpHeaders headers, RestTemplate restTemplate, String kcUpdateUrl) {
        ResponseEntity<Object> response = restTemplate.exchange(
                kcUpdateUrl,
                HttpMethod.GET,
                new HttpEntity<>(
                        headers),
                Object.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> users = mapper.convertValue(response.getBody(),
                    new TypeReference<List<Map<String, Object>>>() {
                    });

            Optional<Map<String, Object>> foundUser = users.stream()
                    .filter(user -> username.equals(user.get("username")))
                    .findFirst();

            if (foundUser.isPresent()) {
                return foundUser.get().get("id").toString();
            }
        }
        return null;
    }


}
