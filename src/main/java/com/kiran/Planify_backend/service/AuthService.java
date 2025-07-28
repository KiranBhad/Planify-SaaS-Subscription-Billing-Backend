package com.kiran.Planify_backend.service;

import com.kiran.Planify_backend.dto.LoginRequest;
import com.kiran.Planify_backend.dto.RegisterRequest;
import com.kiran.Planify_backend.model.User;
import com.kiran.Planify_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder){

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName()); // only if name is part of request
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_ADMIN");
        user.setVerified(false);

        // ✅ Generate token here
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);

        return userRepository.save(user); // token now saved in DB
    }


    public User authenticate(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    public void resetPassword(String token, String newPassword){
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(()-> new RuntimeException("Invalid token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public boolean verifyUser(String token) {
        Optional<User> optionalUser = userRepository.findByVerificationToken(token);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check if already verified
            if (user.isVerified()) {
                return true;
            }

            // Mark user as verified
            user.setVerified(true);
            user.setVerificationToken(null); // optional: clear the token after verification
            userRepository.save(user);
            return true;
        }

        return false;
    }

}
