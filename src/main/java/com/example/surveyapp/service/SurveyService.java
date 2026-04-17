package com.example.surveyapp.service;

import java.util.List;

import com.example.surveyapp.dto.AdminSurveyDashboardDTO;
import com.example.surveyapp.dto.LaunchSurveyDTO;
import com.example.surveyapp.dto.StatsResponseDTO;
import com.example.surveyapp.dto.SubmitSurveyDTO;
import com.example.surveyapp.dto.SurveyDTO;

public interface SurveyService {
    void createSurvey(SurveyDTO surveyDTO);
    SurveyDTO getSurveyById(Long id);
    void submitSurvey(Long surveyId, SubmitSurveyDTO dto);
    void launchSurvey(Long surveyId, LaunchSurveyDTO dto);
    StatsResponseDTO getSurveyStats(Long surveyId);
    byte[] exportSurvey(Long surveyId);
    void deleteSurvey(Long id);
    List<AdminSurveyDashboardDTO> getAllSurveysForAdmin();
}