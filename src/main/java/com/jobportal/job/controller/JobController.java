package com.jobportal.job.controller;

import com.jobportal.job.dto.JobRequest;
import com.jobportal.job.dto.JobResponse;
import com.jobportal.job.dto.JobSummaryResponse;
import com.jobportal.job.service.JobService;
import com.jobportal.user.dto.CreateJobSeekerProfileRequest;
import com.jobportal.user.dto.ProfileResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponse> createJob(Authentication authentication, @Valid @RequestBody JobRequest request) {
         
    	
    	return ResponseEntity.ok(jobService.createJob(authentication.getName(), request));
    }
    
    
    
   
    
    
    

    @PutMapping("/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponse> updateJob(Authentication authentication, @PathVariable UUID jobId, @Valid @RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.updateJob(authentication.getName(), jobId, request));
    }

    @PatchMapping("/{jobId}/publish")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponse> publishJob(Authentication authentication, @PathVariable UUID jobId) {
        return ResponseEntity.ok(jobService.publishJob(authentication.getName(), jobId));
    }

    @PatchMapping("/{jobId}/close")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponse> closeJob(Authentication authentication, @PathVariable UUID jobId) {
        return ResponseEntity.ok(jobService.closeJob(authentication.getName(), jobId));
    }

    @GetMapping
    public ResponseEntity<Page<JobSummaryResponse>> listJobs(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(jobService.listJobs(query, pageable));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJob(@PathVariable UUID jobId) {
        return ResponseEntity.ok(jobService.getJobById(jobId));
    }

    @GetMapping("/recruiter/my-jobs")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Page<JobSummaryResponse>> myJobs(Authentication authentication,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(jobService.listRecruiterJobs(authentication.getName(), pageable));
    }
}
