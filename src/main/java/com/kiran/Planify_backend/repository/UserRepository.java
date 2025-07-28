package com.kiran.Planify_backend.repository;

import com.kiran.Planify_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationToken(String token);
    Optional<User> findByResetToken(String token);

    boolean existsByEmail(String email);
}
