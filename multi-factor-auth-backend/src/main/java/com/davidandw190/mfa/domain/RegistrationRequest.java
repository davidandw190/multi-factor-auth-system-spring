package com.davidandw190.mfa.domain;

import com.davidandw190.mfa.enums.Role;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a new user`s registration request.
 */
@Data
@Builder
public class RegistrationRequest {
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @NotNull
    private String email;
    @NotNull
    @JsonProperty(access = WRITE_ONLY)
    private String password;
    @NotNull
    private Role role;
    @JsonProperty(value = "mfa_enabled")
    private boolean mfaEnabled;
}
