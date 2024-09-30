package com.kse.core.authkeycloak.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ClientDto {

//    @NotNull(message = "Contact obligatoire")
    @Null
    private String clientcontact;

    @NotNull(message = "Mot de passe obligatoire")
    private String clientmotdepasse;

    @NotNull(message = "Nom obligatoire")
    private String clientnom;

    @NotNull(message = "Prenom obligatoire")
    private String clientprenom;

    @NotNull(message = "Email obligatoire")
    private String clientemail;

}
