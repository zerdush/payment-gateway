package com.example.paymentgateway.controller;

import com.example.paymentgateway.domain.PaymentRequest;
import com.example.paymentgateway.domain.PaymentStatus;
import com.example.paymentgateway.service.SendPaymentRequestCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.UUID;

@Controller
public class PaymentController {
    private final SendPaymentRequestCommand sendPaymentRequestCommand;

    public PaymentController(SendPaymentRequestCommand sendPaymentRequestCommand) {
        this.sendPaymentRequestCommand = sendPaymentRequestCommand;
    }

    @PostMapping("/payment")
    public ResponseEntity<String> post(@RequestBody PaymentApiRequest paymentApiRequest, Principal principal){
        PaymentRequest paymentRequest = new PaymentRequest.Builder()
                .withId(UUID.randomUUID().toString())
                .withMerchantReferenceId(paymentApiRequest.merchantReferenceId())
                .withMerchantId(principal.getName())
                .withCreditCardNumber(paymentApiRequest.creditCardNumber())
                .withExpiryMonth(paymentApiRequest.expiryMonth())
                .withExpiryYear(paymentApiRequest.expiryYear())
                .withAmount(paymentApiRequest.amount())
                .withCurrency(paymentApiRequest.currency())
                .withCvv(paymentApiRequest.cvv())
                .withStatus(PaymentStatus.REQUESTED)
                .build();

        sendPaymentRequestCommand.sendPaymentRequest(paymentRequest);
        return ResponseEntity.ok("");
    }
}
