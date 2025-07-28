package com.kiran.Planify_backend.controller;

import com.kiran.Planify_backend.dto.UserDto;
import com.kiran.Planify_backend.dto.UserProfileDto;
import com.kiran.Planify_backend.model.User;
import com.kiran.Planify_backend.repository.UserRepository;
import com.kiran.Planify_backend.service.JwtService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final JwtService jwtService;
    @Autowired
    private final UserRepository userRepository;

    public UserController(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(!user.getRole().equalsIgnoreCase("ADMIN")){
            return ResponseEntity.status(403).body("Access Denied: Admins only.");
        }

        return ResponseEntity.ok(userRepository.findAll());
    }
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User user = (User) authentication.getPrincipal(); // ✅ correct principal extraction
        UserDto dto = new UserDto(user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/test")
    public String test(){
        return "This is for testing";
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(user);
    }


}

