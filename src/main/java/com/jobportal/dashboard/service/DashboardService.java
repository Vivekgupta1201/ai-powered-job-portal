package com.jobportal.dashboard.service;

import com.jobportal.dashboard.dto.RecruiterDashboardResponse;

public interface DashboardService {
    RecruiterDashboardResponse recruiterDashboard(String email);
}
