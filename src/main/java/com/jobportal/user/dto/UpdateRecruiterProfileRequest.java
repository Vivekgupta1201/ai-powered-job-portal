package com.jobportal.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UpdateRecruiterProfileRequest {
	@NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String designation;

    @NotBlank(message = "Company name is required")
    private String companyName;

    private String companyWebsite;
    private String companyDescription;
}
