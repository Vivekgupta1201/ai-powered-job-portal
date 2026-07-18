package com.jobportal.user.repository;

import com.jobportal.user.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, UUID> {
    boolean existsByUserId(UUID userId);
    Optional<JobSeekerProfile> findByUserId(UUID userId);
}
