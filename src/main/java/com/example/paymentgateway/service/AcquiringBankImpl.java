package com.example.paymentgateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AcquiringBankImpl implements AcquiringBank {

    private final OkHttpClient httpClient;
    private final String apiUrl;
    private final ObjectMapper objectMapper;

    public AcquiringBankImpl(@Value("${ACQUIRING_BANK_URL}") String apiUrl) {
        this.apiUrl = apiUrl;
        httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public AcquiringBankResponse sendPaymentRequest(AcquiringBankPaymentRequest acquiringBankPaymentRequest) {
        Request request;
        try {
            request = new Request.Builder()
                    .url(apiUrl)
                    .post(
                            RequestBody.create(
                            objectMapper.writerFor(AcquiringBankPaymentRequest.class).writeValueAsString(acquiringBankPaymentRequest),
                                            MediaType.parse("application/json")))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try(Response response = httpClient.newCall(request).execute()) {
            if(response.body() == null){
                throw new RuntimeException("Response body is null");
            }
            return objectMapper.readValue(response.body().string(), AcquiringBankResponse.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
