package com.example.paymentgateway.controller;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentApiResponseTest {

    @Test
    void whenConstructMaskTheCreditCardNumber() {
        var paymentApiResponse = new PaymentApiResponse("1234567890123456", 12, 2023, BigDecimal.TEN, "USD" ,
                "456", "SUCCESSFUL", "some message");

        assertEquals("************3456", paymentApiResponse.maskedCreditCardNumber());
    }
}