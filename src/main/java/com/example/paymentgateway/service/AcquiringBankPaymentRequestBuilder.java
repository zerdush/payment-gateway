package com.example.paymentgateway.service;

import java.math.BigDecimal;

public final class AcquiringBankPaymentRequestBuilder {
    private String cardNumber;
    private int expiryMonth;
    private int expiryYear;
    private String cvv;
    private String currency;
    private BigDecimal amount;

    private AcquiringBankPaymentRequestBuilder() {
    }

    public static AcquiringBankPaymentRequestBuilder aBuilder() {
        return new AcquiringBankPaymentRequestBuilder();
    }

    public AcquiringBankPaymentRequestBuilder withCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public AcquiringBankPaymentRequestBuilder withExpiryMonth(int expiryMonth) {
        this.expiryMonth = expiryMonth;
        return this;
    }

    public AcquiringBankPaymentRequestBuilder withExpiryYear(int expiryYear) {
        this.expiryYear = expiryYear;
        return this;
    }

    public AcquiringBankPaymentRequestBuilder withCvv(String cvv) {
        this.cvv = cvv;
        return this;
    }

    public AcquiringBankPaymentRequestBuilder withCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public AcquiringBankPaymentRequestBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public AcquiringBankPaymentRequest build() {
        return new AcquiringBankPaymentRequest(cardNumber, expiryMonth, expiryYear, cvv, currency, amount);
    }
}
