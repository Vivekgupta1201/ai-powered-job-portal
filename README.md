# AI-Powered Job Portal

A production-oriented, modular **Job Portal Backend** built using **Java 21, Spring Boot, Spring Security, JWT, Spring Data JPA, PostgreSQL, and Flyway**.

The platform is designed to connect **Job Seekers** and **Recruiters** through a secure REST API architecture. Job seekers can create profiles, manage resumes, search for jobs, apply to positions, and participate in assessments. Recruiters can create companies, publish job openings, manage applicants, and create assessments.

The system is designed with future **AI-powered recruitment capabilities** in mind, including resume parsing, skill extraction, candidate-job matching, AI-generated assessment questions, and intelligent candidate ranking.

---

# 📌 Project Overview

Traditional job portals require candidates to repeatedly enter their information, upload resumes, search for jobs, and manually track applications.

This project aims to provide a modular recruitment platform where:

* Job seekers maintain a centralized professional profile.
* Recruiters manage companies and job openings.
* Candidates can upload and manage multiple resumes.
* Candidates can apply for jobs through a structured application workflow.
* Recruiters can review and manage applicants.
* Recruiters can use assessments to evaluate candidates.
* Application statuses are tracked throughout the recruitment lifecycle.
* The backend provides secure APIs using JWT authentication.
* The architecture is prepared for future AI integration.

The project currently focuses on building a strong backend foundation before expanding into frontend and AI capabilities.

---

# 🎯 Project Objectives

The main objectives of the project are:

1. Build a secure RESTful backend for a job portal.
2. Implement authentication using JWT.
3. Implement role-based authorization.
4. Separate business logic from controllers and persistence layers.
5. Build reusable and maintainable modules.
6. Manage database changes using Flyway.
7. Implement complete job application workflows.
8. Provide resume management capabilities.
9. Provide recruiter dashboard information.
10. Introduce an online assessment module.
11. Prepare the platform for future AI-based recruitment features.

---

# 👥 User Roles

The application currently supports two primary roles.

## Job Seeker

A Job Seeker represents a candidate looking for employment opportunities.

A Job Seeker can:

* Register an account
* Login securely
* Create a professional profile
* Update profile information
* Upload resumes
* Manage multiple resumes
* Activate a preferred resume
* Browse available jobs
* View job details
* Apply for jobs
* View submitted applications
* Track application status
* Participate in assessments

---

## Recruiter

A Recruiter represents an organization or hiring professional.

A Recruiter can:

* Register an account
* Login securely
* Create a recruiter profile
* Create a company
* Update company/profile information
* Create job postings
* Update job postings
* Publish jobs
* Close jobs
* View their own jobs
* View applicants
* Shortlist candidates
* Reject candidates
* View recruitment dashboard statistics
* Create and manage assessments

---

# 🏗️ Architecture

The project follows a modular layered architecture.

```text
Client
  │
  │ HTTP Request
  ▼
Controller Layer
  │
  ▼
Service Layer
  │
  ▼
Repository Layer
  │
  ▼
PostgreSQL Database
```

Security is applied before protected requests reach the application logic.

```text
HTTP Request
     │
     ▼
JWT Authentication Filter
     │
     ▼
Spring Security
     │
     ▼
Role Authorization
     │
     ▼
Controller
     │
     ▼
Service
     │
     ▼
Repository
     │
     ▼
Database
```

This structure makes the application easier to maintain, test, debug, and extend.

---

# 📁 Project Structure

The project is organized by business functionality rather than putting every controller, service, and repository into one common package.

```text
src/main/java/com/jobportal
│
├── application
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── assessment
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── auth
│   ├── config
│   ├── controller
│   └── service
│
├── company
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── dashboard
│   ├── controller
│   └── service
│
├── job
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── resume
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── user
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
└── common
    └── exception
```

Each feature owns its own business logic, DTOs, repositories, controllers, and entities wherever applicable.

This modular approach allows new functionality to be added without heavily modifying existing modules.

