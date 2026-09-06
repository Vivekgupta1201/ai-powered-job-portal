package com.jobportal.assessment.dto;

import com.jobportal.assessment.entity.AttemptStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class AssessmentResultResponse {
    private UUID attemptId;
    private UUID assessmentId;
    private AttemptStatus status;
    private BigDecimal score;
    private Integer totalMarks;
    private BigDecimal percentage;
    private Boolean passed;
}
