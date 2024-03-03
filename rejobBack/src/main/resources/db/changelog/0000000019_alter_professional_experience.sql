-- liquibase formatted sql

-- Changeset pedro.tenorio:alter professional_experience type from varchar to text

ALTER TABLE employees
  ALTER COLUMN professional_experience TYPE TEXT;