---

# 🔐 Authentication and Authorization

Security is one of the core parts of the application.

The system uses:

* Spring Security
* JWT authentication
* Role-based authorization
* Password-based login
* Bearer token authentication

## Authentication Flow

```text
User Registration
       │
       ▼
Credentials Stored
       │
       ▼
User Login
       │
       ▼
Credentials Validated
       │
       ▼
JWT Generated
       │
       ▼
JWT Returned to Client
```

For subsequent protected requests:

```text
Client
  │
  │ Authorization: Bearer <JWT>
  ▼
JWT Authentication Filter
  │
  ▼
Token Validation
  │
  ▼
User Identification
  │
  ▼
Role Verification
  │
  ▼
Protected API
```

The frontend is responsible for sending the token with protected API requests.

Example:

```text
Authorization: Bearer <JWT_TOKEN>
```

---

# 👤 Profile Management

The profile module separates the common user account from role-specific information.

A Job Seeker can maintain candidate-specific information.

A Recruiter can maintain recruiter-specific information and company information.

This allows the platform to support different workflows without mixing unrelated user data.

### Job Seeker Profile

Typical information can include:

* Name
* Contact information
* Professional information
* Skills
* Experience
* Education
* Resume information

### Recruiter Profile

Typical information can include:

* Recruiter information
* Contact details
* Company information
* Hiring-related information

---

# 🏢 Company Management

Recruiters can create and manage company information.

The company module provides a foundation for associating job postings with organizations.

Typical company information can include:

* Company name
* Description
* Website
* Location
* Industry
* Company information

This allows recruiters to publish jobs under their organization.

---

# 💼 Job Management

The Job module represents the core recruitment functionality.

Recruiters can create job postings and manage their lifecycle.

## Job Lifecycle

```text
Draft
  │
  ▼
Published
  │
  ▼
Closed
```

Recruiters can:

* Create jobs
* Update jobs
* Publish jobs
* Close jobs
* View their own jobs

Job seekers can:

* Browse jobs
* View job details
* Apply to jobs

---

# 📄 Resume Management

Resume management allows job seekers to maintain their resumes within the platform.

The system supports:

* Resume upload target generation
* Resume upload
* Resume listing
* Resume details
* Resume activation

A candidate may have multiple resumes but can identify one as the active resume.

Example:

```text
Candidate
   │
   ├── Resume 1
   ├── Resume 2
   └── Resume 3
          │
          ▼
     Active Resume
```

This structure is useful for future AI resume analysis because resumes can later be processed to extract:

* Skills
* Experience
* Education
* Certifications
* Technologies
* Job preferences

---

# 📨 Application Management

The Application module manages the relationship between a candidate and a job.

A candidate can apply for a job using the application API.

Recruiters can then review the submitted applications.

## Application Flow

```text
Job Seeker
    │
    ▼
Browse Job
    │
    ▼
Apply
    │
    ▼
Application Created
    │
    ▼
Recruiter Reviews
    │
    ├── Shortlisted
    │
    └── Rejected
```

Application status changes can be tracked using application status history.

This provides an audit trail of recruitment decisions.

---

# 📊 Recruiter Dashboard

The recruiter dashboard provides a summary of recruitment activity.

The dashboard can display:

* Total jobs
* Draft jobs
* Published jobs
* Closed jobs
* Total applicants
* Shortlisted applicants
* Rejected applicants
* Applied applicants

Example:

```text
Recruiter Dashboard
────────────────────────────
Total Jobs          : 20
Draft Jobs          : 3
Published Jobs      : 12
Closed Jobs         : 5

Total Applicants    : 185
Shortlisted         : 42
Rejected            : 73
Applied             : 70
────────────────────────────
```

This module can later be extended with charts, trends, hiring analytics, and AI-generated recruitment insights.

---

# 📝 Assessment Module

The Assessment module provides the foundation for an online candidate evaluation system.

It allows recruiters to create assessments and candidates to attempt them.

