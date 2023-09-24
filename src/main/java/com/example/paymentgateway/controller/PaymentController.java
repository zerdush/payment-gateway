package com.example.paymentgateway.controller;

import com.example.paymentgateway.domain.PaymentRequest;
import com.example.paymentgateway.domain.PaymentResponse;
import com.example.paymentgateway.domain.PaymentStatus;
import com.example.paymentgateway.service.PaymentRequestQuery;
import com.example.paymentgateway.service.SendPaymentRequestCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.UUID;

@Controller
public class PaymentController {
    private final SendPaymentRequestCommand sendPaymentRequestCommand;
    private final PaymentRequestQuery paymentRequestQuery;

    public PaymentController(SendPaymentRequestCommand sendPaymentRequestCommand,
                             PaymentRequestQuery paymentRequestQuery) {
        this.sendPaymentRequestCommand = sendPaymentRequestCommand;
        this.paymentRequestQuery = paymentRequestQuery;
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

        PaymentResponse paymentResponse = sendPaymentRequestCommand.sendPaymentRequest(paymentRequest);

        if (paymentResponse == PaymentResponse.DUPLICATED) {
            return ResponseEntity.status(409).body("Duplicated payment request id: %s"
                    .formatted(paymentRequest.getMerchantReferenceId()));
        }
        else if (paymentResponse == PaymentResponse.FAILED) {
            return ResponseEntity.status(422).body("Failed to process payment request id: %s"
                    .formatted(paymentRequest.getMerchantReferenceId()));
        }

        return ResponseEntity.ok("");
    }

    @GetMapping("/payment/{merchantReferenceId}")
    public ResponseEntity<PaymentApiResponse> get(@PathVariable String merchantReferenceId, Principal principal) {
        String merchantId = principal.getName();
        return  paymentRequestQuery.findMerchantReferenceId(merchantId, merchantReferenceId)
                .map(PaymentController::createResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private static PaymentApiResponse createResponse(PaymentRequest paymentRequest){
        return new PaymentApiResponse(paymentRequest.getCreditCardNumber(),
                paymentRequest.getExpiryMonth(),
                paymentRequest.getExpiryYear(),
                paymentRequest.getAmount(),
                paymentRequest.getCurrency(),
                paymentRequest.getMerchantReferenceId(),
                paymentRequest.getStatus().name(),
                paymentRequest.getMessage());
    }
}
