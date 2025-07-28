package com.kiran.Planify_backend.controller;

import com.kiran.Planify_backend.dto.JwtResponse;
import com.kiran.Planify_backend.dto.LoginRequest;
import com.kiran.Planify_backend.dto.RegisterRequest;
import com.kiran.Planify_backend.model.User;
import com.kiran.Planify_backend.repository.UserRepository;
import com.kiran.Planify_backend.service.AuthService;
import com.kiran.Planify_backend.service.EmailService;
import com.kiran.Planify_backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EmailService emailService;
    private PasswordEncoder passwordEncoder;


    public AuthController(AuthService authService, UserRepository userRepository, JwtService jwtService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){

        User user = authService.register(request);

        emailService.sendVerificationEmail(user, "http://localhost:8080");


        return ResponseEntity.ok("User registered successfully. Please check your email to verify your account.");


    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        System.out.println("Received verification token: " + token);
        boolean verified = authService.verifyUser(token);
        if (verified) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "http://localhost:5173/verification-success")
                    .build(); // ✅ Redirect to frontend
        } else {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "http://localhost:5173/verification-failed")
                    .build(); // ❌ Redirect to failure page
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = authService.authenticate(request);

        if (!user.isVerified()) {
            return ResponseEntity
                    .status(403)
                    .body("Your account is not verified. Please check your email to verify.");
        }

        String token = jwtService.generateToken(user);
        JwtResponse jwtResponse = new JwtResponse(token, user.getEmail(), user.getRole());
        return ResponseEntity.ok(jwtResponse);
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String,String> request){
        String email = request.get("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(new Date(System.currentTimeMillis() + 15 * 60 * 1000));
        userRepository.save(user);

        String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + token;

        emailService.sendSimpleEmail(user.getEmail(), "Reset your password ","Click this link to reset your password: "+ resetLink);

        return  ResponseEntity.ok("Password reset link sent to email");

    }

    @PostMapping("/reset-password")
    public  ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody Map<String, String> request){
        String newPassword = request.get("newPassword");
        User user = userRepository.findByResetToken(token)
                .orElseThrow(()-> new RuntimeException("Invalid token"));

        if(user.getResetTokenExpiry().before(new Date())){
            return ResponseEntity.badRequest().body("Reset token expired");
        }

       user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successful");
    }

//    @GetMapping("/profile")
//    public ResponseEntity<?> getProfile(Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(401).body("Unauthorized");
//        }
//
//        User user = (User) authentication.getPrincipal(); // ✅ Get user directly from SecurityContext
//
//        return ResponseEntity.ok(user);
//    }

}
