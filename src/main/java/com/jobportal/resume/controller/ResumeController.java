package com.jobportal.resume.controller;

import com.jobportal.resume.dto.ResumeResponse;
import com.jobportal.resume.dto.ResumeUploadResponse;
import com.jobportal.resume.dto.ResumeUploadUrlResponse;
import com.jobportal.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/upload-target")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ResumeUploadUrlResponse> createUploadTarget(Authentication authentication,
                                                                       @RequestParam String fileName) {
        return ResponseEntity.ok(resumeService.createUploadTarget(authentication.getName(), fileName));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ResumeUploadResponse> upload(Authentication authentication,
                                                       @RequestPart MultipartFile file,
                                                       @RequestParam String storageKey) {
        return ResponseEntity.ok(resumeService.saveResume(authentication.getName(), file, storageKey));
    }

    @GetMapping
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<List<ResumeResponse>> list(Authentication authentication) {
        return ResponseEntity.ok(resumeService.listMyResumes(authentication.getName()));
    }

    @PatchMapping("/{resumeId}/activate")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ResumeResponse> activate(Authentication authentication, @PathVariable UUID resumeId) {
        return ResponseEntity.ok(resumeService.activateResume(authentication.getName(), resumeId));
    }

    @GetMapping("/{resumeId}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ResumeResponse> get(Authentication authentication, @PathVariable UUID resumeId) {
        return ResponseEntity.ok(resumeService.getResume(authentication.getName(), resumeId));
    }

    @GetMapping("/{resumeId}/download")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'RECRUITER')")
    public ResponseEntity<org.springframework.core.io.Resource> download(Authentication authentication,
                                                                           @PathVariable UUID resumeId) {
        ResumeService.ResumeFile file = resumeService.downloadResume(authentication.getName(), resumeId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.fileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, file.contentType())
                .body(file.resource());
    }
}
