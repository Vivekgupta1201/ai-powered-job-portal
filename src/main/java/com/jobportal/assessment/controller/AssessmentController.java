package com.jobportal.assessment.controller;

import com.jobportal.assessment.dto.*;
import com.jobportal.assessment.service.AssessmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentController {
    private final AssessmentService assessmentService;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<AssessmentResponse> create(Authentication authentication,
                                                       @Valid @RequestBody CreateAssessmentRequest request) {
        return ResponseEntity.ok(assessmentService.create(authentication.getName(), request));
    }

    @PatchMapping("/{assessmentId}/publish")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<AssessmentResponse> publish(Authentication authentication,
                                                        @PathVariable UUID assessmentId) {
        return ResponseEntity.ok(assessmentService.publish(authentication.getName(), assessmentId));
    }

    @GetMapping("/{assessmentId}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    public ResponseEntity<AssessmentResponse> get(Authentication authentication,
                                                    @PathVariable UUID assessmentId) {
        return ResponseEntity.ok(assessmentService.getForUser(authentication.getName(), assessmentId));
    }

    @GetMapping("/application/{applicationId}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    public ResponseEntity<AssessmentResponse> getForApplication(Authentication authentication,
                                                                  @PathVariable UUID applicationId) {
        return ResponseEntity.ok(assessmentService.getForApplication(authentication.getName(), applicationId));
    }

    @PostMapping("/{assessmentId}/start")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<AssessmentResultResponse> start(Authentication authentication,
                                                            @PathVariable UUID assessmentId) {
        return ResponseEntity.ok(assessmentService.start(authentication.getName(), assessmentId));
    }

    @PostMapping("/attempts/{attemptId}/submit")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<AssessmentResultResponse> submit(Authentication authentication,
                                                            @PathVariable UUID attemptId,
                                                            @Valid @RequestBody SubmitAssessmentRequest request) {
        return ResponseEntity.ok(assessmentService.submit(authentication.getName(), attemptId, request));
    }

    @GetMapping("/{assessmentId}/result")
    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    public ResponseEntity<AssessmentResultResponse> result(Authentication authentication,
                                                            @PathVariable UUID assessmentId) {
        return ResponseEntity.ok(assessmentService.result(authentication.getName(), assessmentId));
    }
}
