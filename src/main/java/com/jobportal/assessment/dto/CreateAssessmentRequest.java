package com.jobportal.assessment.dto;

import com.jobportal.assessment.entity.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateAssessmentRequest {
    @NotNull
    private UUID applicationId;

    @NotBlank
    private String title;

    @NotNull
    @Min(1)
    private Integer durationMinutes;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private BigDecimal passPercentage;

    @NotEmpty
    @Valid
    private List<QuestionRequest> questions;

    @Getter
    @Setter
    public static class QuestionRequest {
        @NotBlank
        private String questionText;

        @NotNull
        private QuestionType questionType;

        @NotNull
        @Min(1)
        private Integer marks;

        @NotEmpty
        @Valid
        private List<OptionRequest> options;
    }

    @Getter
    @Setter
    public static class OptionRequest {
        @NotBlank
        private String optionText;

        private boolean correct;
    }
}
