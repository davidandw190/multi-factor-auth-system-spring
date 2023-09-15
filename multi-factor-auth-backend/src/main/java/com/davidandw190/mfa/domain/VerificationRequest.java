package com.davidandw190.mfa.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerificationRequest {
    private String email;
    private String code;

}
