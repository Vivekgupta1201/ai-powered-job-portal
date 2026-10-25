package com.jobportal.ai.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GeneratedAssessmentResponse {
    private String title;
    private Integer durationMinutes;
    private Integer passPercentage;
    private List<GeneratedQuestionResponse> questions;
}
