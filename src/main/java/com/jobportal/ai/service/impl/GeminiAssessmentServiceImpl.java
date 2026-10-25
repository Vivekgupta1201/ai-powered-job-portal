package com.jobportal.ai.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.ai.config.GeminiProperties;
import com.jobportal.ai.dto.GenerateAssessmentRequest;
import com.jobportal.ai.dto.GeneratedAssessmentResponse;
import com.jobportal.ai.dto.GeneratedOptionResponse;
import com.jobportal.ai.dto.GeneratedQuestionResponse;
import com.jobportal.ai.service.AiAssessmentService;
import com.jobportal.application.entity.Application;
import com.jobportal.application.entity.ApplicationStatus;
import com.jobportal.application.repository.ApplicationRepository;
import com.jobportal.auth.entity.User;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.common.exception.ConflictException;
import com.jobportal.common.exception.NotFoundException;
import com.jobportal.resume.entity.Resume;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiAssessmentServiceImpl implements AiAssessmentService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final GeminiProperties geminiProperties;
    private final ObjectMapper objectMapper;

    @Value("${app.resume.upload-dir:uploads/resumes}")
    private String uploadDir;

    @Override
    public GeneratedAssessmentResponse generateAssessment(String recruiterEmail, GenerateAssessmentRequest request) {
        if (geminiProperties.getApiKey() == null || geminiProperties.getApiKey().isBlank()) {
            throw new ConflictException("Gemini API key is not configured. Set GEMINI_API_KEY before generating questions.");
        }

        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new NotFoundException("Application not found"));
        if (!application.getJob().getRecruiter().getId().equals(recruiter.getId())) {
            throw new ConflictException("You are not allowed to generate an assessment for this application");
        }
        if (application.getStatus() != ApplicationStatus.SHORTLISTED) {
            throw new ConflictException("Candidate must be shortlisted before generating an assessment");
        }

        Resume resume = application.getResume();
        ResumeDocument document = readResume(resume);
        Map<String, Object> requestBody = buildGeminiRequest(application, document, request.getQuestionCount());

        try {
            String response = RestClient.create()
                    .post()
                    .uri("https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent", geminiProperties.getModel())
                    .header("x-goog-api-key", geminiProperties.getApiKey())
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);
            return parseAndValidate(response);
        } catch (RestClientResponseException exception) {
            throw new IllegalStateException("Gemini request failed with HTTP "
                    + exception.getStatusCode().value() + ": " + providerMessage(exception), exception);
        } catch (RestClientException exception) {
            throw new IllegalStateException("Gemini could not generate an assessment. Check the API key, model, and network connection.", exception);
        }
    }

    private ResumeDocument readResume(Resume resume) {
        if (resume.getContentType() == null || !resume.getContentType().equalsIgnoreCase("application/pdf")) {
            throw new ConflictException("AI question generation currently supports PDF resumes only");
        }
        Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path path = root.resolve(resume.getStorageKey()).normalize();
        if (!path.startsWith(root) || !Files.exists(path)) {
            throw new NotFoundException("Resume file not found");
        }
        try {
            byte[] content = Files.readAllBytes(path);
            if (content.length > geminiProperties.getMaxResumeBytes()) {
                throw new ConflictException("Resume is too large for AI generation");
            }
            return new ResumeDocument(resume.getContentType(), Base64.getEncoder().encodeToString(content));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read resume for AI generation", exception);
        }
    }

    private Map<String, Object> buildGeminiRequest(Application application, ResumeDocument document, int questionCount) {
        String prompt = "Generate " + questionCount + " fair, job-relevant multiple-choice technical questions. "
                + "Use the attached candidate resume and this job information. "
                + "Do not ask for protected personal information. "
                + "Every question must have exactly four options and exactly one correct option. "
                + "Use marks between 1 and 10. "
                + "Job title: " + application.getJob().getTitle() + "\n"
                + "Job description: " + application.getJob().getDescription();

        Map<String, Object> questionSchema = Map.of(
                "type", "object",
                "properties", Map.of(
                        "questionText", Map.of("type", "string"),
                        "marks", Map.of("type", "integer"),
                        "options", Map.of("type", "array", "items", Map.of("type", "object", "properties", Map.of(
                                "optionText", Map.of("type", "string"),
                                "correct", Map.of("type", "boolean")), "required", List.of("optionText", "correct")))),
                "required", List.of("questionText", "marks", "options"));
        Map<String, Object> schema = Map.of(
                "type", "object",
                "properties", Map.of(
                        "title", Map.of("type", "string"),
                        "durationMinutes", Map.of("type", "integer"),
                        "passPercentage", Map.of("type", "integer"),
                        "questions", Map.of("type", "array", "items", questionSchema)),
                "required", List.of("title", "durationMinutes", "passPercentage", "questions"));

        Map<String, Object> textPart = Map.of("text", prompt);
        Map<String, Object> documentPart = Map.of("inlineData", Map.of("mimeType", document.mimeType(), "data", document.base64Data()));
        Map<String, Object> generationConfig = new LinkedHashMap<>();
        generationConfig.put("responseMimeType", "application/json");
        generationConfig.put("responseJsonSchema", schema);
        generationConfig.put("temperature", 0.3);
        return Map.of("contents", List.of(Map.of("parts", List.of(textPart, documentPart))), "generationConfig", generationConfig);
    }

    private GeneratedAssessmentResponse parseAndValidate(JsonNode response) {
        if (response == null) {
            throw new IllegalStateException("Gemini returned an empty response");
        }
        String generatedJson = response.at("/candidates/0/content/parts/0/text").asText();
        if (generatedJson.isBlank()) {
            throw new IllegalStateException("Gemini did not return assessment questions");
        }
        try {
            GeneratedAssessmentResponse generated = objectMapper.readValue(generatedJson, GeneratedAssessmentResponse.class);
            if (generated.getQuestions() == null || generated.getQuestions().isEmpty()) {
                throw new IllegalStateException("Gemini returned no questions");
            }
            for (GeneratedQuestionResponse question : generated.getQuestions()) {
                boolean valid = question.getQuestionText() != null && !question.getQuestionText().isBlank()
                        && question.getMarks() != null && question.getMarks() > 0
                        && question.getOptions() != null && question.getOptions().size() == 4
                        && question.getOptions().stream().filter(GeneratedOptionResponse::isCorrect).count() == 1;
                if (!valid) {
                    throw new IllegalStateException("Gemini returned an invalid question format");
                }
            }
            return generated;
        } catch (IOException exception) {
            throw new IllegalStateException("Gemini returned invalid JSON", exception);
        }
    }

    private GeneratedAssessmentResponse parseAndValidate(String responseBody) {
        try {
            return parseAndValidate(objectMapper.readTree(responseBody));
        } catch (IOException exception) {
            throw new IllegalStateException("Gemini returned invalid JSON", exception);
        }
    }

    private String providerMessage(RestClientResponseException exception) {
        try {
            String message = objectMapper.readTree(exception.getResponseBodyAsString())
                    .at("/error/message")
                    .asText();
            return message.isBlank() ? "Check the API key, model, quota, and Gemini API access." : message;
        } catch (IOException ignored) {
            return "Check the API key, model, quota, and Gemini API access.";
        }
    }

    private record ResumeDocument(String mimeType, String base64Data) { }
}
