package com.jobportal.job.dto;

import com.jobportal.job.entity.EmploymentType;
import com.jobportal.job.entity.JobStatus;
import com.jobportal.job.entity.RemoteType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class JobResponse {
    private UUID id;
    private String title;
    private String description;
    private EmploymentType employmentType;
    private Integer experienceMin;
    private Integer experienceMax;
    private String location;
    private RemoteType remoteType;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private JobStatus status;
    private LocalDateTime applicationDeadline;
    private String companyName;
    private String recruiterEmail;
}
