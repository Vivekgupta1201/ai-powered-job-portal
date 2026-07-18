CREATE TABLE companies (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    website VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE job_seekers (
    user_id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    experience_years INT DEFAULT 0,
    current_location VARCHAR(150),
    headline VARCHAR(255),
    summary TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_job_seekers_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE recruiters (
    user_id UUID PRIMARY KEY,
    company_id UUID NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    designation VARCHAR(150),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_recruiters_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_recruiters_company FOREIGN KEY (company_id) REFERENCES companies(id)
);
