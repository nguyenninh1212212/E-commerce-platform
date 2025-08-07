package com.example.email_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.email_service.model.dto.req.MaiLReq;
import com.example.email_service.model.dto.res.Message;
import com.example.email_service.service.MailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMail(@RequestParam String to, @RequestBody MaiLReq req) {
        mailService.sendMail(to, req);
        return ResponseEntity.ok(Message.of(200, "Send success"));
    }

}
