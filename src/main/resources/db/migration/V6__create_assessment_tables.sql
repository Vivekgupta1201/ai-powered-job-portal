CREATE TABLE assessments (
    id UUID PRIMARY KEY,
    application_id UUID NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    duration_minutes INTEGER NOT NULL,
    pass_percentage NUMERIC(5,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    starts_at TIMESTAMP,
    due_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_assessment_application FOREIGN KEY (application_id) REFERENCES applications(id)
);

CREATE TABLE assessment_questions (
    id UUID PRIMARY KEY,
    assessment_id UUID NOT NULL,
    question_text TEXT NOT NULL,
    question_type VARCHAR(30) NOT NULL,
    marks INTEGER NOT NULL,
    sequence_number INTEGER NOT NULL,
    CONSTRAINT fk_question_assessment FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE,
    CONSTRAINT uk_question_sequence UNIQUE (assessment_id, sequence_number)
);

CREATE TABLE assessment_options (
    id UUID PRIMARY KEY,
    question_id UUID NOT NULL,
    option_text TEXT NOT NULL,
    correct BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_option_question FOREIGN KEY (question_id) REFERENCES assessment_questions(id) ON DELETE CASCADE
);

CREATE TABLE assessment_attempts (
    id UUID PRIMARY KEY,
    assessment_id UUID NOT NULL,
    candidate_user_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    started_at TIMESTAMP NOT NULL,
    submitted_at TIMESTAMP,
    score NUMERIC(10,2),
    total_marks INTEGER,
    percentage NUMERIC(5,2),
    passed BOOLEAN,
    CONSTRAINT fk_attempt_assessment FOREIGN KEY (assessment_id) REFERENCES assessments(id),
    CONSTRAINT fk_attempt_candidate FOREIGN KEY (candidate_user_id) REFERENCES users(id),
    CONSTRAINT uk_assessment_candidate UNIQUE (assessment_id, candidate_user_id)
);

CREATE TABLE assessment_answers (
    id UUID PRIMARY KEY,
    attempt_id UUID NOT NULL,
    question_id UUID NOT NULL,
    selected_option_id UUID,
    answer_text TEXT,
    marks_awarded NUMERIC(10,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_answer_attempt FOREIGN KEY (attempt_id) REFERENCES assessment_attempts(id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id) REFERENCES assessment_questions(id),
    CONSTRAINT fk_answer_option FOREIGN KEY (selected_option_id) REFERENCES assessment_options(id),
    CONSTRAINT uk_attempt_question UNIQUE (attempt_id, question_id)
);
