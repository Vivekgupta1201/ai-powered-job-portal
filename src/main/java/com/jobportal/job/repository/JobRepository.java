package com.jobportal.job.repository;

import com.jobportal.job.entity.Job;
import com.jobportal.job.entity.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID>, JpaSpecificationExecutor<Job> {
    List<Job> findByRecruiterId(UUID recruiterId);
    List<Job> findByStatus(JobStatus status);
}
