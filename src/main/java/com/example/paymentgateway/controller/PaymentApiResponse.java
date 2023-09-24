package com.example.paymentgateway.controller;

import java.math.BigDecimal;

public record PaymentApiResponse(String maskedCreditCardNumber, int expiryMonth, int expiryYear,
                                 BigDecimal amount, String currency,
                                 String merchantReferenceId, String paymentResponse, String message){

    public PaymentApiResponse {
        maskedCreditCardNumber = "************" + maskedCreditCardNumber.substring(12);
    }
}
