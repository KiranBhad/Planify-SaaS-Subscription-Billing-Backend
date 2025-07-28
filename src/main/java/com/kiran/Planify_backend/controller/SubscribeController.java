package com.kiran.Planify_backend.controller;

import com.kiran.Planify_backend.model.Plan;
import com.kiran.Planify_backend.model.User;
import com.kiran.Planify_backend.repository.PlanRepository;
import com.kiran.Planify_backend.repository.UserRepository;
import com.kiran.Planify_backend.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/subscribe")
public class SubscribeController {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final JwtService jwtService;

    public SubscribeController(UserRepository userRepository, PlanRepository planRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/{planId}")
    public ResponseEntity<?> subscribeToPlan(
            @PathVariable Long planId,
            @RequestHeader("Authorization") String authHeader){

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User Not Found"));
        Plan plan = planRepository.findById(planId).orElseThrow(()-> new RuntimeException("Plan not found"));

        user.setPlan(plan);
        user.setSubscribedAt(new Date());

        long expiryMillis = System.currentTimeMillis()+(30L * 24 * 60 * 60 * 1000);
        user.setSubscriptionExpiry(new Date(expiryMillis));

        userRepository.save(user);
        return ResponseEntity.ok("Subscribed to "+ plan.getName() + " plan successfully ");

    }
}
