package com.example.surveyapp.controller;

import com.example.surveyapp.entity.Employee;
import com.example.surveyapp.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ➕ Add employee
    @PostMapping
    public Employee addEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    // 📋 Get all employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // ✏️ Update employee
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id,
                                   @Valid @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    // ❌ Delete employee
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "Employee deleted successfully";
    }
}