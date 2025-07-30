package com.example.payment_service.service;

import com.example.payment_service.model.dto.req.VNPayRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface VnpayService {
    String createPayment(HttpServletRequest req, VNPayRequest input);

    String resultPayment();
}