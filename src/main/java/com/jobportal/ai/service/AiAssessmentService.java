package com.jobportal.ai.service;

import com.jobportal.ai.dto.GenerateAssessmentRequest;
import com.jobportal.ai.dto.GeneratedAssessmentResponse;

public interface AiAssessmentService {
    GeneratedAssessmentResponse generateAssessment(String recruiterEmail, GenerateAssessmentRequest request);
}
