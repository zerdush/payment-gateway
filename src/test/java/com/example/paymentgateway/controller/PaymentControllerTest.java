package com.example.paymentgateway.controller;

import com.example.paymentgateway.domain.PaymentRequest;
import com.example.paymentgateway.domain.PaymentResponse;
import com.example.paymentgateway.domain.PaymentStatus;
import com.example.paymentgateway.service.PaymentRequestQuery;
import com.example.paymentgateway.service.SendPaymentRequestCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SendPaymentRequestCommand sendPaymentRequestCommand;

    @MockBean
    PaymentRequestQuery paymentRequestQuery;
    private MockHttpServletRequestBuilder mockPost;

    @BeforeEach
    void setUp() {
        String content = """
                {
                    "merchantReferenceId": "merchant1_ref_1",
                    "creditCardNumber": "1234567890123456",
                    "expiryMonth": 12,
                    "expiryYear": 2023,
                    "amount": 100.00,
                    "currency": "USD",
                    "cvv": "123"
                }
                """;

        mockPost = MockMvcRequestBuilders
                .post("/payment")
                .content(content)
                .contentType("application/json")
                .principal(() -> "merchant1");
    }

    @Test
    void whenPostThenReturnOk() throws Exception {
        when(sendPaymentRequestCommand.sendPaymentRequest(any())).thenReturn(PaymentResponse.SUCCESSFUL);

        mockMvc.perform(mockPost)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void givenPaymentRequestSentBeforeWhenPostThenReturnDuplicated() throws Exception {
        when(sendPaymentRequestCommand.sendPaymentRequest(any())).thenReturn(PaymentResponse.DUPLICATED);

        mockMvc.perform(mockPost)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(content().string("Duplicated payment request id: merchant1_ref_1"));
    }

    @Test
    void givenPaymentRequestFailedWhenPostThenReturnUnprocessableEntity() throws Exception {
        when(sendPaymentRequestCommand.sendPaymentRequest(any())).thenReturn(PaymentResponse.FAILED);

        mockMvc.perform(mockPost)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(content()
                        .string("Failed to process payment request id: merchant1_ref_1"));
    }

    @Test
    void whenGetThenReturnOk() throws Exception {
        when(paymentRequestQuery.findMerchantReferenceId("merchant1", "merchant1_ref_1"))
                .thenReturn(Optional.of(createPaymentRequest()));
        String expectedResponse = "{\"maskedCreditCardNumber\":\"************3456\",\"expiryMonth\":12,\"expiryYear\":2023,\"amount\":100.00,\"currency\":\"USD\",\"merchantReferenceId\":\"merchant1_ref_1\",\"paymentResponse\":\"REQUESTED\",\"message\":\"some message\"}";

        mockMvc.perform(MockMvcRequestBuilders.get("/payment/merchant1_ref_1").principal(() -> "merchant1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(expectedResponse))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void givenPaymentRequestNotExistsWhenGetThenReturnNotFound() throws Exception {
        when(paymentRequestQuery.findMerchantReferenceId("merchant1", "merchant1_ref_1"))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/payment/merchant1_ref_1").principal(() -> "merchant1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
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
                .withMessage("some message")
                .build();
    }
}