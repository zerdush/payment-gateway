package com.example.paymentgateway.service;

import org.springframework.stereotype.Component;

@Component
public class AcquiringBankImpl implements AcquiringBank {
    @Override
    public AcquiringBankPaymentResult sendPaymentRequest(AcquiringBankPaymentRequest acquiringBankPaymentRequest) {
        return AcquiringBankPaymentResult.SUCCESSFUL;
    }
}