The module currently contains entities and services for:

* Assessments
* Questions
* Options
* Attempts
* Answers
* Assessment status
* Attempt status
* Question types
* Assessment results

## Assessment Structure

```text
Assessment
    │
    ├── Question 1
    │      ├── Option A
    │      ├── Option B
    │      ├── Option C
    │      └── Option D
    │
    ├── Question 2
    │      ├── Option A
    │      ├── Option B
    │      └── Option C
    │
    └── Question 3
```

A candidate can create an attempt and submit answers.

```text
Candidate
    │
    ▼
Start Assessment
    │
    ▼
Answer Questions
    │
    ▼
Submit Assessment
    │
    ▼
Evaluate Answers
    │
    ▼
Assessment Result
```

The assessment architecture is also designed to support future AI-generated questions.

---

# 🤖 Planned AI Capabilities

The long-term goal of the project is to introduce AI into the recruitment workflow.

Potential AI features include:

## AI Resume Parsing

The system can analyze an uploaded resume and extract:

* Skills
* Experience
* Education
* Certifications
* Technologies
* Job roles

Example:

```text
Resume
  ↓
AI Resume Parser
  ↓
Extracted Information
  ├── Java
  ├── Spring Boot
  ├── PostgreSQL
  ├── REST APIs
  └── 3 Years Experience
```

---

## AI Candidate Matching

The system can compare:

```text
Candidate Resume
       +
Job Description
       ↓
AI Matching Engine
       ↓
Match Score
```

For example:

```text
Candidate A → 92%
Candidate B → 84%
Candidate C → 71%
```

This can help recruiters identify candidates whose skills and experience are more closely aligned with a job.

---

## AI Assessment Generation

A future version can generate questions based on:

* Job description
* Required skills
* Candidate experience
* Difficulty level

Example:

```text
Job Description
      ↓
AI Question Generator
      ↓
Assessment
      ↓
Questions
      ↓
Candidate Attempt
      ↓
Automatic Evaluation
```

---

# 🗄️ Database

The application uses **PostgreSQL** as its relational database.

Database schema changes are managed using **Flyway migrations**.

Current database areas include:

```text
users
roles
user_roles
companies
job_seekers
recruiters
jobs
resumes
applications
application_status_history
assessments
assessment_questions
assessment_options
assessment_attempts
assessment_answers
flyway_schema_history
```

Flyway ensures database changes are version-controlled and executed in the correct order.

Example migration:

```text
src/main/resources/db/migration/

V1__...
V2__...
V3__...
V4__...
V5__...
V6__create_assessment_tables.sql
```

The assessment database tables were introduced through a dedicated migration rather than manually modifying the database.

---

# 🛡️ Exception Handling

The backend uses centralized exception handling to provide consistent API error responses.

The application contains a common exception layer for handling errors such as:

* Unauthorized access
* Invalid requests
* Resource not found
* Authentication failures
* Business validation failures

Centralized exception handling prevents controllers from containing repetitive error-handling code.

---

# 🔄 API Request Flow

A typical protected API request follows this flow:

```text
Frontend / Postman
       │
       ▼
HTTP Request
       │
       ▼
JWT Filter
       │
       ▼
Spring Security
       │
       ▼
Controller
       │
       ▼
Service
       │
       ▼
Repository
       │
       ▼
PostgreSQL
       │
       ▼
Response DTO
       │
       ▼
Client
```

DTOs are used to prevent direct exposure of database entities through REST APIs.

---

# 🌐 API Overview

## Authentication

```http
POST /api/auth/register/job-seeker
POST /api/auth/register/recruiter
POST /api/auth/login
GET  /api/auth/me
```

## Profile

```http
POST /api/profile/job-seeker
POST /api/profile/recruiter
GET  /api/profile/me
PUT  /api/profile/job-seeker
PUT  /api/profile/recruiter
```

## Jobs

