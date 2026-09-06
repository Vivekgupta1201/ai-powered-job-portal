package com.jobportal.assessment.service;

import com.jobportal.assessment.dto.AssessmentResponse;
import com.jobportal.assessment.dto.AssessmentResultResponse;
import com.jobportal.assessment.dto.CreateAssessmentRequest;
import com.jobportal.assessment.dto.SubmitAssessmentRequest;

import java.util.UUID;

public interface AssessmentService {
    AssessmentResponse create(String recruiterEmail, CreateAssessmentRequest request);
    AssessmentResponse publish(String recruiterEmail, UUID assessmentId);
    AssessmentResponse getForUser(String email, UUID assessmentId);
    AssessmentResponse getForApplication(String email, UUID applicationId);
    AssessmentResultResponse start(String candidateEmail, UUID assessmentId);
    AssessmentResultResponse submit(String candidateEmail, UUID attemptId, SubmitAssessmentRequest request);
    AssessmentResultResponse result(String email, UUID assessmentId);
}
