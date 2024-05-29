package com.example.login.signin.controller;

import com.example.login.signin.entity.Users;
import com.example.login.signin.exception.OTPExpiredException;
import com.example.login.signin.payload.ForgotPasswordRequest;
import com.example.login.signin.payload.LoginRequest;
import com.example.login.signin.payload.ResetPasswordRequest;
import com.example.login.signin.repository.UserRepository;
import com.example.login.signin.service.EmailService;
import com.example.login.signin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;



    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        try {
            if (userRepository.findByUsername(user.getUsername()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already taken");
            }
            if (userRepository.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already registered");
            }
            Users savedUser = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (DataIntegrityViolationException ex) {
            throw ex;
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        // Check if username and password are provided
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            LOGGER.warning("Username or password is null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password are required");
        }

        // Attempt login
        LOGGER.info("Attempting login with username: " + loginRequest.getUsername());
        Users existingUser = userRepository.findByUsername(loginRequest.getUsername());
        if (existingUser != null) {
            LOGGER.info("User found: " + existingUser.toString());
            // Check if password matches
            if (existingUser.getPassword().equals(loginRequest.getPassword())) {
                LOGGER.info("Password match for user: " + existingUser.getUsername());
                return ResponseEntity.ok(existingUser);
            } else {
                LOGGER.warning("Password mismatch for user: " + existingUser.getUsername());
            }
        } else {
            LOGGER.warning("No user found with username: " + loginRequest.getUsername());
        }

        // Return unauthorized status if login fails
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            userService.sendForgotPasswordEmail(request.getEmail());
            return ResponseEntity.ok("Password reset email sent successfully");
        } catch (OTPExpiredException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        try {
            userService.verifyOTP(email, otp); // Implement this method in your UserService
            return ResponseEntity.ok("OTP verified successfully");
        } catch (OTPExpiredException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OTP verification failed: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            userService.resetPassword(request);
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

}
