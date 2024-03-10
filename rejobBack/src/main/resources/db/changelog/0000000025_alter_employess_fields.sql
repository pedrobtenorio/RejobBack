-- liquibase formatted sql

-- Changeset pedro.tenorio:alter professional_experience type from varchar to text

ALTER TABLE employees
  ALTER COLUMN educational_history TYPE TEXT,
  ALTER COLUMN areas_of_interest TYPE TEXT,
  ALTER COLUMN skills_and_qualifications TYPE TEXT;
