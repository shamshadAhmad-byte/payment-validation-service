package com.example.paymentvalidationservice.service.interfaces;

import java.security.GeneralSecurityException;

public interface HmacSHA256 {

	String generateHmac(String data, String secret) throws GeneralSecurityException;
	boolean isVerified(String generatedSignature, String receivedSignature);
}
