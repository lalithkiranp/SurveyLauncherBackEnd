package com.example.surveyapp.dto;

import java.util.List;

public class StatsResponseDTO {

    private long totalResponses;
    private List<QuestionStatsDTO> questions;
	public long getTotalResponses() {
		return totalResponses;
	}
	public void setTotalResponses(long totalResponses) {
		this.totalResponses = totalResponses;
	}
	public List<QuestionStatsDTO> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionStatsDTO> questions) {
		this.questions = questions;
	}

   
}