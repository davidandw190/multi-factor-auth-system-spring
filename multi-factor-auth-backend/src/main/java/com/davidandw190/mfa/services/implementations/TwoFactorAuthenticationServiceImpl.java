package com.davidandw190.mfa.services.implementations;

import com.davidandw190.mfa.services.TwoFactorAuthenticationService;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Slf4j
@Service
public class TwoFactorAuthenticationServiceImpl implements TwoFactorAuthenticationService {

    /**
     * Generates a new secret for two-factor authentication.
     *
     * @return A new secret as a string.
     */
    @Override
    public String generateNewSecret() {
        return new DefaultSecretGenerator().generate();
    }

    /**
     * Generates a URI for a QR code image that can be used to set up two-factor authentication.
     *
     * @param secret The secret to be encoded in the QR code.
     * @return A data URI containing the QR code image.
     */
    @Override
    public String generateQrCodeImageUri(String secret) {
        QrData data = new QrData.Builder()
                .label("davidandw190 - MFA Spring")
                .algorithm(HashingAlgorithm.SHA512)
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = new byte[256];

        try {
            imageData = generator.generate(data);
        } catch (QrGenerationException e) {
            log.error("Error generating QR Code");
        }

        return getDataUriForImage(imageData, generator.getImageMimeType());
    }

    /**
     * Validates a one-time password (OTP) code using the provided secret.
     *
     * @param secret The secret associated with the OTP code.
     * @param code   The OTP code to be validated.
     * @return True if the OTP code is valid, false otherwise.
     */
    @Override
    public boolean isOtpValid(String secret, String code) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        return verifier.isValidCode(secret, code);
    }

    /**
     * Checks if an OTP code is not valid by negating the result of {@link #isOtpValid(String, String)}.
     *
     * @param secret The secret associated with the OTP code.
     * @param code   The OTP code to be checked.
     * @return True if the OTP code is not valid, false if it is valid.
     */
    @Override
    public boolean isOtpNotValid (String secret, String code) {
        return !this.isOtpValid(secret, code);
    }


}
