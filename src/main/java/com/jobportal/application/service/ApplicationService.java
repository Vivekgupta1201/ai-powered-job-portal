package com.jobportal.application.service;

import com.jobportal.application.dto.ApplyRequest;
import com.jobportal.application.dto.ApplicationResponse;
import com.jobportal.application.dto.ApplicationSummaryResponse;
import com.jobportal.application.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ApplicationService {
    ApplicationResponse applyToJob(String email, UUID jobId, ApplyRequest request);
    Page<ApplicationSummaryResponse> myApplications(String email, Pageable pageable);
    Page<ApplicationSummaryResponse> recruiterApplicants(String email, UUID jobId, Pageable pageable);
    ApplicationResponse changeStatus(String email, UUID applicationId, ApplicationStatus status, String remarks);
}
