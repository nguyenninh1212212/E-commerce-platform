package com.example.auth_service.service;

import java.security.SecureRandom;
import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final StringRedisTemplate redisTemplate;
    private static final Duration OTP_TTL = Duration.ofMinutes(3);
    private static final Duration FAIL_TTL = Duration.ofMinutes(5);
    private static final Duration COOLDOWN_TTL = Duration.ofSeconds(60);
    private static final int MAX_FAIL = 5;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateAndStoreOtp(String key) {
        String cooldownKey = "otp:cooldown:" + key;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cooldownKey))) {
            throw new RuntimeException("Gửi OTP quá nhanh, thử lại sau");
        }

        int otpInt = secureRandom.nextInt(10000);
        String otp = String.format("%04d", otpInt);
        String otpKey = "otp:" + key;

        redisTemplate.opsForValue().set(otpKey, otp, OTP_TTL);
        redisTemplate.opsForValue().set(cooldownKey, "1", COOLDOWN_TTL);

        redisTemplate.delete("otp:fail:" + key);

        return otp;
    }

    public void verifyOtp(String key, String inputOtp) {
        String otpKey = "otp:" + key;
        String failKey = "otp:fail:" + key;

        String realOtp = redisTemplate.opsForValue().get(otpKey);
        if (realOtp == null) {
            throw new RuntimeException("OTP expired");
        }

        if (!realOtp.equals(inputOtp)) {
            Long fails = redisTemplate.opsForValue().increment(failKey);
            redisTemplate.expire(failKey, FAIL_TTL);
            if (fails != null && fails >= MAX_FAIL) {
                redisTemplate.delete(otpKey);
                throw new RuntimeException("Too much wrong password , must be enter otp");
            }
            throw new RuntimeException("OTP invalid");
        }

        redisTemplate.delete(otpKey);
        redisTemplate.delete(failKey);
    }
}
