package com.jobportal.resume.service;

import com.jobportal.resume.dto.ResumeResponse;
import com.jobportal.resume.dto.ResumeUploadResponse;
import com.jobportal.resume.dto.ResumeUploadUrlResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;

public interface ResumeService {
    ResumeUploadUrlResponse createUploadTarget(String email, String fileName);
    ResumeUploadResponse saveResume(String email, MultipartFile file, String storageKey);
    List<ResumeResponse> listMyResumes(String email);
    ResumeResponse activateResume(String email, UUID resumeId);
    ResumeResponse getResume(String email, UUID resumeId);
    ResumeFile downloadResume(String email, UUID resumeId);

    record ResumeFile(Resource resource, String fileName, String contentType) { }
}
