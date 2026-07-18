package com.jobportal.job.service;

import com.jobportal.job.dto.JobRequest;
import com.jobportal.job.dto.JobResponse;
import com.jobportal.job.dto.JobSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface JobService {
    JobResponse createJob(String email, JobRequest request);
    JobResponse updateJob(String email, UUID jobId, JobRequest request);
    JobResponse publishJob(String email, UUID jobId);
    JobResponse closeJob(String email, UUID jobId);
    Page<JobSummaryResponse> listJobs(String query, Pageable pageable);
    Page<JobSummaryResponse> listRecruiterJobs(String email, Pageable pageable);
    JobResponse getJobById(UUID jobId);
}
