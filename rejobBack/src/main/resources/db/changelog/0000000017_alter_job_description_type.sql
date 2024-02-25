-- liquibase formatted sql

-- Changeset fred:alter job_description type from varchar to text

ALTER TABLE job
  ALTER COLUMN job_description TYPE TEXT;
