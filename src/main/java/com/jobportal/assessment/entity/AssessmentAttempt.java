package com.jobportal.assessment.entity;

import com.jobportal.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assessment_attempts")
@Getter
@Setter
@NoArgsConstructor
public class AssessmentAttempt {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_user_id", nullable = false)
    private User candidate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AttemptStatus status;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime submittedAt;
    private BigDecimal score;
    private Integer totalMarks;
    private BigDecimal percentage;
    private Boolean passed;

    @PrePersist
    void prePersist() {
        id = UUID.randomUUID();
        startedAt = LocalDateTime.now();
    }
}
