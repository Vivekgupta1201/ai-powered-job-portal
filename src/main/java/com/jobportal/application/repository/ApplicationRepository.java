package com.jobportal.application.repository;

import com.jobportal.application.entity.Application;
import com.jobportal.application.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    boolean existsByJobIdAndJobSeekerId(UUID jobId, UUID jobSeekerId);
    List<Application> findByJobSeekerId(UUID jobSeekerId);
    List<Application> findByJobId(UUID jobId);
    boolean existsByResumeIdAndJobRecruiterId(UUID resumeId, UUID recruiterId);
}
