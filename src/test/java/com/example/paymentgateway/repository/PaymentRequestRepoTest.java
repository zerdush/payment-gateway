package com.example.paymentgateway.repository;

import com.example.paymentgateway.domain.PaymentRequest;
import com.example.paymentgateway.domain.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = {"ACQUIRING_BANK_URL=url"})
class PaymentRequestRepoTest {
    @Autowired
    private PaymentRequestRepo paymentRequestRepo;

    @Test
    public void testFindByMerchantIdAndMerchantReferenceId() {
        PaymentRequest paymentRequest = createPaymentRequest();
        paymentRequestRepo.save(paymentRequest);

        Optional<PaymentRequest> foundPaymentRequest = paymentRequestRepo
                .findMerchantReferenceId("merchant1", "merchant1_ref_1");

        assertTrue(foundPaymentRequest.isPresent());
        assertEquals("merchant1", foundPaymentRequest.get().getMerchantId());
        assertEquals("merchant1_ref_1", foundPaymentRequest.get().getMerchantReferenceId());
    }

    @Test
    public void testFindByMerchantIdAndMerchantReferenceIdNotFound() {
        // Attempt to retrieve a non-existent PaymentRequest
        Optional<PaymentRequest> foundPaymentRequest = paymentRequestRepo
                .findMerchantReferenceId("merchant2", "reference2");

        assertFalse(foundPaymentRequest.isPresent());
    }

    private static PaymentRequest createPaymentRequest() {
        return new PaymentRequest.Builder()
                .withId("123")
                .withMerchantReferenceId("merchant1_ref_1")
                .withMerchantId("merchant1")
                .withCreditCardNumber("1234567890123456")
                .withExpiryMonth(12)
                .withExpiryYear(2023)
                .withAmount(new BigDecimal("100.00"))
                .withCurrency("USD")
                .withCvv("123")
                .withStatus(PaymentStatus.REQUESTED)
                .build();
    }
}