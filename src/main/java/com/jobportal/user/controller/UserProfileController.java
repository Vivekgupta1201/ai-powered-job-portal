package com.jobportal.user.controller;

import com.jobportal.user.dto.CreateJobSeekerProfileRequest;
import com.jobportal.user.dto.CreateRecruiterProfileRequest;
import com.jobportal.user.dto.ProfileResponse;
import com.jobportal.user.dto.UpdateJobSeekerProfileRequest;
import com.jobportal.user.dto.UpdateRecruiterProfileRequest;
import com.jobportal.user.dto.UserProfileResponse;
import com.jobportal.user.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/job-seeker")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ProfileResponse> createJobSeekerProfile(
            Authentication authentication,
            @Valid @RequestBody CreateJobSeekerProfileRequest request) {

        String email = authentication.getName();
        ProfileResponse response = userProfileService.createJobSeekerProfile(email, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/recruiter")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ProfileResponse> createRecruiterProfile(
            Authentication authentication,
            @Valid @RequestBody CreateRecruiterProfileRequest request) {

        String email = authentication.getName();
        ProfileResponse response = userProfileService.createRecruiterProfile(email, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        UserProfileResponse response = userProfileService.getMyProfile(email);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/job-seeker")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ProfileResponse> updateJobSeekerProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateJobSeekerProfileRequest request) {

        String email = authentication.getName();
        ProfileResponse response = userProfileService.updateJobSeekerProfile(email, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/recruiter")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ProfileResponse> updateRecruiterProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateRecruiterProfileRequest request) {

        String email = authentication.getName();
        ProfileResponse response = userProfileService.updateRecruiterProfile(email, request);
        return ResponseEntity.ok(response);
    }  
}
