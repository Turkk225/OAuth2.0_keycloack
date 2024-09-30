package com.kse.core.authkeycloak.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}