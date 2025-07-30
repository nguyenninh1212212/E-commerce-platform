package com.example.payment_service.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment_service.model.dto.req.VNPayRequest;
import com.example.payment_service.model.dto.res.ApiRes;
import com.example.payment_service.service.VnpayService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final VnpayService vnPayService;

    @PostMapping("/vnpay")
    public ResponseEntity<ApiRes<String>> createPayment(@RequestBody VNPayRequest input, HttpServletRequest request) {
        String paymentUrl = vnPayService.createPayment(request, input);
        return ResponseEntity.ok(ApiRes.of(HttpStatus.OK.value(), paymentUrl));
    }

    @GetMapping("/vnpay/return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> allParams) {
        // TODO: check vnp_SecureHash here
        return ResponseEntity.ok(allParams);
    }
}
