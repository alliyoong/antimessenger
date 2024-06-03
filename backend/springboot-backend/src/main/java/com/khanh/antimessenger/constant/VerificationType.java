package com.khanh.antimessenger.constant;

public enum VerificationType {
    ACCOUNT("ACCOUNT"),
    RESET_PASSWORD_BY_USER("RESET_PASSWORD_BY_USER"),
    RESET_PASSWORD_BY_ADMIN("RESET_PASSWORD_BY_ADMIN");

    private final String type;

    VerificationType(String type) {this.type = type;}

    public String getType() {
        return this.type.toLowerCase();
    }
}
