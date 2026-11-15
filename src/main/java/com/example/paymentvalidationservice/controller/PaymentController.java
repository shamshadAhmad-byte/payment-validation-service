package com.example.paymentvalidationservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.paymentvalidationservice.dto.PaymentRequest;
import com.example.paymentvalidationservice.service.interfaces.Payment;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	private final Payment payment;

	public PaymentController(Payment payment) {
		this.payment = payment;
	}

	@PostMapping
	public String createPayment(
			@RequestBody PaymentRequest paymentRequest,
			Authentication authentication) {
		return payment.createPayment(paymentRequest, authentication.getName());
	}

	@GetMapping
	public String getPaymentStatus() {
		return "Payment service is running";
	}
}
