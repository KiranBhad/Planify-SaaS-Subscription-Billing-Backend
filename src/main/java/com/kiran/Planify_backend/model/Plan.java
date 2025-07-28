package com.kiran.Planify_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Free, Pro, Premium
    private String description;
    private Double price;

    private int durationInDays;

    public Plan(){

    }

    public Plan(Long id, String name, String description, Double price, int durationInDays) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationInDays = durationInDays;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
