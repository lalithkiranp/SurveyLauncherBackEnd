package com.example.surveyapp.dto;

import java.util.Map;

public class QuestionStatsDTO {

    private String question;
    private String type;
    private Map<String, Long> distribution;
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String, Long> getDistribution() {
		return distribution;
	}
	public void setDistribution(Map<String, Long> distribution) {
		this.distribution = distribution;
	}
    
    // Getters and Setters
}