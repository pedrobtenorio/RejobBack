-- liquibase formatted sql

-- Changeset pedro.tenorio:Create job_applications constrain

ALTER TABLE job_applications
    ADD CONSTRAINT unique_job_application UNIQUE (applicant_id, job_id);
