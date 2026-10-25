package com.jobportal.ai.controller;

import com.jobportal.ai.dto.GenerateAssessmentRequest;
import com.jobportal.ai.dto.GeneratedAssessmentResponse;
import com.jobportal.ai.service.AiAssessmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/assessments")
@RequiredArgsConstructor
public class AiAssessmentController {
    private final AiAssessmentService aiAssessmentService;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<GeneratedAssessmentResponse> generate(Authentication authentication,
                                                                 @Valid @RequestBody GenerateAssessmentRequest request) {
        return ResponseEntity.ok(aiAssessmentService.generateAssessment(authentication.getName(), request));
    }
}
