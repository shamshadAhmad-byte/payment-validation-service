package com.example.paymentvalidationservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Entity
@Table(
    name = "merchant_payment_requests",
    schema = "validations",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_merchant_txn_reference",
            columnNames = "merchant_txn_reference"
        )
    }
)
@Getter
@Setter
public class MerchantPaymentRequestEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "end_user_id", length = 100)
    private String endUserId;


    @Column(name = "merchant_txn_reference", nullable = false, length = 100)
    private String merchantTxnReference;


    @Column(name = "transaction_request", columnDefinition = "TEXT")
    private String transactionRequest;


    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
