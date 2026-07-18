package com.jobportal.application.dto;

import com.jobportal.application.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ApplicationResponse {
    private UUID id;
    private String message;
    private ApplicationStatus status;
    private String jobTitle;
    private String companyName;
    private String seekerEmail;
}
