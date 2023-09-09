package com.davidandw190.mfa.domain;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a user's authentication request containing email and password.
 */
@Data
@Builder
public class AuthenticationRequest {
    @NotNull
    private String email;
    @NotNull
    @JsonProperty(access = WRITE_ONLY)
    private String password;

    public AuthenticationRequest() {}

    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

