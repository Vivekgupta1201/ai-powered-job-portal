package com.jobportal.user.repository;

import com.jobportal.user.entity.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, UUID> {
    boolean existsByUserId(UUID userId);
    Optional<RecruiterProfile> findByUserId(UUID userId);
}
