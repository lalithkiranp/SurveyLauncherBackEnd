package com.example.surveyapp.service;

import com.example.surveyapp.entity.Employee;
import java.util.List;

public interface EmployeeService {

    Employee addEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Employee updateEmployee(Long id, Employee employee);

    void deleteEmployee(Long id);
}