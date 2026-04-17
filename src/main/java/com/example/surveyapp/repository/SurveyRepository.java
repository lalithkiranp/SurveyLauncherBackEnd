package com.example.surveyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.surveyapp.entity.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}