package com.kse.core.authkeycloak.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

//import javax.validation.constraints.NotNull;

@Data
public class LoginDto {

    @NotNull(message = "Username obligatoire")
    private String username;

    @NotNull(message = "Mot de passe obligatoire")
    private String password;
}
