package com.jobportal.assessment.dto;

import com.jobportal.assessment.entity.AssessmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class AssessmentResponse {
    private UUID id;
    private UUID applicationId;
    private String title;
    private Integer durationMinutes;
    private BigDecimal passPercentage;
    private AssessmentStatus status;
    private LocalDateTime dueAt;
    private List<QuestionResponse> questions;
}
