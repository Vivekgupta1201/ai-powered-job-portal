package com.jobportal.assessment.repository;

import com.jobportal.assessment.entity.AssessmentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssessmentAttemptRepository extends JpaRepository<AssessmentAttempt, UUID> {
    Optional<AssessmentAttempt> findByAssessmentIdAndCandidateId(UUID assessmentId, UUID candidateId);
}
