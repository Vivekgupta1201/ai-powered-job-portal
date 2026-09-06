package com.jobportal.assessment.repository;

import com.jobportal.assessment.entity.AssessmentOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AssessmentOptionRepository extends JpaRepository<AssessmentOption, UUID> {
    List<AssessmentOption> findByQuestionId(UUID questionId);
}
