package com.jobportal.application.dto;

import com.jobportal.application.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ApplicationSummaryResponse {
    private UUID id;
    private String jobTitle;
    private String companyName;
    private String seekerEmail;
    private ApplicationStatus status;
    private String coverLetter;
}
