package com.example.email.service;

import com.example.email.details.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendMail(EmailDetails emailDetails) {
        try{
            SimpleMailMessage mail=new SimpleMailMessage();
            mail.setFrom(sender);
            mail.setTo(emailDetails.getRecipient());
            mail.setText(emailDetails.getMsgBody());
            mail.setSubject(emailDetails.getSubject());

            javaMailSender.send(mail);
            return "Mail sent successfully";
        }
        catch(Exception e){
            return "Error in sending email";
        }
    }

    @Override
    public String sendMailWithAttachment(EmailDetails emailDetails) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            // Setting multipart as true for attachments to
            // be send
            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMsgBody());
            mimeMessageHelper.setSubject(
                    emailDetails.getSubject());

            // Adding the attachment
            FileSystemResource file
                    = new FileSystemResource(
                    new File(emailDetails.getAttachment()));

            mimeMessageHelper.addAttachment(
                    file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        // Catch block to handle MessagingException
        catch (Exception e) {

            // Display message when exception occurred
            return "Error while sending mail!!!";
        }
    }
}
