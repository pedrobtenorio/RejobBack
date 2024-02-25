-- liquibase formatted sql

-- Changeset pedro.tenorio:Create job_applications table

CREATE TABLE job_applications (
                                  id SERIAL PRIMARY KEY,
                                  applicant_id BIGINT NOT NULL,
                                  job_id BIGINT NOT NULL,
                                  application_date TIMESTAMP NOT NULL,
                                  status VARCHAR(50) NOT NULL,
                                  feedback TEXT,
                                  FOREIGN KEY (applicant_id) REFERENCES employees(id),
                                  FOREIGN KEY (job_id) REFERENCES job(id)
);
