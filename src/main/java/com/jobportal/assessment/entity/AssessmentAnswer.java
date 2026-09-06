package com.jobportal.assessment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "assessment_answers")
@Getter
@Setter
@NoArgsConstructor
public class AssessmentAnswer {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private AssessmentAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private AssessmentQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    private AssessmentOption selectedOption;

    @Column(columnDefinition = "TEXT")
    private String answerText;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal marksAwarded;

    @PrePersist
    void prePersist() { id = UUID.randomUUID(); }
}
