package com.kse.core.authkeycloak.dto;

public record UserRegistrationRecord(String username, String email, String firstName, String lastName, String password) {
}
