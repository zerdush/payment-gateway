package com.example.paymentgateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PaymentController {
    @PostMapping("/payment")
    public ResponseEntity<String> post(){
        return ResponseEntity.ok("");
    }
}
