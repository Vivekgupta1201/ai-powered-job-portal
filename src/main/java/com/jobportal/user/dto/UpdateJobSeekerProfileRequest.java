package com.jobportal.user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateJobSeekerProfileRequest {
	
	@NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String phone;

    @Min(value = 0, message = "Experience years cannot be negative")
    private Integer experienceYears;

    private String currentLocation;
    private String headline;
    private String summary;

}
