package com.example.paymentvalidationservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.GeneralSecurityException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import com.example.paymentvalidationservice.dto.PaymentRequest;
import com.example.paymentvalidationservice.service.implementation.HmacSha256Service;

@SpringBootTest(properties = {
		"spring.security.user.name=test",
		"spring.security.user.password=test"
})
@AutoConfigureMockMvc
class PaymentValidationServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private String validSignature;

	private static final String AUTHORIZATION = "Basic dGVzdDp0ZXN0";

	@BeforeEach
	void setUp() throws GeneralSecurityException {
		PaymentRequest request = new PaymentRequest();
		validSignature = new HmacSha256Service().generateHmac(request.toString(), "your_secret_key");
	}

	@Test
	void getPaymentStatusReturnsRunning() throws Exception {
		mockMvc.perform(get("/payments").header(HttpHeaders.AUTHORIZATION, AUTHORIZATION))
				.andExpect(status().isOk())
				.andExpect(content().string("Payment service is running"));
	}

	@Test
	void createPaymentAcceptsValidSignature() throws Exception {
		mockMvc.perform(post("/payments")
						.contentType("application/json")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION)
						.header("X-Payment-Signature", validSignature)
						.content("{}"))
				.andExpect(status().isOk())
				.andExpect(content().string("Payment processed successfully"));
	}

	@Test
	void createPaymentRejectsInvalidSignature() throws Exception {
		mockMvc.perform(post("/payments")
						.contentType("application/json")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION)
						.header("X-Payment-Signature", "invalid")
						.content("{}"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void createPaymentRequiresSignature() throws Exception {
		mockMvc.perform(post("/payments")
						.contentType("application/json")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION)
						.content("{}"))
				.andExpect(status().isBadRequest());
	}

}
