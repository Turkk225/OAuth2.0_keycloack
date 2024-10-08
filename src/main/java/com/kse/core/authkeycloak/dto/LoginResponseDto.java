package com.kse.core.authkeycloak.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private String status;
    private String message;
    private String accessToken;
    private String refreshToken;
}