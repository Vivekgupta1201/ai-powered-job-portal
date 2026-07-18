package com.jobportal.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ResumeResponse {
    private UUID id;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private Boolean active;
    private String uploadedAt;
    private String storageKey;
}
