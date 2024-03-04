package com.ecommerce.shopapp.services.auth;

import com.ecommerce.shopapp.config.JwtService;
import com.ecommerce.shopapp.dtos.*;
import com.ecommerce.shopapp.exceptions.BadRequestException;
import com.ecommerce.shopapp.exceptions.DataNotFoundException;
import com.ecommerce.shopapp.exceptions.UnauthorizedException;
import com.ecommerce.shopapp.models.EmailDetails;
import com.ecommerce.shopapp.models.Role;
import com.ecommerce.shopapp.models.User;
import com.ecommerce.shopapp.repositories.RoleRepository;
import com.ecommerce.shopapp.repositories.UserRepository;
import com.ecommerce.shopapp.responses.TokenResponse;
import com.ecommerce.shopapp.services.email.EmailService;
import com.ecommerce.shopapp.utils.GetEmailText;
import com.ecommerce.shopapp.utils.OTPCode;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ecommerce.shopapp.models.TokenType.REFRESH_TYPE;
import static com.ecommerce.shopapp.models.TokenType.RESET_TYPE;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final RoleRepository roleRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${frontend-url}")
    private String frontendUrl;

    @Override
    public void register(UserDTO userDTO) {
        Optional<User> optionalUser = userRepo.findByEmail(userDTO.getEmail());

        System.out.println(frontendUrl);

        if (optionalUser.isPresent()) {
            throw new BadRequestException("Email has been used");
        }

        Role userRole = roleRepo.findByName(Role.USER);

        User user = User.builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(userRole)
                .build();

        String otp = OTPCode.generateOTP(6);
        user.setOtp(otp);
        userRepo.save(user);

        String message = "This is your verify code: " + otp;
        EmailDetails emailDetails = EmailDetails
                .builder()
                .recipient(userDTO.getEmail())
                .msgBody("Hey! \n\n" + message)
                .subject(message)
                .build();

        emailService.sendSimpleMail(emailDetails);
    }

    @Override
    public TokenResponse login(LoginDTO loginDTO) {
       User user = userRepo.findByEmail(loginDTO.getEmail()).
               orElseThrow(() -> new BadRequestException("Email not found")
       );

       if(!user.isActive()) {
           throw new BadRequestException("Inactive account");
       }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword(),
                        user.getAuthorities()
                )
        );

        String atToken = jwtService.generateAccessToken(user);
        String rtToken = jwtService.generateRefreshToken(user);

        return TokenResponse.builder()
                .accessToken(atToken)
                .refreshToken(rtToken)
                .build();
    }

    public TokenResponse refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException();
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractEmail(refreshToken);

        if (userEmail == null) {
            throw new UnauthorizedException();
        }

        User user = userRepo.findByEmail(userEmail).
                orElseThrow(() -> new DataNotFoundException("Email not found"));

        if (!jwtService.isValidToken(refreshToken, REFRESH_TYPE, user)) {
            throw new UnauthorizedException();
        }

        String accessToken = jwtService.generateAccessToken(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void resendOTP(VerifyEmailDTO verifyEmailDTO) {
        User user = userRepo.findByEmail(verifyEmailDTO.getEmail()).
                orElseThrow(() -> new DataNotFoundException("Email not found"));

        if(user.isActive()) {
            throw new BadRequestException("Already activated");
        }

        String otp = OTPCode.generateOTP(6);
        user.setOtp(otp);
        userRepo.save(user);

        String message = "This is your verify code: " + otp;
        EmailDetails emailDetails = EmailDetails
                .builder()
                .recipient(verifyEmailDTO.getEmail())
                .msgBody("Hey! \n\n" + message)
                .subject(message)
                .build();

        emailService.sendSimpleMail(emailDetails);
    }

    @Override
    public TokenResponse activeEmail(ActiveEmailDTO activeEmailDTO) {
        User user = userRepo.findByEmail(activeEmailDTO.getEmail()).
                orElseThrow(() -> new BadRequestException("Email not found")
        );

        if (!user.getOtp().equals(activeEmailDTO.getOtp())) {
            throw new BadRequestException("OTP does not match");
        }

        user.setActive(true);
        user.setOtp("");
        userRepo.save(user);

        String atToken = jwtService.generateAccessToken(user);
        String rtToken = jwtService.generateRefreshToken(user);

        return TokenResponse.builder()
                .accessToken(atToken)
                .refreshToken(rtToken)
                .build();
    }

    @Override
    public void verifyEmail(VerifyEmailDTO verifyEmailDTO) throws MessagingException {
        User user = userRepo.findByEmail(verifyEmailDTO.getEmail()).
                orElseThrow(() -> new BadRequestException("Email not found"));



        String resetToken = jwtService.generateResetToken(user);
        String redirectUrl = String.format("%s/password-recovery?reset-token=%s", frontendUrl, resetToken);
        String message = GetEmailText.getEmailText(redirectUrl);

        EmailDetails emailDetails = EmailDetails
                .builder()
                .recipient(verifyEmailDTO.getEmail())
                .msgBody(message)
                .subject("Password recovery")
                .build();

        emailService.sendHtmlEmail(emailDetails);
    }

    @Override
    public TokenResponse passwordRecovery(PasswordRecoveryDTO passwordRecoveryDTO) {
        String userEmail = jwtService.extractEmail(passwordRecoveryDTO.getResetToken());

        if (userEmail == null) {
            throw new UnauthorizedException();
        }

        User user = userRepo.findByEmail(userEmail).
                orElseThrow(() -> new DataNotFoundException("Email not found"));

        if (!jwtService.isValidToken(passwordRecoveryDTO.getResetToken(), RESET_TYPE, user)) {
            throw new UnauthorizedException();
        }

        if (!passwordRecoveryDTO.getPassword()
                .equals(passwordRecoveryDTO.getConfirmPassword())) {
            throw new BadRequestException("Password does not match");
        }

        user.setPassword((passwordEncoder.encode(passwordRecoveryDTO.getPassword())));
        userRepo.save(user);

        String atToken = jwtService.generateAccessToken(user);
        String rtToken = jwtService.generateRefreshToken(user);

        return TokenResponse.builder()
                .accessToken(atToken)
                .refreshToken(rtToken)
                .build();
    }
}
