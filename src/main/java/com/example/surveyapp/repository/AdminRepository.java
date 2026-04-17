package com.example.surveyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.surveyapp.entity.Admin;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
}