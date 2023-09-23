package com.example.paymentgateway.service;

import java.math.BigDecimal;

public record AcquiringBankPaymentRequest(String cardNumber, int expiryMonth, int expiryYear, String cvv,
                                          String currency, BigDecimal amount) {

}
