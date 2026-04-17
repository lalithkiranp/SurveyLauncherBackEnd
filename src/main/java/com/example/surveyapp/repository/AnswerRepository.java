package com.example.surveyapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.surveyapp.entity.Answer;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionId(Long questionId);
}