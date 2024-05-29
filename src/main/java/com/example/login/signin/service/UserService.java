package com.example.login.signin.service;

import com.example.login.signin.entity.Users;
import com.example.login.signin.exception.OTPExpiredException;
import com.example.login.signin.payload.ForgotPasswordRequest;
import com.example.login.signin.payload.ResetPasswordRequest;
import com.example.login.signin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public void sendForgotPasswordEmail(String email) {
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            throw new OTPExpiredException("User with email " + email + " not found");
        }

        String otp = generateOTP();
        user.setOtp(otp);
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);
    }

    private String generateOTP() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void verifyOTP(String email, String otp) {
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            throw new OTPExpiredException("User with email " + email + " not found");
        }

        if (!user.getOtp().equals(otp)) {
            throw new OTPExpiredException("Invalid OTP");
        }

    }
    public void resetPassword(ResetPasswordRequest request) {
        Users user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new OTPExpiredException("User with email " + request.getEmail() + " not found");
        }

        // Update user's password
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

}
