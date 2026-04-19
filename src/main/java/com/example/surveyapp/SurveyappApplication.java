package com.example.surveyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SurveyappApplication {
	public static void main(String[] args) {
		SpringApplication.run(SurveyappApplication.class, args);
		System.out.println("DB_URL=" + System.getenv("DB_URL"));
	}

}
