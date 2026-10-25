package com.jobportal.ai.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GenerateAssessmentRequest {
    @NotNull
    private UUID applicationId;

    @Min(1)
    @Max(10)
    private Integer questionCount = 5;
}
