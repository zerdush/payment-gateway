package com.example.paymentgateway.controller;

import com.example.paymentgateway.domain.PaymentResponse;
import com.example.paymentgateway.service.SendPaymentRequestCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SendPaymentRequestCommand sendPaymentRequestCommand;

    @Test
    void whenPostThenReturnOk() throws Exception {
//        when(sendPaymentRequestCommand.sendPaymentRequest(any())).thenReturn(PaymentResponse.SUCCESSFUL);
//        mockMvc.perform(MockMvcRequestBuilders.post("/payment"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}