```http
POST   /api/jobs
PUT    /api/jobs/{jobId}
PATCH  /api/jobs/{jobId}/publish
PATCH  /api/jobs/{jobId}/close
GET    /api/jobs
GET    /api/jobs/{jobId}
GET    /api/jobs/recruiter/my-jobs
```

## Resumes

```http
POST  /api/resumes/upload-target
POST  /api/resumes/upload
GET   /api/resumes
PATCH /api/resumes/{resumeId}/activate
GET   /api/resumes/{resumeId}
```

## Applications

```http
POST  /api/applications/jobs/{jobId}/apply
GET   /api/applications/me
GET   /api/applications/recruiter/jobs/{jobId}
PATCH /api/applications/{applicationId}/status
```

## Dashboard

```http
GET /api/dashboard/recruiter
```

## Assessments

Assessment endpoints are being developed as part of the assessment module and may evolve as the functionality is expanded.

---

# 🧰 Technology Stack

## Backend

| Technology        | Purpose                          |
| ----------------- | -------------------------------- |
| Java 21           | Programming language             |
| Spring Boot 4.0.5 | Backend framework                |
| Spring Web MVC    | REST APIs                        |
| Spring Security   | Authentication and authorization |
| JWT               | Stateless authentication         |
| Spring Data JPA   | Database access                  |
| PostgreSQL        | Relational database              |
| Flyway            | Database migration               |
| Lombok            | Boilerplate reduction            |
| Maven             | Build and dependency management  |

---

# 🚀 Running the Project

## Prerequisites

Install:

* Java 21
* PostgreSQL
* Maven
* Git

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

---

## Clone Repository

```bash
git clone https://github.com/Vivekgupta1201/ai-powered-job-portal.git
```

Move into the project:

```bash
cd ai-powered-job-portal
```

---

# 🗃️ Create Database

Open PostgreSQL and create:

```sql
CREATE DATABASE jobportal_db;
```

---

# ⚙️ Configure Application

Update:

```text
src/main/resources/application.properties
```

with your local PostgreSQL configuration.

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jobportal_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Do not commit production passwords, API keys, JWT secrets, or other sensitive credentials to GitHub.

For production, environment variables or a secure secrets-management solution should be used.

---

# ▶️ Run Application

### Windows

