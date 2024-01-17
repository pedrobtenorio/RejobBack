-- liquibase formatted sql

-- Changeset pedro.tenorio:changes to job

ALTER TABLE job
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP;


-- Change data type to TEXT for specific columns in PostgreSQL
ALTER TABLE job
    ALTER COLUMN responsibilities TYPE TEXT,
    ALTER COLUMN required_experience TYPE TEXT,
    ALTER COLUMN job_description TYPE TEXT,
    ALTER COLUMN benefits TYPE TEXT;

ALTER TABLE job
    ADD COLUMN city VARCHAR(255),
    ADD COLUMN state VARCHAR(255),
    ADD COLUMN address TEXT;
