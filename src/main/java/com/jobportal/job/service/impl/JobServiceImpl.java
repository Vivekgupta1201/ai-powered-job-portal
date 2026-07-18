package com.jobportal.job.service.impl;

import com.jobportal.auth.entity.User;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.common.exception.ConflictException;
import com.jobportal.common.exception.NotFoundException;
import com.jobportal.company.repository.CompanyRepository;
import com.jobportal.job.dto.JobRequest;
import com.jobportal.job.dto.JobResponse;
import com.jobportal.job.dto.JobSummaryResponse;
import com.jobportal.job.entity.Job;
import com.jobportal.job.entity.JobStatus;
import com.jobportal.job.repository.JobRepository;
import com.jobportal.job.service.JobService;
import com.jobportal.user.repository.RecruiterProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final CompanyRepository companyRepository;

    @Override
   @Transactional
    public JobResponse createJob(String email, JobRequest request) {
        User recruiter = findUser(email);
        var recruiterProfile = recruiterProfileRepository.findByUserId(recruiter.getId())
                .orElseThrow(() -> new NotFoundException("Recruiter profile not found"));
        System.out.print("cgvh");

        Job job = new Job();
        job.setRecruiter(recruiter);
        job.setCompany(recruiterProfile.getCompany());
        applyRequest(job, request);
        job.setStatus(JobStatus.DRAFT);

        Job saved = jobRepository.save(job);
        
        return toResponse(saved);
    }

    @Override
    @Transactional
    public JobResponse updateJob(String email, UUID jobId, JobRequest request) {
        User recruiter = findUser(email);
        Job job = findOwnedJob(jobId, recruiter.getId());
        applyRequest(job, request);
        job.setStatus(job.getStatus() == null ? JobStatus.DRAFT : job.getStatus());
        return toResponse(jobRepository.save(job));
    }

    @Override
    @Transactional
    public JobResponse publishJob(String email, UUID jobId) {
        User recruiter = findUser(email);
        Job job = findOwnedJob(jobId, recruiter.getId());
        job.setStatus(JobStatus.PUBLISHED);
        return toResponse(jobRepository.save(job));
    }

    @Override
    @Transactional
    public JobResponse closeJob(String email, UUID jobId) {
        User recruiter = findUser(email);
        Job job = findOwnedJob(jobId, recruiter.getId());
        job.setStatus(JobStatus.CLOSED);
        return toResponse(jobRepository.save(job));
    }

    @Override
    public Page<JobSummaryResponse> listJobs(String query, Pageable pageable) {
        Specification<Job> spec = Specification.where((root, cq, cb) -> cb.equal(root.get("status"), JobStatus.PUBLISHED));
        if (query != null && !query.isBlank()) {
            String like = "%" + query.toLowerCase() + "%";
            spec = spec.and((root, cq, cb) -> cb.or(
                    cb.like(cb.lower(root.get("title")), like),
                    cb.like(cb.lower(root.get("location")), like),
                    cb.like(cb.lower(root.get("description")), like)
            ));
        }
        return jobRepository.findAll(spec, pageable).map(this::toSummaryResponse);
    }

    @Override
    public Page<JobSummaryResponse> listRecruiterJobs(String email, Pageable pageable) {
        User recruiter = findUser(email);
        return jobRepository.findAll((root, cq, cb) -> cb.equal(root.get("recruiter"), recruiter), pageable)
                .map(this::toSummaryResponse);
    }

    @Override
    public JobResponse getJobById(UUID jobId) {
        return toResponse(jobRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found")));
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Job findOwnedJob(UUID jobId, UUID recruiterId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found"));
        if (!job.getRecruiter().getId().equals(recruiterId)) {
            throw new ConflictException("You are not allowed to modify this job");
        }
        return job;
    }

    private void applyRequest(Job job, JobRequest request) {
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setEmploymentType(request.getEmploymentType());
        job.setExperienceMin(request.getExperienceMin());
        job.setExperienceMax(request.getExperienceMax());
        job.setLocation(request.getLocation());
        job.setRemoteType(request.getRemoteType());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setApplicationDeadline(request.getApplicationDeadline());
    }

    private JobResponse toResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getEmploymentType(),
                job.getExperienceMin(),
                job.getExperienceMax(),
                job.getLocation(),
                job.getRemoteType(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getStatus(),
                job.getApplicationDeadline(),
                job.getCompany().getName(),
                job.getRecruiter().getEmail()
        );
    }

    private JobSummaryResponse toSummaryResponse(Job job) {
        return new JobSummaryResponse(
                job.getId(),
                job.getTitle(),
                job.getLocation(),
                job.getEmploymentType(),
                job.getRemoteType(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getStatus(),
                job.getApplicationDeadline(),
                job.getCompany().getName()
        );
    }
}
