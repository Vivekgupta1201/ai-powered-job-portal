package com.jobportal.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ResumeUploadResponse {
    private UUID id;
    private String message;
    private String fileName;
    private String storageKey;
}
