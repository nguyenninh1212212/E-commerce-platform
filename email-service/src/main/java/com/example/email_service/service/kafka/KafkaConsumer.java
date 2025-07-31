package com.example.email_service.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.email_service.model.dto.req.MaiLReq;
import com.example.email_service.service.MailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mail.MailRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final MailService mailService;

    @KafkaListener(topics = "mail.otp")
    public void cosumerSendMail(ConsumerRecord<String, byte[]> value) throws Exception {
        MailRequest mail = MailRequest.parseFrom(value.value());
        if (mail.getTo().isEmpty() || mail.getSubject().isEmpty() || mail.getContent().isEmpty()) {
            log.warn("Received invalid mail request: {}", mail);
            return;
        }
        MaiLReq lReq = new MaiLReq();
        lReq.setContent(mail.getContent());
        lReq.setSubject(mail.getSubject());
        mailService.sendMail(mail.getTo(), lReq);
    }
}
