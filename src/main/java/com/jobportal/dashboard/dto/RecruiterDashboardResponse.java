package com.jobportal.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecruiterDashboardResponse {
    private long totalJobs;
    private long draftJobs;
    private long publishedJobs;
    private long closedJobs;
    private long totalApplicants;
    private long shortlistedApplicants;
    private long rejectedApplicants;
    private long appliedApplicants;
}
