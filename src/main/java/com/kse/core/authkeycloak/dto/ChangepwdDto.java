package com.kse.core.authkeycloak.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

//import javax.validation.constraints.NotNull;


@Data
public class ChangepwdDto {
	@NotNull(message = "Username obligatoire")
	private  String username;

	@NotNull(message = "Code otp obligatoire")
	private String otp;

	@NotNull(message = "Nouveau mot de passe obligatoire")
	private String newpasswd;

	
}
