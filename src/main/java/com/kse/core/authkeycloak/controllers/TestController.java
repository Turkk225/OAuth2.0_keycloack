package com.kse.core.authkeycloak.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/anonymous")
    public ResponseEntity<String> getAnonymous() {
        return ResponseEntity.ok("Hello Anonymous");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String userName = (String) token.getTokenAttributes().get("name");
        String userEmail = (String) token.getTokenAttributes().get("email");
        return ResponseEntity.ok("Hello Admin \nUser Name : " + userName + "\nUser Email : " + userEmail);
    }

   @GetMapping("/user")
public ResponseEntity<String> getUser(Principal principal) {
    JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
    String userName = (String) token.getTokenAttributes().get("name");
    String userEmail = (String) token.getTokenAttributes().get("email");

    List<String> roles = convertToList(token.getTokenAttributes().get("roles"));
    List<String> groups = convertToList(token.getTokenAttributes().get("groups"));
    List<String> realmAccess = convertToList(token.getTokenAttributes().get("realm_access"));
    List<String> resourceAccess = convertToList(token.getTokenAttributes().get("resource_access"));
    List<String> scope = convertToList(token.getTokenAttributes().get("scope"));
    List<String> user = convertToList(token.getTokenAttributes().get("user"));

    return ResponseEntity.ok(
            "Hello User \nUser Name : " + userName + "\nUser Email : " + userEmail + "\nRoles : " + roles
                    + "\nGroups : " + groups + "\nRealm Access : " + realmAccess + "\nResource Access : "
                    + resourceAccess + "\nScope : " + scope + "\nUser : " + user
    );
}

@SuppressWarnings("unchecked")
private List<String> convertToList(Object attribute) {
    if (attribute instanceof List) {
        return (List<String>) attribute;
    } else if (attribute instanceof Map) {
        return new ArrayList<>(((Map<String, Object>) attribute).keySet());
    } else {
        return new ArrayList<>();
    }
}
}
