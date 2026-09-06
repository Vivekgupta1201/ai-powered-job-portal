package com.jobportal.assessment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SubmitAssessmentRequest {
    @NotEmpty
    @Valid
    private List<AnswerRequest> answers;

    @Getter
    @Setter
    public static class AnswerRequest {
        @NotNull
        private UUID questionId;

        private UUID selectedOptionId;
        private String answerText;
    }
}
