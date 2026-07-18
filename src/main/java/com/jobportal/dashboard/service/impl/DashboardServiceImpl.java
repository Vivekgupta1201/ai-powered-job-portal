package com.jobportal.dashboard.service.impl;

import com.jobportal.application.entity.ApplicationStatus;
import com.jobportal.application.repository.ApplicationRepository;
import com.jobportal.auth.entity.User;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.dashboard.dto.RecruiterDashboardResponse;
import com.jobportal.dashboard.service.DashboardService;
import com.jobportal.job.entity.JobStatus;
import com.jobportal.job.repository.JobRepository;
import com.jobportal.user.repository.RecruiterProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public RecruiterDashboardResponse recruiterDashboard(String email) {
        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var recruiterProfile = recruiterProfileRepository.findByUserId(recruiter.getId())
                .orElseThrow(() -> new IllegalArgumentException("Recruiter profile not found"));

        long totalJobs = jobRepository.findByRecruiterId(recruiter.getId()).size();
        long draftJobs = jobRepository.findByRecruiterId(recruiter.getId()).stream().filter(j -> j.getStatus() == JobStatus.DRAFT).count();
        long publishedJobs = jobRepository.findByRecruiterId(recruiter.getId()).stream().filter(j -> j.getStatus() == JobStatus.PUBLISHED).count();
        long closedJobs = jobRepository.findByRecruiterId(recruiter.getId()).stream().filter(j -> j.getStatus() == JobStatus.CLOSED).count();
        long totalApplicants = jobRepository.findByRecruiterId(recruiter.getId()).stream()
                .mapToLong(job -> applicationRepository.findByJobId(job.getId()).size())
                .sum();
        long shortlistedApplicants = jobRepository.findByRecruiterId(recruiter.getId()).stream()
                .mapToLong(job -> applicationRepository.findByJobId(job.getId()).stream()
                        .filter(app -> app.getStatus() == ApplicationStatus.SHORTLISTED)
                        .count())
                .sum();
        long rejectedApplicants = jobRepository.findByRecruiterId(recruiter.getId()).stream()
                .mapToLong(job -> applicationRepository.findByJobId(job.getId()).stream()
                        .filter(app -> app.getStatus() == ApplicationStatus.REJECTED)
                        .count())
                .sum();
        long appliedApplicants = jobRepository.findByRecruiterId(recruiter.getId()).stream()
                .mapToLong(job -> applicationRepository.findByJobId(job.getId()).stream()
                        .filter(app -> app.getStatus() == ApplicationStatus.APPLIED)
                        .count())
                .sum();

        return new RecruiterDashboardResponse(
                totalJobs,
                draftJobs,
                publishedJobs,
                closedJobs,
                totalApplicants,
                shortlistedApplicants,
                rejectedApplicants,
                appliedApplicants
        );
    }
}
