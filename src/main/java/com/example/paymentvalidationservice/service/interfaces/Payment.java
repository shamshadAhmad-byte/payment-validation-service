package com.example.paymentvalidationservice.service.interfaces;

import com.example.paymentvalidationservice.dto.PaymentRequest;

public interface Payment {

	String createPayment(PaymentRequest paymentRequest);
}
