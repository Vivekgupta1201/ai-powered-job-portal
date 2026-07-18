package com.jobportal.application.service.impl;

import com.jobportal.application.dto.ApplyRequest;
import com.jobportal.application.dto.ApplicationResponse;
import com.jobportal.application.dto.ApplicationSummaryResponse;
import com.jobportal.application.entity.Application;
import com.jobportal.application.entity.ApplicationStatus;
import com.jobportal.application.entity.ApplicationStatusHistory;
import com.jobportal.application.repository.ApplicationRepository;
import com.jobportal.application.repository.ApplicationStatusHistoryRepository;
import com.jobportal.application.service.ApplicationService;
import com.jobportal.auth.entity.User;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.common.exception.ConflictException;
import com.jobportal.common.exception.NotFoundException;
import com.jobportal.job.entity.Job;
import com.jobportal.job.entity.JobStatus;
import com.jobportal.job.repository.JobRepository;
import com.jobportal.resume.entity.Resume;
import com.jobportal.resume.repository.ResumeRepository;
import com.jobportal.user.repository.RecruiterProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationStatusHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    @Override
    @Transactional
    public ApplicationResponse applyToJob(String email, java.util.UUID jobId, ApplyRequest request) {
        User seeker = findUser(email);
        Job job = findJob(jobId);
        if (job.getStatus() != JobStatus.PUBLISHED) {
            throw new ConflictException("Job is not open for application");
        }
        if (applicationRepository.existsByJobIdAndJobSeekerId(jobId, seeker.getId())) {
            throw new ConflictException("You have already applied for this job");
        }
        Resume resume = resumeRepository.findByIdAndJobSeekerId(request.getResumeId(), seeker.getId())
                .orElseThrow(() -> new NotFoundException("Resume not found"));

        Application application = new Application();
        application.setJob(job);
        application.setJobSeeker(seeker);
        application.setResume(resume);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setCoverLetter(request.getCoverLetter());

        Application saved = applicationRepository.save(application);
        saveHistory(saved, null, ApplicationStatus.APPLIED.name(), seeker, "Application submitted");
        return toResponse(saved);
    }

    @Override
    public Page<ApplicationSummaryResponse> myApplications(String email, Pageable pageable) {
        User seeker = findUser(email);
        return applicationRepository.findByJobSeekerId(seeker.getId()).stream()
                .map(this::toSummary)
                .collect(java.util.stream.Collectors.collectingAndThen(java.util.stream.Collectors.toList(), list -> new org.springframework.data.domain.PageImpl<>(list, pageable, list.size())));
    }

    @Override
    public Page<ApplicationSummaryResponse> recruiterApplicants(String email, java.util.UUID jobId, Pageable pageable) {
        User recruiter = findUser(email);
        Job job = findJob(jobId);
        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new ConflictException("You are not allowed to view these applicants");
        }
        return applicationRepository.findByJobId(jobId).stream()
                .map(this::toSummary)
                .collect(java.util.stream.Collectors.collectingAndThen(java.util.stream.Collectors.toList(), list -> new org.springframework.data.domain.PageImpl<>(list, pageable, list.size())));
    }

    @Override
    @Transactional
    public ApplicationResponse changeStatus(String email, java.util.UUID applicationId, ApplicationStatus status, String remarks) {
        User recruiter = findUser(email);
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found"));
        if (!application.getJob().getRecruiter().getId().equals(recruiter.getId())) {
            throw new ConflictException("You are not allowed to modify this application");
        }
        ApplicationStatus old = application.getStatus();
        application.setStatus(status);
        Application saved = applicationRepository.save(application);
        saveHistory(saved, old == null ? null : old.name(), status.name(), recruiter, remarks);
        return toResponse(saved);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Job findJob(java.util.UUID jobId) {
        return jobRepository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found"));
    }

    private void saveHistory(Application application, String oldStatus, String newStatus, User changedBy, String remarks) {
        ApplicationStatusHistory history = new ApplicationStatusHistory();
        history.setApplication(application);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setChangedBy(changedBy);
        history.setRemarks(remarks);
        historyRepository.save(history);
    }

    private ApplicationResponse toResponse(Application application) {
        return new ApplicationResponse(
                application.getId(),
                "Application saved successfully",
                application.getStatus(),
                application.getJob().getTitle(),
                application.getJob().getCompany().getName(),
                application.getJobSeeker().getEmail()
        );
    }

    private ApplicationSummaryResponse toSummary(Application application) {
        return new ApplicationSummaryResponse(
                application.getId(),
                application.getJob().getTitle(),
                application.getJob().getCompany().getName(),
                application.getJobSeeker().getEmail(),
                application.getStatus(),
                application.getCoverLetter()
        );
    }
}
