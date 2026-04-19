package com.example.surveyapp.service;

import com.example.surveyapp.dto.*;
import com.example.surveyapp.entity.*;
import com.example.surveyapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.format.DateTimeFormatter;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;




@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;
    
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResponseRepository responseRepository;
    
    @Autowired
    private AnswerRepository answerRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a");

    @Value("${frontend.url}")
    private String frontendUrl;

    
    
    @Override
    public void createSurvey(SurveyDTO surveyDTO) {

        Survey survey = new Survey();
        survey.setTitle(surveyDTO.getTitle());
        survey.setDescription(surveyDTO.getDescription());
        survey.setActive(false);

        List<Question> questionList = new ArrayList<>();

        for (QuestionDTO qdto : surveyDTO.getQuestions()) {

            Question question = new Question();
            question.setQuestionText(qdto.getQuestionText());
            question.setType(qdto.getType());
            question.setSurvey(survey);

            List<Option> options = new ArrayList<>();

            if (qdto.getOptions() != null) {
                for (String opt : qdto.getOptions()) {
                    Option option = new Option();
                    option.setValue(opt);
                    option.setQuestion(question);
                    options.add(option);
                }
            }

            question.setOptions(options);
            questionList.add(question);
        }

        survey.setQuestions(questionList);

        surveyRepository.save(survey);
    }
    
    
    @Override
    public SurveyDTO getSurveyById(Long id) {

        Survey survey = surveyRepository.findById(id).orElseThrow();

        SurveyDTO dto = new SurveyDTO();
        dto.setTitle(survey.getTitle());
        dto.setDescription(survey.getDescription());

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question q : survey.getQuestions()) {

            QuestionDTO qdto = new QuestionDTO();
            qdto.setQuestionText(q.getQuestionText());
            qdto.setType(q.getType());

            List<String> options = new ArrayList<>();

            if (q.getOptions() != null) {
                for (Option opt : q.getOptions()) {
                    options.add(opt.getValue());
                }
            }

            qdto.setOptions(options);
            questionDTOList.add(qdto);
        }

        dto.setQuestions(questionDTOList);

        return dto;
    }
    
    
    @Override
    public void submitSurvey(Long surveyId, SubmitSurveyDTO dto) {
    	
        // ✅ STEP 6.3 → Required fields
        if (dto.getUserName() == null || dto.getUserName().isEmpty()) {
            throw new RuntimeException("User name is required");
        }

        if (dto.getUserEmail() == null || dto.getUserEmail().isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        if (dto.getAnswers() == null || dto.getAnswers().isEmpty()) {
            throw new RuntimeException("Answers are required");
        }

        // ✅ STEP 6.4 → Email format
        if (!dto.getUserEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RuntimeException("Invalid email format");
        }


        Survey survey = surveyRepository.findById(surveyId).orElseThrow();

        
        // ✅ Duplicate check (STEP 6.1)
        if (responseRepository.existsBySurveyIdAndUserEmail(surveyId, dto.getUserEmail())) {
            throw new RuntimeException("You have already submitted this survey");
        }

        
        
     // ✅ STEP 6.2 → Survey active check
        if (!survey.isActive()) {
            throw new RuntimeException("Survey is not active");
        }

        if (survey.getStartDate() != null && survey.getEndDate() != null) {
            if (java.time.LocalDateTime.now().isBefore(survey.getStartDate()) ||
                java.time.LocalDateTime.now().isAfter(survey.getEndDate())) {
                throw new RuntimeException("Survey is not within active date range");
            }
        }

       
        
        Response response = new Response();
        response.setUserName(dto.getUserName());
        response.setUserEmail(dto.getUserEmail());
        response.setSurvey(survey);
        response.setSubmittedAt(java.time.LocalDateTime.now());

        List<Answer> answerList = new ArrayList<>();

        for (AnswerDTO adto : dto.getAnswers()) {

            Question question = questionRepository.findById(adto.getQuestionId()).orElseThrow();

            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setResponse(response);
            answer.setAnswerText(adto.getAnswer());

            answerList.add(answer);
        }

        response.setAnswers(answerList);

        responseRepository.save(response);
        
        emailService.sendEmail(
        	    dto.getUserEmail(),
        	    "Survey Submitted Successfully",
        	    "Thank you " + dto.getUserName() + " for participating in the survey!"
        	);
    }
    
    
    
    @Override
    public void launchSurvey(Long surveyId, LaunchSurveyDTO dto) {

        Survey survey = surveyRepository.findById(surveyId).orElseThrow();
        String formattedEndDate = dto.getEndDate().format(formatter);

        //  Prevent re-launch
        if (survey.isActive()) {
            throw new RuntimeException("Survey is already active");
        }

        //  Validate dates
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new RuntimeException("Start date and end date are required");
        }

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }
        
        
        //  Set values
        survey.setStartDate(dto.getStartDate());
        survey.setEndDate(dto.getEndDate());
        survey.setActive(true);
        
        if (LocalDateTime.now().isAfter(survey.getEndDate())) {
            survey.setActive(false);										//can be removed
        }
        surveyRepository.save(survey);
        
        

        // ==========================
        // 📧 EMAIL INVITATION LOGIC
        // ==========================

        // 🔗 Generate survey link
       String surveyLink = frontendUrl + "/survey/" + surveyId;

        // 📥 Fetch employees
        List<Employee> employees = employeeRepository.findAll();

        // 📤 Send emails
        for (Employee emp : employees) {

            String subject = "Survey Invitation";

            String body = "Hello " + emp.getName() + ",\n\n"
                    + "You are invited to participate in a survey.\n\n"
                    + "Click below link:\n"
                    + surveyLink + "\n\n"
                    + "Valid till: " + formattedEndDate;

            emailService.sendEmail(emp.getEmail(), subject, body);
        }
    }
    
    
    @Override
    public StatsResponseDTO getSurveyStats(Long surveyId) {

        long totalResponses = responseRepository.countBySurveyId(surveyId);
        
        if (totalResponses == 0) {
            StatsResponseDTO emptyResponse = new StatsResponseDTO();
            emptyResponse.setTotalResponses(0);
            emptyResponse.setQuestions(new ArrayList<>());
            return emptyResponse;
        }

        List<Question> questions = questionRepository.findBySurveyId(surveyId);

        List<QuestionStatsDTO> statsList = new ArrayList<>();

        for (Question question : questions) {

            List<Answer> answers = answerRepository.findByQuestionId(question.getId());

            Map<String, Long> distribution = answers.stream()
                    .collect(Collectors.groupingBy(
                            Answer::getAnswerText,
                            Collectors.counting()
                    ));

            QuestionStatsDTO qdto = new QuestionStatsDTO();
            qdto.setQuestion(question.getQuestionText());
            qdto.setType(question.getType());
            qdto.setDistribution(distribution);

            statsList.add(qdto);
        }

        StatsResponseDTO response = new StatsResponseDTO();
        response.setTotalResponses(totalResponses);
        response.setQuestions(statsList);

        return response;
    }
    
    @Override
    public byte[] exportSurvey(Long surveyId) {

        List<Response> responses = responseRepository.findAll();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        // Header
        writer.print("User Name,Email,Submitted At");

        List<Question> questions = questionRepository.findBySurveyId(surveyId);
        for (Question q : questions) {
            writer.print("," + q.getQuestionText());
        }
        writer.println();

        // Data rows
        for (Response r : responses) {

            if (!r.getSurvey().getId().equals(surveyId)) continue;

            writer.print(r.getUserName() + "," + r.getUserEmail() + "," + r.getSubmittedAt());

            for (Question q : questions) {

                String answerText = "";

                for (Answer a : r.getAnswers()) {
                    if (a.getQuestion().getId().equals(q.getId())) {
                        answerText = a.getAnswerText();
                        break;
                    }
                }

                writer.print("," + answerText);
            }

            writer.println();
        }

        writer.flush();
        return out.toByteArray();
    }
    
    @Override
    public void deleteSurvey(Long id) {

        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        surveyRepository.delete(survey);
     }
    
    @Override
    public List<AdminSurveyDashboardDTO> getAllSurveysForAdmin() {

        List<Survey> surveys = surveyRepository.findAll();

        List<AdminSurveyDashboardDTO> result = new ArrayList<>();

        for (Survey s : surveys) {

            AdminSurveyDashboardDTO dto = new AdminSurveyDashboardDTO();

            dto.setId(s.getId());
            dto.setTitle(s.getTitle());
            dto.setDescription(s.getDescription());
            dto.setStartDate(s.getStartDate());
            dto.setEndDate(s.getEndDate());
            dto.setActive(s.isActive());

            result.add(dto);
        }

        return result;
    }
    
}
