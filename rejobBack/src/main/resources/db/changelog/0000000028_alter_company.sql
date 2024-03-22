-- liquibase formatted sql

-- Changeset pedro.tenorio:alter institutional_description type from varchar to text

ALTER TABLE companies
  ALTER COLUMN institutional_description TYPE TEXT;
