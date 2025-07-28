package com.kiran.Planify_backend.controller;

import com.kiran.Planify_backend.model.User;
import com.kiran.Planify_backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    private final UserRepository userRepository;


    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<?> getStats(){
        long totalUsers = userRepository.count();
        long subscribedUsers = userRepository.findAll().stream().filter(u -> u.getPlan() != null).count();
        double totalRevenue = userRepository.findAll().stream()
                .filter(u-> u.getPlan() != null)
                .mapToDouble(u-> u.getPlan().getPrice())
                .sum();

        return ResponseEntity.ok(Map.of(
                "totalUsers",totalUsers,
                "subscribedUsers",subscribedUsers,
                "totalRevenue",totalRevenue
        ));
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) String plan){

        List<User> users;

        if(plan != null){
            users = userRepository.findAll().stream()
                    .filter(u-> u.getPlan() != null && u.getPlan().getName().equalsIgnoreCase(plan))
                    .toList();
        }else {
            users = userRepository.findAll();
        }

        return ResponseEntity.ok(users);
    }
}
