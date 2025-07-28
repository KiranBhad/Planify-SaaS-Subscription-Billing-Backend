package com.kiran.Planify_backend.config;

import com.kiran.Planify_backend.controller.SubscriptionValidationFilter;
import com.kiran.Planify_backend.config.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final SubscriptionValidationFilter subscriptionValidationFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, SubscriptionValidationFilter subscriptionValidationFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.subscriptionValidationFilter = subscriptionValidationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/user/**",
                                "/api/plans/all"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(subscriptionValidationFilter, JwtAuthFilter.class);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // Support both common React development ports
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",  // Create React App default
                "http://localhost:5173",  // Vite default
                "http://127.0.0.1:3000",  // Alternative localhost
                "http://127.0.0.1:5173"   // Alternative localhost
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*")); // Allow all headers
        config.setExposedHeaders(List.of("Authorization")); // Expose auth header if needed

        // Handle preflight requests
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}