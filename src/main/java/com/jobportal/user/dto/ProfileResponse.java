package com.jobportal.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponse {

    private String message;
    private String email;
    private String profileType;
}
