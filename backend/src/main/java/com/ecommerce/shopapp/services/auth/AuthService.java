package com.ecommerce.shopapp.services.auth;

import com.ecommerce.shopapp.dtos.*;
import com.ecommerce.shopapp.responses.TokenResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
        void register(UserDTO userDTO);
        TokenResponse login(LoginDTO userLoginDTO);
        TokenResponse refreshToken(HttpServletRequest request);

        TokenResponse activeEmail(ActiveEmailDTO activeEmailDTO);

        void resendOTP(VerifyEmailDTO verifyEmailDTO);

        void verifyEmail(VerifyEmailDTO verifyEmailDTO) throws MessagingException;

        TokenResponse passwordRecovery(PasswordRecoveryDTO passwordRecoveryDTO);
}
