package com.davidandw190.mfa.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;


/**
 * Represents a response containing an access token and a refresh token.
 */
@Data
@Builder
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @JsonProperty(value = "mfa_enabled")
    private boolean mfaEnabled;

}
