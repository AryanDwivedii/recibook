package com.example.email.service;

import com.example.email.details.EmailDetails;

public interface EmailService {

    String sendMail(EmailDetails emailDetails);

    String sendMailWithAttachment(EmailDetails emailDetails);
}
