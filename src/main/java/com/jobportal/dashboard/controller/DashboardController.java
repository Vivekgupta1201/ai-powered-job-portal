package com.jobportal.dashboard.controller;

import com.jobportal.dashboard.dto.RecruiterDashboardResponse;
import com.jobportal.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/recruiter")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<RecruiterDashboardResponse> recruiter(Authentication authentication) {
        return ResponseEntity.ok(dashboardService.recruiterDashboard(authentication.getName()));
    }
}
