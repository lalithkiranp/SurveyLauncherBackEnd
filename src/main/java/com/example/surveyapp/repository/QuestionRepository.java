package com.example.surveyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.surveyapp.entity.Question;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySurveyId(Long surveyId);
}