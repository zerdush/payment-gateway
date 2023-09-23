package com.example.paymentgateway.repository;

import com.example.paymentgateway.domain.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRequestRepo extends JpaRepository<PaymentRequest, String> {
    @Query("SELECT pr FROM PaymentRequest pr WHERE pr.merchantId = :merchantId AND pr.merchantReferenceId = :merchantReferenceId")
    Optional<PaymentRequest> findMerchantReferenceId(String merchantId, String merchantReferenceId);
}
