package com.example.surveyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.surveyapp.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
