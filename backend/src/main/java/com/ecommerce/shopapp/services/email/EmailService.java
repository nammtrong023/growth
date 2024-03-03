package com.ecommerce.shopapp.services.email;

import com.ecommerce.shopapp.models.EmailDetails;

public interface EmailService {
    void sendSimpleMail(EmailDetails details);
}
