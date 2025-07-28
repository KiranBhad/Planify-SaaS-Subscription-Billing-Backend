package com.kiran.Planify_backend.dto;

import com.kiran.Planify_backend.model.User;

public class UserDto {
    private Long id;
    private String email;
    private String role;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
// Getters and setters
}
