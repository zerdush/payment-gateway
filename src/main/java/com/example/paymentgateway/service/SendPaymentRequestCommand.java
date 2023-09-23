package com.example.paymentgateway.service;

import com.example.paymentgateway.domain.PaymentRequest;
import com.example.paymentgateway.domain.PaymentResponse;
import com.example.paymentgateway.domain.PaymentStatus;
import com.example.paymentgateway.repository.PaymentRequestRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SendPaymentRequestCommand {
    private final AcquiringBank acquiringBank;
    private final PaymentRequestRepo paymentRequestRepo;

    public SendPaymentRequestCommand(AcquiringBank acquiringBank, PaymentRequestRepo paymentRequestRepo) {
        this.acquiringBank = acquiringBank;
        this.paymentRequestRepo = paymentRequestRepo;
    }

    public PaymentResponse sendPaymentRequest(PaymentRequest paymentRequest) {
        Optional<PaymentRequest> existingPaymentRequest = paymentRequestRepo.findMerchantReferenceId(paymentRequest.getMerchantId(),
                paymentRequest.getMerchantReferenceId());

        if(existingPaymentRequest.isPresent()) {
            return PaymentResponse.DUPLICATED;
        }

        var request = AcquiringBankPaymentRequestBuilder.aBuilder()
                .withCardNumber(paymentRequest.getCreditCardNumber())
                .withExpiryMonth(paymentRequest.getExpiryMonth())
                .withExpiryYear(paymentRequest.getExpiryYear())
                .withCvv(paymentRequest.getCvv())
                .withCurrency(paymentRequest.getCurrency())
                .withAmount(paymentRequest.getAmount())
                .build();

        PaymentResponse paymentResponse = PaymentResponse.SUCCESSFUL;
        if(acquiringBank.sendPaymentRequest(request) == AcquiringBankPaymentResult.SUCCESSFUL) {
            paymentRequest.setStatus(PaymentStatus.SUCCESSFUL);
        } else {
            paymentResponse = PaymentResponse.FAILED;
            paymentRequest.setStatus(PaymentStatus.FAILED);
        }
        paymentRequestRepo.save(paymentRequest);

        return paymentResponse;
    }
}
