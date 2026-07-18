package com.jobportal.resume.repository;

import com.jobportal.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    List<Resume> findByJobSeekerId(UUID jobSeekerId);
    Optional<Resume> findByIdAndJobSeekerId(UUID id, UUID jobSeekerId);
    Optional<Resume> findByJobSeekerIdAndActiveTrue(UUID jobSeekerId);
}
