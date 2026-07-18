CREATE TABLE resumes (
    id UUID PRIMARY KEY,
    job_seeker_user_id UUID NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    storage_key VARCHAR(500) NOT NULL,
    content_type VARCHAR(100),
    file_size BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    uploaded_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_resumes_user FOREIGN KEY (job_seeker_user_id) REFERENCES users(id)
);
