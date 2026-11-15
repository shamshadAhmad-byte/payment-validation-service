package com.example.paymentvalidationservice.service.implementation;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.paymentvalidationservice.service.interfaces.HmacSHA256;

@Service
public class HmacSha256Service implements HmacSHA256 {

    private static final Logger log = LoggerFactory.getLogger(HmacSha256Service.class);

    @Override
    public String generateHmac(String data, String secret) {
        log.info("Generating HMAC-SHA256 signature");
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(
                    data.getBytes(StandardCharsets.UTF_8)
            );
            return Base64.getEncoder().encodeToString(hash);
        } catch (GeneralSecurityException e) {
            log.error("Error generating HMAC-SHA256 signature", e);
            throw new IllegalStateException("Unable to generate HMAC-SHA256 signature", e);
        }

    }

    @Override
    public boolean isVerified(String generatedSignature, String receivedSignature) {
        log.info("Verifying HMAC-SHA256 signature");
        boolean verified = generatedSignature != null && receivedSignature != null
                && generatedSignature.equals(receivedSignature);
        if (!verified) {
            log.warn("Invalid signature");
            return false;
        }
        log.info("Signature verified successfully");
        return true;
    }
}
