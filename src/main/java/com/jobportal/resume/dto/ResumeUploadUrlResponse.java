package com.jobportal.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResumeUploadUrlResponse {
    private String uploadPath;
    private String storageKey;
}
