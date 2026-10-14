CREATE DATABASE IF NOT EXISTS hirepath CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hirepath;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  full_name VARCHAR(100) NOT NULL,
  email VARCHAR(190) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  role ENUM('SEEKER','EMPLOYER','ADMIN') NOT NULL DEFAULT 'SEEKER',
  skills VARCHAR(1000) DEFAULT '',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS jobs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  employer_id BIGINT NOT NULL,
  title VARCHAR(150) NOT NULL,
  company VARCHAR(150) NOT NULL,
  location VARCHAR(150) NOT NULL,
  job_type VARCHAR(40) NOT NULL DEFAULT 'Full-time',
  description TEXT NOT NULL,
  required_skills VARCHAR(1000) DEFAULT '',
  status ENUM('OPEN','CLOSED') NOT NULL DEFAULT 'OPEN',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_jobs_employer FOREIGN KEY (employer_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS applications (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  job_id BIGINT NOT NULL,
  seeker_id BIGINT NOT NULL,
  cover_letter TEXT,
  status ENUM('SUBMITTED','REVIEWING','SHORTLISTED','REJECTED','HIRED') NOT NULL DEFAULT 'SUBMITTED',
  applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_job_seeker (job_id, seeker_id),
  CONSTRAINT fk_app_job FOREIGN KEY (job_id) REFERENCES jobs(id),
  CONSTRAINT fk_app_seeker FOREIGN KEY (seeker_id) REFERENCES users(id)
);

-- Demo employer account is not seeded because password hashes should be generated
-- through the registration form. Register an account and select Employer to post jobs.
