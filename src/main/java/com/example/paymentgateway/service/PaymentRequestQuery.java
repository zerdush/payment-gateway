package com.example.paymentgateway.service;

import com.example.paymentgateway.domain.PaymentRequest;
import com.example.paymentgateway.repository.PaymentRequestRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentRequestQuery {
    private final PaymentRequestRepo paymentRequestRepo;

    public PaymentRequestQuery(PaymentRequestRepo paymentRequestRepo) {
        this.paymentRequestRepo = paymentRequestRepo;
    }

    public Optional<PaymentRequest> findMerchantReferenceId(String merchantId, String merchantReferenceId) {
        return paymentRequestRepo.findMerchantReferenceId(merchantId, merchantReferenceId);
    }
}
