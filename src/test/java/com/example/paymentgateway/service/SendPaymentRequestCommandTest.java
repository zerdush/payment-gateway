package com.example.paymentgateway.service;

import com.example.paymentgateway.domain.PaymentRequest;
import com.example.paymentgateway.domain.PaymentResponse;
import com.example.paymentgateway.domain.PaymentStatus;
import com.example.paymentgateway.repository.PaymentRequestRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendPaymentRequestCommandTest {

    @Mock
    AcquiringBank acquiringBank;
    @Mock
    PaymentRequestRepo paymentRequestRepo;

    private PaymentRequest paymentRequest;
    private SendPaymentRequestCommand sendPaymentRequestCommand;

    @BeforeEach
    void setUp() {
        paymentRequest = createPaymentRequest();

        sendPaymentRequestCommand = new SendPaymentRequestCommand(acquiringBank, paymentRequestRepo);

    }

    @Test
    void whenSendRequestThenSendItToAcquiringBank() {
        when(acquiringBank.sendPaymentRequest(any())).thenReturn(createSuccessResponse());
        sendPaymentRequestCommand.sendPaymentRequest(paymentRequest);

        var acquiringBankPaymentRequest = new AcquiringBankPaymentRequest("1234567890123456", 12, 2023, "123",
                "USD", new BigDecimal("100.00"));

        verify(acquiringBank).sendPaymentRequest(acquiringBankPaymentRequest);
    }

    @Test
    public void givenAcquiringBankResultIsSuccessThenSavePaymentRequestWithSuccess() {
        when(acquiringBank.sendPaymentRequest(any())).thenReturn(createSuccessResponse());

        sendPaymentRequestCommand.sendPaymentRequest(paymentRequest);

        var expectedPaymentRequest = createPaymentRequest();
        expectedPaymentRequest.setStatus(PaymentStatus.SUCCESSFUL);
        expectedPaymentRequest.setMessage("some message");
        verify(paymentRequestRepo).save(expectedPaymentRequest);
    }

    @Test
    public void givenAcquiringBankResultIsSuccessThenReturnSuccess() {
        when(acquiringBank.sendPaymentRequest(any())).thenReturn(createSuccessResponse());

        PaymentResponse paymentResponse = sendPaymentRequestCommand.sendPaymentRequest(paymentRequest);

        assertEquals(PaymentResponse.SUCCESSFUL, paymentResponse);
    }

    @Test
    public void givenAcquiringBankResultIsFailThenReturnFailed() {
        when(acquiringBank.sendPaymentRequest(any())).thenReturn(createFailedResponse());

        PaymentResponse paymentResponse = sendPaymentRequestCommand.sendPaymentRequest(paymentRequest);

        assertEquals(PaymentResponse.FAILED, paymentResponse);
    }

    @Test
    public void givenAcquiringBankResultIsFailThenSavePaymentRequestWithFailed() {
        when(acquiringBank.sendPaymentRequest(any())).thenReturn(createFailedResponse());

        sendPaymentRequestCommand.sendPaymentRequest(paymentRequest);

        var expectedPaymentRequest = createPaymentRequest();
        expectedPaymentRequest.setStatus(PaymentStatus.FAILED);
        expectedPaymentRequest.setMessage("some message");
        verify(paymentRequestRepo).save(eq(expectedPaymentRequest));
    }

    @Test
    public void givenPaymentRequestSentBeforeWhenSendPaymentRequestThenDoNotSendItToAcquiringBank() {
        var existingPaymentRequest = createPaymentRequest();
        existingPaymentRequest.setStatus(PaymentStatus.SUCCESSFUL);
        when(paymentRequestRepo.findMerchantReferenceId(anyString(), anyString()))
                .thenReturn(Optional.of(existingPaymentRequest));

        sendPaymentRequestCommand.sendPaymentRequest(paymentRequest);

        verify(acquiringBank, never()).sendPaymentRequest(any());
    }

    @Test
    public void givenPaymentRequestSentBeforeWhenSendPaymentRequestThenReturnDuplicated() {
        var existingPaymentRequest = createPaymentRequest();
        existingPaymentRequest.setStatus(PaymentStatus.SUCCESSFUL);
        when(paymentRequestRepo.findMerchantReferenceId(anyString(), anyString()))
                .thenReturn(Optional.of(existingPaymentRequest));

        PaymentResponse paymentResponse = sendPaymentRequestCommand.sendPaymentRequest(paymentRequest);

        assertEquals(PaymentResponse.DUPLICATED, paymentResponse);
    }

    private static AcquiringBankResponse createSuccessResponse() {
        return new AcquiringBankResponse(AcquiringBankPaymentResult.SUCCESSFUL, "some message");
    }

    private static AcquiringBankResponse createFailedResponse() {
        return new AcquiringBankResponse(AcquiringBankPaymentResult.FAILED, "some message");
    }

    private static PaymentRequest createPaymentRequest() {
        return new PaymentRequest.Builder()
                .withId("123")
                .withMerchantReferenceId("456")
                .withMerchantId("789")
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