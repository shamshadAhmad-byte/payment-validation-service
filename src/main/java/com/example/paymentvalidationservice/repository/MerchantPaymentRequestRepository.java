package com.example.paymentvalidationservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.paymentvalidationservice.entity.MerchantPaymentRequestEntity;


public interface MerchantPaymentRequestRepository
        extends JpaRepository<MerchantPaymentRequestEntity, Long> {


    Optional<MerchantPaymentRequestEntity> findByMerchantTxnReference(
        String merchantTxnReference);


    //boolean existsByMerchantTxnReference(String merchantTxnReference);
}
