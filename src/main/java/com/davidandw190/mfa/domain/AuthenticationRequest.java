package com.davidandw190.mfa.domain;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a user's authentication request containing email and password.
 */
@Data
@Builder
public class AuthenticationRequest {
    private String email;
    private String password;

    public AuthenticationRequest() {}

    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

