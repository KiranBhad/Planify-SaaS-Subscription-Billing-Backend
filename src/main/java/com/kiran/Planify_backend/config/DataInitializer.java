package com.kiran.Planify_backend.config;

import com.kiran.Planify_backend.model.Plan;
import com.kiran.Planify_backend.repository.PlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initPlans(PlanRepository planRepository) {
        return args -> {
            if (planRepository.count() == 0) {
                List<Plan> defaultPlans = List.of(
                        new Plan(null, "Free", "Basic access with limited features", 0.0, 7),
                        new Plan(null, "Pro", "Access to pro features", 499.0, 0),
                        new Plan(null, "Premium", "All features unlocked", 999.0, 90),
                        new Plan(null, "Delux", "All features unlocked", 10000.0, 0)
                );

                planRepository.saveAll(defaultPlans);
                System.out.println("✅ Default plans inserted.");
            } else {
                System.out.println("ℹ️ Plans already exist. Skipping initialization.");
            }
        };
    }
}
