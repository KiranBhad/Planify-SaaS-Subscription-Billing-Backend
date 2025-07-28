package com.kiran.Planify_backend.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    private User user;

    private Date expiryDate;
}
