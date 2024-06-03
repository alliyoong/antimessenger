package com.khanh.antimessenger.utilities;

import com.khanh.antimessenger.constant.VerificationType;
import com.khanh.antimessenger.exception.SendEmailException;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

import static com.khanh.antimessenger.constant.EmailConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Async
    public void sendEmail(String recipient, String firstName, String data, VerificationType type) {
        Message msg = new MimeMessage(getSession());
        try {
            String subject;
            String content;
            switch (type) {
                case RESET_PASSWORD_BY_USER -> {
                    subject = RESET_PASSWORD_BY_USER_MAIL_SUBJECT;
                    content = RESET_PASSWORD_BY_USER_MAIL_TEMPLATE;
                }
                case RESET_PASSWORD_BY_ADMIN -> {
                    subject = RESET_PASSWORD_BY_ADMIN_MAIL_SUBJECT;
                    content = RESET_PASSWORD_BY_ADMIN_MAIL_TEMPLATE;
                }
                case ACCOUNT -> {
                    subject = ACCOUNT_VERIFY_MAIL_SUBJECT;
                    content = ACCOUNT_VERIFY_MAIL_TEMPLATE;
                }
                default -> throw new SendEmailException();
            }
            msg.setFrom(new InternetAddress(FROM_EMAIL));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            msg.setSubject(subject);

            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(String.format(content, firstName, data), "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);
            msg.setContent(multipart);

            Transport.send(msg);
        } catch (MessagingException e) {
            log.error(e.getStackTrace().toString());
            throw new SendEmailException();
        }
    }

    private Session getSession() {
        Properties prop = new Properties();
        prop.put(SMTP_AUTH, true);
        prop.put(SMTP_STARTTLS_ENABLED, true);
        prop.put(SMTP_STARTTLS_REQUIRED, true);
        prop.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        prop.put(SMTP_PORT, DEFAULT_PORT);
        prop.put(SMTP_SSL_TRUST, GMAIL_SMTP_SERVER);
        return Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MAIL_USERNAME, MAIL_PASSWORD);
            }
        });
    }

}
