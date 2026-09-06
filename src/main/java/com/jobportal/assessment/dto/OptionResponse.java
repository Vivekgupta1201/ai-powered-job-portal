package com.jobportal.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class OptionResponse {
    private UUID id;
    private String optionText;
}
