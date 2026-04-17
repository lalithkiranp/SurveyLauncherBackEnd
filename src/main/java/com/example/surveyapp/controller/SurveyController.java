package com.example.surveyapp.controller;

import com.example.surveyapp.dto.AdminSurveyDashboardDTO;
import com.example.surveyapp.dto.LaunchSurveyDTO;
import com.example.surveyapp.dto.StatsResponseDTO;
import com.example.surveyapp.dto.SubmitSurveyDTO;
import com.example.surveyapp.dto.SurveyDTO;
import com.example.surveyapp.service.SurveyService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    // ✅ ADMIN
    @PostMapping("/survey")
    public String createSurvey(@RequestBody SurveyDTO surveyDTO) {
        surveyService.createSurvey(surveyDTO);
        return "Survey created successfully";
    }

    // ✅ ADMIN
    @PutMapping("/survey/{id}/launch")
    public String launchSurvey(@PathVariable Long id, @RequestBody LaunchSurveyDTO dto) {
        surveyService.launchSurvey(id, dto);
        return "Survey launched successfully";
    }

    // ✅ ADMIN
    @GetMapping("/survey/{id}/stats")
    public StatsResponseDTO getStats(@PathVariable Long id) {
        return surveyService.getSurveyStats(id);
    }

    // ✅ ADMIN
    @GetMapping("/survey/{id}/export")
    public ResponseEntity<byte[]> exportSurvey(@PathVariable Long id) {

        byte[] data = surveyService.exportSurvey(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=survey.csv")
                .body(data);
    }

    // ✅ ADMIN
    @DeleteMapping("/survey/{id}")
    public String deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return "Survey deleted successfully";
    }
    
 // ✅ ADMIN DASHBOARD
    @GetMapping("/surveys")
    public List<AdminSurveyDashboardDTO> getAllSurveys() {
        return surveyService.getAllSurveysForAdmin();
    }
}