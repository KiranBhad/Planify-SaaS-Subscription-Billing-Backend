package com.kiran.Planify_backend.controller;

import com.kiran.Planify_backend.model.User;
import com.kiran.Planify_backend.repository.UserRepository;
import com.kiran.Planify_backend.service.JwtService;
import org.apache.catalina.webresources.JarWarResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public SubscriptionController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentSubscription(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));

        if(user.getPlan() == null){
            return ResponseEntity.ok("User has not subscribed to any plan yet.");
        }

        Map<String, Object> subscriptionDetails = new HashMap<>();
        subscriptionDetails.put("Plan", user.getPlan().getName());
        subscriptionDetails.put("subscribedAt",user.getSubscribedAt());
        subscriptionDetails.put("expiresAt",user.getSubscriptionExpiry());

        return ResponseEntity.ok(subscriptionDetails);
    }
}
