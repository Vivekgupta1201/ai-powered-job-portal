# Job Portal

A production-oriented Job Portal backend built with Spring Boot, PostgreSQL, Flyway, JWT authentication, and role-based access control.  
The system supports two user roles:

- Job Seeker
- Recruiter

This project is designed as a modular backend foundation that can grow into a complete full-stack platform with frontend, AI features, resume parsing, and assessment tools.

## Repository Name Suggestions

If you want a GitHub repository name that sounds more professional and future-ready, these are good options:

- `ai-job-portal`
- `smart-job-portal`
- `job-portal-platform`
- `automated-job-portal`
- `ai-powered-job-portal`
- `nextgen-job-portal`

### Best recommendation
`ai-powered-job-portal`

Why this is the best:
- it sounds modern and professional
- it matches your future AI-based roadmap
- it still works even if AI features are added later
- it is easier to understand than a very long name

If you want a more neutral name without emphasizing AI too much, use:
- `job-portal-platform`

## Project Overview

This backend provides the core APIs for:

- user registration
- login with JWT
- role-based authorization
- recruiter and job seeker profile management
- job posting and browsing
- resume upload and storage
- job application workflow
- recruiter applicant management
- recruiter dashboard summary

It follows a modular structure and clean separation of concerns so it can later be expanded into microservices if needed.

## Features Completed

### Authentication

- Recruiter registration
- Job seeker registration
- Login with email and password
- JWT token generation
- JWT validation for protected APIs
- Current logged-in user endpoint

### Profile Management

- Recruiter profile creation
- Job seeker profile creation
- Recruiter company creation
- View current logged-in profile
- Update recruiter profile
- Update job seeker profile
- Role-based profile access protection

### Job Portal Core

- Job creation by recruiter
- Job update
- Job publish and close
- Public job listing
- Job details endpoint
- Recruiter's own job listing

### Resume Management

- Resume upload target generation
- Resume file upload
- List resumes
- Activate a resume
- View resume details

### Applications

- Apply to a job
- View my applications
- Recruiter view applicants for a job
- Shortlist or reject candidate
- Application status change tracking

### Recruiter Dashboard

- Total jobs
- Draft jobs
- Published jobs
- Closed jobs
- Total applicants
- Shortlisted applicants
- Rejected applicants
- Applied applicants

### Production Support

- PostgreSQL database
- Flyway migrations
- custom exception handling
- REST API design
- role-based backend security

## Tech Stack

### Backend

- Java 21
- Spring Boot 4.0.5
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Flyway
- JWT
- Lombok
- PostgreSQL

### Frontend

Planned frontend stack:

- React or Next.js
- Tailwind CSS or Material UI
- Axios or Fetch API
- React Hook Form

### Future Integrations

- AI-based question generation
- resume parsing
- email notifications
- AWS S3 file storage
- online assessment platform

## Architecture

This project is organized as a modular backend with separate feature areas:

- `auth`
- `user`
- `company`
- `job`
- `resume`
- `application`
- `dashboard`
- `common`

The design follows clean architecture ideas:

- controllers handle HTTP requests
- services contain business logic
- repositories handle persistence
- DTOs keep API contracts separate from entities

## User Roles

### Job Seeker

Can:
- register
- login
- create and update profile
- view profile
- upload resume
- browse jobs
- apply to jobs
- view own applications

### Recruiter

Can:
- register
- login
- create and update profile
- create company
- post jobs
- update/publish/close jobs
- view applicants
- shortlist/reject candidates
- view dashboard summary

## API Overview

### Auth APIs

- `POST /api/auth/register/job-seeker`
- `POST /api/auth/register/recruiter`
- `POST /api/auth/login`
- `GET /api/auth/me`

### Profile APIs

- `POST /api/profile/job-seeker`
- `POST /api/profile/recruiter`
- `GET /api/profile/me`
- `PUT /api/profile/job-seeker`
- `PUT /api/profile/recruiter`

### Job APIs

- `POST /api/jobs`
- `PUT /api/jobs/{jobId}`
- `PATCH /api/jobs/{jobId}/publish`
- `PATCH /api/jobs/{jobId}/close`
- `GET /api/jobs`
- `GET /api/jobs/{jobId}`
- `GET /api/jobs/recruiter/my-jobs`

### Resume APIs

- `POST /api/resumes/upload-target`
- `POST /api/resumes/upload`
- `GET /api/resumes`
- `PATCH /api/resumes/{resumeId}/activate`
- `GET /api/resumes/{resumeId}`

### Application APIs

- `POST /api/applications/jobs/{jobId}/apply`
- `GET /api/applications/me`
- `GET /api/applications/recruiter/jobs/{jobId}`
- `PATCH /api/applications/{applicationId}/status`

### Dashboard API

- `GET /api/dashboard/recruiter`

## Database Tables

The backend currently uses these tables:

- `users`
- `roles`
- `user_roles`
- `companies`
- `job_seekers`
- `recruiters`
- `jobs`
- `resumes`
- `applications`
- `application_status_history`
- `flyway_schema_history`

## Security

The project uses JWT-based authentication with role-based access control.

Important security behavior:

- public routes are open for registration and login
- protected routes require a valid Bearer token
- recruiter-only APIs are restricted
- job seeker-only APIs are restricted

## How the Flow Works

1. A user registers as recruiter or job seeker.
2. The user logs in using email and password.
3. Backend returns a JWT token.
4. Frontend sends the token in the `Authorization` header.
5. Backend validates the token and identifies the logged-in user.
6. The user creates or updates a profile.
7. Recruiters post jobs and manage candidates.
8. Job seekers browse jobs, upload resumes, and apply.

## How to Run

### Prerequisites

- Java 21
- PostgreSQL
- Maven

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/ai-job-portal.git
```

### 2. Create PostgreSQL database

```sql
CREATE DATABASE jobportal_db;
```

### 3. Update database credentials

Update `src/main/resources/application.properties` with your local PostgreSQL username and password.

### 4. Run the application

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## Testing

You can test the backend using:

- Postman
- Swagger UI if enabled
- browser for public endpoints like `/health`

### Recommended testing order

1. Register job seeker
2. Register recruiter
3. Login
4. Create profile
5. View profile
6. Update profile
7. Create job
8. Browse jobs
9. Upload resume
10. Apply to job
11. View recruiter dashboard

## Future Enhancements

This project is intentionally designed for future growth.

### AI Features

- AI-based interview question generation from resume and job description
- AI skill extraction from resumes
- AI job-candidate matching

### Resume Intelligence

- automated resume parsing
- skill normalization
- profile enrichment from resume

### Notifications

- registration email
- application confirmation email
- shortlist/reject notifications

### File Storage

- AWS S3 for resume storage
- signed URLs for secure access

### Frontend

- recruiter dashboard UI
- job seeker job feed
- profile screens
- resume upload screens
- application tracking UI

### Advanced Platform Features

- online test/assessment platform
- payment integration for premium recruiter plans
- Google or LinkedIn login
- admin panel

## Project Status

Current backend milestone:

- Auth completed
- Profile management completed
- Job, resume, application, and dashboard modules added
- Ready for frontend integration and further refinement

## Notes for Contributors

- Keep feature logic modular
- Do not expose entities directly in APIs
- Prefer DTOs for requests and responses
- Keep role checks on the backend
- Use JWT for every protected request
- Maintain Flyway migrations for all database changes

## License

Add your preferred license here before publishing publicly.

