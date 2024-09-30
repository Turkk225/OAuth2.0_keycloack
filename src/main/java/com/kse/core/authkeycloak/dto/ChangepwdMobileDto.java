package com.kse.core.authkeycloak.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangepwdMobileDto {
	@NotNull(message = "Username obligatoire")
	private  String username;


	@NotNull(message = "Nouveau mot de passe obligatoire")
	private String newpasswd;

	
}
