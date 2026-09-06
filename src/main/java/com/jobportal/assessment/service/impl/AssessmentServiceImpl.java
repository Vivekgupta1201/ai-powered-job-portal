package com.jobportal.assessment.service.impl;

import com.jobportal.application.entity.Application;
import com.jobportal.application.entity.ApplicationStatus;
import com.jobportal.application.repository.ApplicationRepository;
import com.jobportal.assessment.dto.*;
import com.jobportal.assessment.entity.*;
import com.jobportal.assessment.repository.*;
import com.jobportal.assessment.service.AssessmentService;
import com.jobportal.auth.entity.User;
import com.jobportal.auth.repository.UserRepository;
import com.jobportal.common.exception.ConflictException;
import com.jobportal.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {
    private final AssessmentRepository assessmentRepository;
    private final AssessmentQuestionRepository questionRepository;
    private final AssessmentOptionRepository optionRepository;
    private final AssessmentAttemptRepository attemptRepository;
    private final AssessmentAnswerRepository answerRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AssessmentResponse create(String recruiterEmail, CreateAssessmentRequest request) {
        User recruiter = user(recruiterEmail);
        Application application = application(request.getApplicationId());
        requireRecruiterOwnsApplication(recruiter, application);
        if (assessmentRepository.existsByApplicationId(application.getId())) {
            throw new ConflictException("Assessment already exists for this application");
        }
        if (application.getStatus() != ApplicationStatus.SHORTLISTED
                && application.getStatus() != ApplicationStatus.ASSESSMENT_PENDING) {
            throw new ConflictException("Candidate must be shortlisted before creating an assessment");
        }

        Assessment assessment = new Assessment();
        assessment.setApplication(application);
        assessment.setTitle(request.getTitle());
        assessment.setDurationMinutes(request.getDurationMinutes());
        assessment.setPassPercentage(request.getPassPercentage());
        assessment.setStatus(AssessmentStatus.OPEN);
        Assessment saved = assessmentRepository.save(assessment);

        int sequence = 1;
        for (CreateAssessmentRequest.QuestionRequest questionRequest : request.getQuestions()) {
            AssessmentQuestion question = new AssessmentQuestion();
            question.setAssessment(saved);
            question.setQuestionText(questionRequest.getQuestionText());
            question.setQuestionType(questionRequest.getQuestionType());
            question.setMarks(questionRequest.getMarks());
            question.setSequenceNumber(sequence++);
            AssessmentQuestion savedQuestion = questionRepository.save(question);

            for (CreateAssessmentRequest.OptionRequest optionRequest : questionRequest.getOptions()) {
                AssessmentOption option = new AssessmentOption();
                option.setQuestion(savedQuestion);
                option.setOptionText(optionRequest.getOptionText());
                option.setCorrect(optionRequest.isCorrect());
                optionRepository.save(option);
            }
        }

        application.setStatus(ApplicationStatus.ASSESSMENT_PENDING);
        applicationRepository.save(application);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public AssessmentResponse publish(String recruiterEmail, UUID assessmentId) {
        Assessment assessment = findAssessment(assessmentId);
        requireRecruiterOwnsApplication(user(recruiterEmail), assessment.getApplication());
        if (questionRepository.findByAssessmentIdOrderBySequenceNumber(assessmentId).isEmpty()) {
            throw new ConflictException("An assessment must contain at least one question");
        }
        assessment.setStatus(AssessmentStatus.OPEN);
        return toResponse(assessmentRepository.save(assessment));
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentResponse getForUser(String email, UUID assessmentId) {
        Assessment assessment = findAssessment(assessmentId);
        User user = user(email);
        if (!assessment.getApplication().getJobSeeker().getId().equals(user.getId())) {
            requireRecruiterOwnsApplication(user, assessment.getApplication());
        }
        return toResponse(assessment);
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentResponse getForApplication(String email, UUID applicationId) {
        Assessment assessment = assessmentRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new NotFoundException("Assessment not found"));
        User current = user(email);
        if (!assessment.getApplication().getJobSeeker().getId().equals(current.getId())) {
            requireRecruiterOwnsApplication(current, assessment.getApplication());
        }
        return toResponse(assessment);
    }

    @Override
    @Transactional
    public AssessmentResultResponse start(String candidateEmail, UUID assessmentId) {
        User candidate = user(candidateEmail);
        Assessment assessment = findAssessment(assessmentId);
        requireCandidateOwnsAssessment(candidate, assessment);
        if (assessment.getStatus() != AssessmentStatus.OPEN) {
            throw new ConflictException("Assessment is not open");
        }
        if (assessment.getDueAt() != null && LocalDateTime.now().isAfter(assessment.getDueAt())) {
            throw new ConflictException("Assessment deadline has passed");
        }
        AssessmentAttempt attempt = attemptRepository.findByAssessmentIdAndCandidateId(assessmentId, candidate.getId())
                .orElseGet(() -> {
                    AssessmentAttempt value = new AssessmentAttempt();
                    value.setAssessment(assessment);
                    value.setCandidate(candidate);
                    value.setStatus(AttemptStatus.IN_PROGRESS);
                    return attemptRepository.save(value);
                });
        if (attempt.getStatus() == AttemptStatus.SUBMITTED) {
            throw new ConflictException("Assessment has already been submitted");
        }
        assessment.getApplication().setStatus(ApplicationStatus.ASSESSMENT_IN_PROGRESS);
        applicationRepository.save(assessment.getApplication());
        return toResult(attempt);
    }

    @Override
    @Transactional
    public AssessmentResultResponse submit(String candidateEmail, UUID attemptId, SubmitAssessmentRequest request) {
        User candidate = user(candidateEmail);
        AssessmentAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Assessment attempt not found"));
        if (!attempt.getCandidate().getId().equals(candidate.getId())) {
            throw new ConflictException("You are not allowed to submit this attempt");
        }
        if (attempt.getStatus() == AttemptStatus.SUBMITTED) {
            throw new ConflictException("Assessment has already been submitted");
        }
        Assessment assessment = attempt.getAssessment();
        if (LocalDateTime.now().isAfter(attempt.getStartedAt().plusMinutes(assessment.getDurationMinutes()))) {
            throw new ConflictException("Assessment time limit has expired");
        }
        if (assessment.getDueAt() != null && LocalDateTime.now().isAfter(assessment.getDueAt())) {
            throw new ConflictException("Assessment deadline has passed");
        }
        int totalMarks = 0;
        BigDecimal score = BigDecimal.ZERO;
        List<AssessmentQuestion> questions = questionRepository.findByAssessmentIdOrderBySequenceNumber(assessment.getId());
        for (AssessmentQuestion question : questions) {
            totalMarks += question.getMarks();
            SubmitAssessmentRequest.AnswerRequest submitted = request.getAnswers().stream()
                    .filter(answer -> question.getId().equals(answer.getQuestionId()))
                    .findFirst().orElse(null);
            AssessmentAnswer answer = new AssessmentAnswer();
            answer.setAttempt(attempt);
            answer.setQuestion(question);
            answer.setMarksAwarded(BigDecimal.ZERO);
            if (submitted != null && submitted.getSelectedOptionId() != null) {
                AssessmentOption option = optionRepository.findById(submitted.getSelectedOptionId())
                        .orElseThrow(() -> new NotFoundException("Selected option not found"));
                if (!option.getQuestion().getId().equals(question.getId())) {
                    throw new ConflictException("Selected option does not belong to the question");
                }
                answer.setSelectedOption(option);
                if (option.isCorrect()) {
                    answer.setMarksAwarded(BigDecimal.valueOf(question.getMarks()));
                    score = score.add(BigDecimal.valueOf(question.getMarks()));
                }
            }
            if (submitted != null) {
                answer.setAnswerText(submitted.getAnswerText());
            }
            answerRepository.save(answer);
        }
        BigDecimal percentage = totalMarks == 0 ? BigDecimal.ZERO
                : score.multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(totalMarks), 2, RoundingMode.HALF_UP);
        attempt.setStatus(AttemptStatus.SUBMITTED);
        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setScore(score);
        attempt.setTotalMarks(totalMarks);
        attempt.setPercentage(percentage);
        attempt.setPassed(percentage.compareTo(assessment.getPassPercentage()) >= 0);
        AssessmentAttempt saved = attemptRepository.save(attempt);
        assessment.setStatus(AssessmentStatus.EVALUATED);
        assessmentRepository.save(assessment);
        assessment.getApplication().setStatus(ApplicationStatus.ASSESSMENT_SUBMITTED);
        applicationRepository.save(assessment.getApplication());
        return toResult(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentResultResponse result(String email, UUID assessmentId) {
        Assessment assessment = findAssessment(assessmentId);
        User current = user(email);
        if (current.getId().equals(assessment.getApplication().getJobSeeker().getId())) {
            return attemptRepository.findByAssessmentIdAndCandidateId(assessmentId, current.getId())
                    .map(this::toResult)
                    .orElseThrow(() -> new NotFoundException("Assessment attempt not found"));
        }
        requireRecruiterOwnsApplication(current, assessment.getApplication());
        return attemptRepository.findByAssessmentIdAndCandidateId(assessmentId, assessment.getApplication().getJobSeeker().getId())
                .map(this::toResult)
                .orElseThrow(() -> new NotFoundException("Assessment attempt not found"));
    }

    private Assessment findAssessment(UUID id) {
        return assessmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Assessment not found"));
    }

    private Application application(UUID id) {
        return applicationRepository.findById(id).orElseThrow(() -> new NotFoundException("Application not found"));
    }

    private User user(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private void requireRecruiterOwnsApplication(User recruiter, Application application) {
        if (!application.getJob().getRecruiter().getId().equals(recruiter.getId())) {
            throw new ConflictException("You are not allowed to manage this assessment");
        }
    }

    private void requireCandidateOwnsAssessment(User candidate, Assessment assessment) {
        if (!assessment.getApplication().getJobSeeker().getId().equals(candidate.getId())) {
            throw new ConflictException("You are not allowed to access this assessment");
        }
    }

    private AssessmentResponse toResponse(Assessment assessment) {
        List<QuestionResponse> questions = questionRepository.findByAssessmentIdOrderBySequenceNumber(assessment.getId()).stream()
                .map(question -> new QuestionResponse(
                        question.getId(), question.getQuestionText(), question.getQuestionType(), question.getMarks(),
                        question.getSequenceNumber(), optionRepository.findByQuestionId(question.getId()).stream()
                                .map(option -> new OptionResponse(option.getId(), option.getOptionText())).toList()))
                .toList();
        return new AssessmentResponse(assessment.getId(), assessment.getApplication().getId(), assessment.getTitle(),
                assessment.getDurationMinutes(), assessment.getPassPercentage(), assessment.getStatus(), assessment.getDueAt(), questions);
    }

    private AssessmentResultResponse toResult(AssessmentAttempt attempt) {
        return new AssessmentResultResponse(attempt.getId(), attempt.getAssessment().getId(), attempt.getStatus(),
                attempt.getScore(), attempt.getTotalMarks(), attempt.getPercentage(), attempt.getPassed());
    }
}
