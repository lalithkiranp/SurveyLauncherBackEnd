package com.example.surveyapp.dto;

import java.util.List;

public class QuestionDTO {

    private String questionText;
    private String type;
    private List<String> options;
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}

    // Getters and Setters
    
}