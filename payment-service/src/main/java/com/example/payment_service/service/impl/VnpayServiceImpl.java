package com.example.payment_service.service.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.Arrays.Iterator;
import org.springframework.stereotype.Service;

import com.example.payment_service.config.wallet.VNPayConfig;
import com.example.payment_service.model.dto.req.VNPayRequest;
import com.example.payment_service.service.VnpayService;
import com.nimbusds.jose.shaded.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VnpayServiceImpl implements VnpayService {

    private final VNPayConfig config;

    public String createPayment(HttpServletRequest req, VNPayRequest input) {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", config.getTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(input.getAmount() * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
        vnp_Params.put("vnp_OrderInfo", input.getOrderInfo());
        vnp_Params.put("vnp_OrderType", input.getOrderType());
        vnp_Params.put("vnp_Locale", input.getLocale() != null ? input.getLocale() : "vn");
        vnp_Params.put("vnp_ReturnUrl", config.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", getIpAddress(req));

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        format.setTimeZone(TimeZone.getTimeZone("Etc/GMT+7"));
        Calendar cal = Calendar.getInstance();
        vnp_Params.put("vnp_CreateDate", format.format(cal.getTime()));
        cal.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", format.format(cal.getTime()));

        if (input.getBankCode() != null && !input.getBankCode().isEmpty()) {
            vnp_Params.put("vnp_BankCode", input.getBankCode());
        }

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String key = fieldNames.get(i);
            String value = vnp_Params.get(key);
            hashData.append(key).append('=').append(URLEncoder.encode(value, StandardCharsets.UTF_8));
            query.append(URLEncoder.encode(key, StandardCharsets.UTF_8)).append('=')
                    .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
            if (i < fieldNames.size() - 1) {
                hashData.append('&');
                query.append('&');
            }
        }

        String secureHash = hmacSHA512(config.getHashSecret(), hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        return config.getPayUrl() + "?" + query.toString();
    }

    public String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip != null) ? ip : request.getRemoteAddr();
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo chữ ký VNPay", e);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            String h = Integer.toHexString(0xff & b);
            if (h.length() == 1)
                hex.append('0');
            hex.append(h);
        }
        return hex.toString();
    }

    @Override
    public String resultPayment() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resultPayment'");
    }

}
