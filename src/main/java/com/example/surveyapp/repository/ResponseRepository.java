package com.example.surveyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.surveyapp.entity.Response;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    long countBySurveyId(Long surveyId);
    boolean existsBySurveyIdAndUserEmail(Long surveyId, String userEmail);
}