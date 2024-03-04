package com.ecommerce.shopapp.services.email;

import com.ecommerce.shopapp.models.EmailDetails;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendSimpleMail(EmailDetails details);
    void sendHtmlEmail(EmailDetails details) throws MessagingException;
}