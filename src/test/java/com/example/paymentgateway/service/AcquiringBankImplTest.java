package com.example.paymentgateway.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AcquiringBankImplTest {
    private WireMockServer wireMockServer;
    private AcquiringBankImpl acquiringBank;

    @BeforeEach
    public void setUp() {
        // Configure WireMock
        wireMockServer = new WireMockServer(WireMockConfiguration.options()
                .port(5034));
        wireMockServer.start();
        acquiringBank = new AcquiringBankImpl(wireMockServer.baseUrl() + "/api/payment");
    }

    @AfterEach
    public void tearDown() {
        // Stop WireMock server
        wireMockServer.stop();
    }

    @Test
    void givenAcquiringBankReturnsSuccessThenReturnSuccessfulResponse() {
        wireMockServer.stubFor(post(urlEqualTo("/api/payment"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"result\":\"SUCCESSFUL\"}")));

        AcquiringBankResponse response = acquiringBank.sendPaymentRequest(new
                AcquiringBankPaymentRequest("1234567890123456", 12, 2021, "001", "GBP", null));


        assertEquals(AcquiringBankPaymentResult.SUCCESSFUL, response.result());
    }

    @Test
    void givenAcquiringBankReturnsFailedThenReturnFailedResponse() {
        wireMockServer.stubFor(post(urlEqualTo("/api/payment"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"result\":\"FAILED\", \"message\":\"Insufficient funds\"}")));

        AcquiringBankResponse response = acquiringBank.sendPaymentRequest(new
                AcquiringBankPaymentRequest("1234567890123456", 12, 2021, "001", "GBP", null));


        assertEquals(AcquiringBankPaymentResult.FAILED, response.result());
        assertEquals("Insufficient funds", response.message());
    }
}