```cmd
mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

Flyway migrations will run when the application starts.

---

# 🧪 Testing

The APIs can be tested using:

* Postman
* Eclipse
* Swagger UI, if enabled
* Browser for public endpoints

## Recommended Testing Flow

### Step 1 — Register Job Seeker

```text
POST /api/auth/register/job-seeker
```

### Step 2 — Register Recruiter

```text
POST /api/auth/register/recruiter
```

### Step 3 — Login

```text
POST /api/auth/login
```

Store the returned JWT.

### Step 4 — Create Profile

Use the JWT in:

```text
Authorization: Bearer <JWT>
```

### Step 5 — Create Company

Recruiter creates the company.

### Step 6 — Create Job

Recruiter creates a job posting.

### Step 7 — Publish Job

Recruiter publishes the job.

### Step 8 — Browse Jobs

Job seeker searches for available jobs.

### Step 9 — Upload Resume

Job seeker uploads a resume.

### Step 10 — Apply

Job seeker applies for a job.

### Step 11 — Recruiter Reviews Application

Recruiter views applicants.

### Step 12 — Update Application Status

Recruiter can shortlist or reject candidates.

### Step 13 — View Dashboard

Recruiter checks recruitment statistics.

### Step 14 — Assessment

Recruiter creates an assessment and the candidate can attempt and submit it.

---

# 📈 Future Roadmap

## Phase 1 — Backend Foundation

* [x] Authentication
* [x] JWT security
* [x] Role-based authorization
* [x] Profile management
* [x] Company management
* [x] Job management
* [x] Resume management
* [x] Application workflow
* [x] Recruiter dashboard

## Phase 2 — Assessment Platform

* [x] Assessment module foundation
* [x] Assessment entities
* [x] Question management
* [x] Options
* [x] Attempts
* [x] Answers
* [x] Assessment results
* [ ] Timed assessments
* [ ] Question randomization
* [ ] Assessment analytics
* [ ] Advanced scoring

## Phase 3 — Frontend

* [ ] React / Next.js frontend
* [ ] Job seeker dashboard
* [ ] Recruiter dashboard
* [ ] Job search
* [ ] Profile management
* [ ] Resume management
* [ ] Application tracking
* [ ] Assessment interface

## Phase 4 — AI Integration

* [ ] AI resume parsing
* [ ] AI skill extraction
* [ ] AI job matching
* [ ] AI candidate ranking
* [ ] AI assessment question generation
* [ ] AI interview question generation

## Phase 5 — Production Features

* [ ] AWS S3
* [ ] Email notifications
* [ ] Google authentication
* [ ] LinkedIn authentication
* [ ] Admin panel
* [ ] Recruiter premium plans
* [ ] Payment integration
* [ ] Analytics
* [ ] Monitoring and logging
* [ ] Docker deployment
* [ ] CI/CD pipeline

---

# 📊 Current Project Status

The project has progressed beyond the initial job portal backend foundation.

### Completed

* Authentication
* JWT-based security
* Role-based authorization
* User profile management
* Company management
* Job management
* Resume management
* Application management
* Application status tracking
* Recruiter dashboard
* Assessment module foundation
* Assessment database migration
* Centralized exception handling

### In Progress / Planned

* Frontend integration
* AI resume processing
* AI candidate matching
* AI assessment generation
* Advanced assessment functionality
* Cloud file storage
* Notifications
* Production deployment

---

# 🔮 Vision

The long-term goal is to evolve this project from a traditional job portal into an **AI-powered recruitment platform**.

The expected future workflow is:

```text
Candidate
   │
   ▼
Create Profile
   │
   ▼
Upload Resume
   │
   ▼
AI Resume Analysis
   │
   ├── Skills
   ├── Experience
   ├── Education
   └── Technologies
   │
   ▼
AI Job Matching
   │
   ▼
Recommended Jobs
   │
   ▼
Application
   │
   ▼
AI / Recruiter Assessment
   │
   ▼
Candidate Evaluation
   │
   ▼
Recruiter Decision
```

For recruiters:

```text
Recruiter
   │
   ▼
Create Company
   │
   ▼
Create Job
   │
   ▼
Publish Job
   │
   ▼
Receive Applications
   │
   ▼
AI Candidate Matching
   │
   ▼
Candidate Ranking
   │
   ▼
Assessment
   │
   ▼
Recruiter Review
   │
   ▼
Shortlist / Reject
```

This architecture provides a foundation for integrating intelligent recruitment capabilities without replacing the existing core backend.

---

# 🔒 Security Considerations

The project follows several security principles:

* Passwords should never be stored in plain text.
* Protected APIs require authentication.
* JWT tokens are used for stateless authentication.
* Role-based authorization is enforced on the backend.
* Recruiter and Job Seeker permissions are separated.
* DTOs are used instead of directly exposing entities.
* Sensitive credentials should not be committed to source control.
* Database credentials should be externalized in production.
* File access should use secure URLs when cloud storage is introduced.

---

# 🤝 Development Guidelines

When extending the project:

### Controllers

Controllers should primarily handle:

* HTTP requests
* Request validation
* Calling services
* Returning responses

Business logic should not be placed directly inside controllers.

### Services

Services should contain:

* Business rules
* Validation
* Workflow processing
* Transactional operations

### Repositories

Repositories should handle:

* Database operations
* Queries
* Persistence

### DTOs

DTOs should be preferred for:

* Request payloads
* Response payloads
* API contracts

Entities should not be exposed directly unless there is a specific reason.

### Database

All database schema changes should be added through Flyway migrations.

---

# 📄 License

This project is currently available for development and learning purposes.

Add an appropriate open-source license before distributing or publishing the project for public use.
