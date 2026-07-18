package com.jobportal.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponse {

    private String profileType;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Integer experienceYears;
    private String currentLocation;
    private String headline;
    private String summary;
    private String designation;
    private String companyName;
    private String companyWebsite;
    private String companyDescription;
}
