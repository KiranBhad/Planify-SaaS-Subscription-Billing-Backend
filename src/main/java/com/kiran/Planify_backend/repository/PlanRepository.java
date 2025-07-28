package com.kiran.Planify_backend.repository;


import com.kiran.Planify_backend.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository  extends JpaRepository<Plan,Long> {
}
