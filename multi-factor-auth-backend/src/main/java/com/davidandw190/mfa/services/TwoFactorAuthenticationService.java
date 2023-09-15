package com.davidandw190.mfa.services;

public interface TwoFactorAuthenticationService {
    String generateNewSecret();

    String generateQrCodeImageUri(String secret);

    boolean isOtpValid(String secret, String code);

    boolean isOtpNotValid(String secret, String code);
}
