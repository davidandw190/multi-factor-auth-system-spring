package com.davidandw190.mfa.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.Authentication;

/**
 * Represents a response containing an authentication token.
 */
@Data
@Builder
public class AuthenticationResponse {

    private String token;

    public AuthenticationResponse() {}

    public AuthenticationResponse(String token) {
        this.token = token;
    }


}
