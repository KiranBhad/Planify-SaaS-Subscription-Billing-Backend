package com.kiran.Planify_backend.controller;

import com.kiran.Planify_backend.model.Plan;
import com.kiran.Planify_backend.model.User;
import com.kiran.Planify_backend.repository.PlanRepository;
import com.kiran.Planify_backend.repository.UserRepository;
import com.kiran.Planify_backend.service.EmailService;
import com.kiran.Planify_backend.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    public PlanController(PlanRepository planRepository, UserRepository userRepository, JwtService jwtService, EmailService emailService) {
        this.planRepository = planRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createPlan(@RequestBody Plan plan) {
        Plan savedPlan = planRepository.save(plan);
        return ResponseEntity.ok(savedPlan);
    }

//    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllPlans() {
        return ResponseEntity.ok(planRepository.findAll());
    }



    @PostMapping("/subscribe/{planId}")
    public ResponseEntity<Map<String,Object>> subscribeToPlan(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long planId){

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));

        Plan plan = planRepository.findById(planId).orElseThrow(()-> new RuntimeException("Plan not found"));

    Date now = new Date();
    Date expiry = new Date(now.getTime() + (plan.getDurationInDays() * 24L * 60 * 1000));

    user.setPlan(plan);
    user.setSubscribedAt(now);
    user.setSubscriptionExpiry(expiry);
    userRepository.save(user);

    emailService.sendSubscriptionConfirmation(user);

    return ResponseEntity.ok(Map.of(
            "message","Subscribed to "+ plan.getName() + " Plan",
            "planId",plan.getId(),
            "subscribedAt",now,
            "subscriptionExpiry",expiry
    ));

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN)")
    @PutMapping("/{planId}")
    public ResponseEntity<?> updatePlan(@PathVariable Long planId, @RequestBody Plan updatedPlan){

        Plan plan = planRepository.findById(planId)
                .orElseThrow(()-> new RuntimeException("Plan not found"));

        plan.setName(updatedPlan.getName());
        plan.setDescription(updatedPlan.getDescription());
        plan.setPrice(updatedPlan.getPrice());
        plan.setDurationInDays(updatedPlan.getDurationInDays());

        planRepository.save(plan);

        return ResponseEntity.ok(Map.of("message","Plan updated successfully"));
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{planId}")
    public ResponseEntity<?> deletePlan(@PathVariable Long planId){

        if(!planRepository.existsById(planId)){
            return ResponseEntity.status(404).body(Map.of("error","Plan not found"));
        }

        planRepository.deleteById(planId);
        return ResponseEntity.ok(Map.of("message","Plan deleted successfully"));
    }

}

