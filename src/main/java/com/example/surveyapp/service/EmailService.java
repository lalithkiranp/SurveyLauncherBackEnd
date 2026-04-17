package com.example.surveyapp.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}