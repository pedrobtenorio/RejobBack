
-- liquibase formatted sql

-- Changeset pedro.tenorio:sequences


ALTER TABLE job
    ADD COLUMN responsibilities VARCHAR(255),
ADD COLUMN required_experience VARCHAR(255),
ADD COLUMN company_name VARCHAR(255);