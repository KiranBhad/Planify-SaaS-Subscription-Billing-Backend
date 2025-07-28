package com.kiran.Planify_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private String email;
    private String role;

    public JwtResponse(String token, String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    public JwtResponse() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
