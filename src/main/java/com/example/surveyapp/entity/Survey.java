package com.example.surveyapp.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

  
	private LocalDateTime startDate;
    private LocalDateTime endDate;

    private boolean isActive;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<Question> questions;

    // Getters and Setters
    public Long getId() {
  		return id;
  	}

  	public void setId(Long id) {
  		this.id = id;
  	}

  	public String getTitle() {
  		return title;
  	}

  	public void setTitle(String title) {
  		this.title = title;
  	}

  	public String getDescription() {
  		return description;
  	}

  	public void setDescription(String description) {
  		this.description = description;
  	}

  	public LocalDateTime getStartDate() {
  		return startDate;
  	}

  	public void setStartDate(LocalDateTime startDate) {
  		this.startDate = startDate;
  	}

  	public LocalDateTime getEndDate() {
  		return endDate;
  	}

  	public void setEndDate(LocalDateTime endDate) {
  		this.endDate = endDate;
  	}

  	public boolean isActive() {
  		return isActive;
  	}

  	public void setActive(boolean isActive) {
  		this.isActive = isActive;
  	}

  	public List<Question> getQuestions() {
  		return questions;
  	}

  	public void setQuestions(List<Question> questions) {
  		this.questions = questions;
  	}

}