-- liquibase formatted sql

-- Changeset fred:drop colum department or area

ALTER TABLE job
    DROP COLUMN job_type,
    DROP COLUMN requirements;