-- liquibase formatted sql

-- Changeset fred:added company type

ALTER TABLE companies

    ADD COLUMN company_type VARCHAR(30);