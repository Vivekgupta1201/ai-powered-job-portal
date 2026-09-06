package com.jobportal.assessment.dto;

import com.jobportal.assessment.entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class QuestionResponse {
    private UUID id;
    private String questionText;
    private QuestionType questionType;
    private Integer marks;
    private Integer sequenceNumber;
    private List<OptionResponse> options;
}
