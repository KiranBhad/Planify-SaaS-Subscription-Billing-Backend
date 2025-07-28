package com.kiran.Planify_backend.dto;

public class UserProfileDto {
    private String name;
    private String email;

    // constructor
    public UserProfileDto(String name, String email) {
        this.name = name;
        this.email = email;
    }


    // getters and setters


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
