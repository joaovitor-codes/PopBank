package com.dev.popbank.model.auth;

public enum ContasRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;

    ContasRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
