package com.example.surveyapp.dto;

import java.util.List;
import jakarta.validation.constraints.*;
public class SubmitSurveyDTO {
	
	@NotBlank(message = "Name is required")
    private String userName;
	
	@NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String userEmail;
	
	@NotEmpty(message = "Answers cannot be empty")
    private List<AnswerDTO> answers;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public List<AnswerDTO> getAnswers() {
		return answers;
	}
	public void setAnswers(List<AnswerDTO> answers) {
		this.answers = answers;
	}

    // Getters and Setters
    
}