package com.jobportal.ai.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeneratedOptionResponse {
    private String optionText;
    private boolean correct;
}
