package com.kse.core.authkeycloak.dto;

public record NewUserRecord(String username, String password, String firstName, String lastName, String email) {
}
