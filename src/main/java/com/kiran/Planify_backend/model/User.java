package com.kiran.Planify_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private  String email;

    private String password;

    @Column(name = "is_verified")
    private boolean isVerified = false;

    @Column(name = "verification_token")
    private String verificationToken;

    private String resetToken;
    private Date resetTokenExpiry;


    private String role;// USER OR ADMIN


    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    private Date subscribedAt;
    private Date subscriptionExpiry;

    public User(Long id, String name, String email, String password, boolean isVerified, String verificationToken, String resetToken, Date resetTokenExpiry, String role, Plan plan, Date subscribedAt, Date subscriptionExpiry) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isVerified = isVerified;
        this.verificationToken = verificationToken;
        this.resetToken = resetToken;
        this.resetTokenExpiry = resetTokenExpiry;
        this.role = role;
        this.plan = plan;
        this.subscribedAt = subscribedAt;
        this.subscriptionExpiry = subscriptionExpiry;
    }

    public User(){

    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Date getResetTokenExpiry() {
        return resetTokenExpiry;
    }

    public void setResetTokenExpiry(Date resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getSubscribedAt() {
        return subscribedAt;
    }

    public void setSubscribedAt(Date subscribedAt) {
        this.subscribedAt = subscribedAt;
    }

    public Date getSubscriptionExpiry() {
        return subscriptionExpiry;
    }

    public void setSubscriptionExpiry(Date subscriptionExpiry) {
        this.subscriptionExpiry = subscriptionExpiry;
    }
}