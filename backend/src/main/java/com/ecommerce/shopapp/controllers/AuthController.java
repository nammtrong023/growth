package com.ecommerce.shopapp.controllers;

import com.ecommerce.shopapp.dtos.*;
import com.ecommerce.shopapp.responses.TokenResponse;
import com.ecommerce.shopapp.services.auth.AuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<String> register(
           @Valid @RequestBody UserDTO userDTO) {
        try {
            authService.register(userDTO);
            return ResponseEntity.ok("Registered successfully, please check your email!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @PostMapping("refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            HttpServletRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("active-email")
    public ResponseEntity<TokenResponse> activeEmail(
            @Valid @RequestBody ActiveEmailDTO activeEmailDTO) {
        return ResponseEntity.ok(authService.activeEmail(activeEmailDTO));
    }

    @PostMapping("resend-otp")
    public ResponseEntity<String> resendOTP(
            @Valid @RequestBody VerifyEmailDTO verifyEmailDTO) {
        authService.resendOTP(verifyEmailDTO);
        return ResponseEntity.ok("Resent otp");
    }

    @PostMapping("verify-email")
    public ResponseEntity<String> verifyEmail(
            @Valid @RequestBody VerifyEmailDTO verifyEmailDTO) throws MessagingException {
        authService.verifyEmail(verifyEmailDTO);
        return ResponseEntity.ok("Please check your email!");
    }

    @PostMapping("password-recovery")
    public ResponseEntity<TokenResponse> passwordRecovery(
            @Valid @RequestBody PasswordRecoveryDTO passwordRecoveryDTO) {
        authService.passwordRecovery(passwordRecoveryDTO);
        return ResponseEntity.ok(authService.passwordRecovery(passwordRecoveryDTO));
    }
}



