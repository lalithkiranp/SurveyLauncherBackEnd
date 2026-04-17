package com.example.surveyapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.surveyapp.dto.SubmitSurveyDTO;
import com.example.surveyapp.dto.SurveyDTO;
import com.example.surveyapp.service.SurveyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/survey")
public class UserSurveyController {

    @Autowired
    private SurveyService surveyService;

    // 👤 USER
    @GetMapping("/{id}")
    public SurveyDTO getSurvey(@PathVariable Long id) {
        return surveyService.getSurveyById(id);
    }

    // 👤 USER
    @PostMapping("/{id}/submit")
    public String submitSurvey(@PathVariable Long id,
                               @Valid @RequestBody SubmitSurveyDTO dto) {
        surveyService.submitSurvey(id, dto);
        return "Survey submitted successfully";
    }
}