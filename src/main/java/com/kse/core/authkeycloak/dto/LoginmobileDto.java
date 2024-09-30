package com.kse.core.authkeycloak.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

//import javax.validation.constraints.NotNull;

@Data
public class LoginmobileDto {

    @NotNull(message = "Username obligatoire")
    private String username;

    @NotNull(message = "Mot de passe obligatoire")
    private String password;

    @NotNull(message = "keycloakUserId obligatoire")
    private String keycloakUserId;
}
