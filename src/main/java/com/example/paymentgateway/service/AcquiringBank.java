package com.example.paymentgateway.service;

public interface AcquiringBank {
    AcquiringBankResponse sendPaymentRequest(AcquiringBankPaymentRequest acquiringBankPaymentRequest);
}
