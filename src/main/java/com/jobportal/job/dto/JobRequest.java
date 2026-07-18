package com.jobportal.job.dto;

import com.jobportal.job.entity.EmploymentType;
import com.jobportal.job.entity.RemoteType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class JobRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private EmploymentType employmentType;

    private Integer experienceMin;
    private Integer experienceMax;
    private String location;
    private RemoteType remoteType;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private LocalDateTime applicationDeadline;
}
