package com.example.paymentgateway.service;

public interface AcquiringBank {
    AcquiringBankPaymentResult sendPaymentRequest(AcquiringBankPaymentRequest acquiringBankPaymentRequest);
}
