package com.jobportal.ai.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GeneratedQuestionResponse {
    private String questionText;
    private Integer marks;
    private List<GeneratedOptionResponse> options;
}
