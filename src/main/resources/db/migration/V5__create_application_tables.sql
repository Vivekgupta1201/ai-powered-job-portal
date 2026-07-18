CREATE TABLE applications (
    id UUID PRIMARY KEY,
    job_id UUID NOT NULL,
    job_seeker_user_id UUID NOT NULL,
    resume_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    cover_letter TEXT,
    applied_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_applications_job FOREIGN KEY (job_id) REFERENCES jobs(id),
    CONSTRAINT fk_applications_user FOREIGN KEY (job_seeker_user_id) REFERENCES users(id),
    CONSTRAINT fk_applications_resume FOREIGN KEY (resume_id) REFERENCES resumes(id),
    CONSTRAINT uk_applications_job_user UNIQUE (job_id, job_seeker_user_id)
);

CREATE TABLE application_status_history (
    id UUID PRIMARY KEY,
    application_id UUID NOT NULL,
    old_status VARCHAR(30),
    new_status VARCHAR(30) NOT NULL,
    changed_by UUID NOT NULL,
    changed_at TIMESTAMP NOT NULL,
    remarks TEXT,
    CONSTRAINT fk_ash_application FOREIGN KEY (application_id) REFERENCES applications(id),
    CONSTRAINT fk_ash_changed_by FOREIGN KEY (changed_by) REFERENCES users(id)
);
