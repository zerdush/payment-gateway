package com.example.paymentgateway.controller;

import java.math.BigDecimal;

public record PaymentApiRequest(String merchantReferenceId, int expiryMonth, int expiryYear,
                                String creditCardNumber, String cvv, String currency, BigDecimal amount){
}
