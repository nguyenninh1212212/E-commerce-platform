package com.example.email_service.service;

import com.example.email_service.model.dto.req.MaiLReq;

public interface MailService {

    void sendMail(String email, MaiLReq req);

}
