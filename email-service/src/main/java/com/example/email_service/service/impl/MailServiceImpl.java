package com.example.email_service.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.email_service.model.dto.req.MaiLReq;
import com.example.email_service.service.MailService;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendMail(String to, MaiLReq lReq) {
        Context context = new Context();
        context.setVariable("to", to);
        context.setVariable("otp", lReq.getContent());
        String htmlContent = templateEngine.process("otp-template", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(new InternetAddress("ninh56099@gmail.com", "An ninh Support"));
            helper.setTo(to);
            helper.setSubject(lReq.getSubject());
            helper.setText(htmlContent, true);
            javaMailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
