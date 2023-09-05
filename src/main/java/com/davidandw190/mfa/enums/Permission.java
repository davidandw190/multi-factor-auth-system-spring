package com.davidandw190.mfa.enums;


public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),

    MANAGEMENT_READ("management:read"),
    MANAGEMENT_CREATE("management:create"),
    MANAGEMENT_UPDATE("management:update"),
    MANAGEMENT_DELETE("management:delete");

    private final String value;

    Permission(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
