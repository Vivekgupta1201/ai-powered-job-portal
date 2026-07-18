package com.jobportal.application.controller;

import com.jobportal.application.dto.ApplyRequest;
import com.jobportal.application.dto.ApplicationResponse;
import com.jobportal.application.dto.ApplicationSummaryResponse;
import com.jobportal.application.entity.ApplicationStatus;
import com.jobportal.application.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/jobs/{jobId}/apply")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ApplicationResponse> apply(Authentication authentication,
                                                     @PathVariable UUID jobId,
                                                     @Valid @RequestBody ApplyRequest request) {
        return ResponseEntity.ok(applicationService.applyToJob(authentication.getName(), jobId, request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<Page<ApplicationSummaryResponse>> myApplications(Authentication authentication,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(applicationService.myApplications(authentication.getName(), pageable));
    }

    @GetMapping("/recruiter/jobs/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Page<ApplicationSummaryResponse>> recruiterApplicants(Authentication authentication,
                                                                                @PathVariable UUID jobId,
                                                                                @RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(applicationService.recruiterApplicants(authentication.getName(), jobId, pageable));
    }

    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApplicationResponse> changeStatus(Authentication authentication,
                                                            @PathVariable UUID applicationId,
                                                            @RequestParam ApplicationStatus status,
                                                            @RequestParam(required = false) String remarks) {
        return ResponseEntity.ok(applicationService.changeStatus(authentication.getName(), applicationId, status, remarks));
    }
}
