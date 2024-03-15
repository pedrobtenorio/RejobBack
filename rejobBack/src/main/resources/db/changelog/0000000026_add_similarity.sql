-- liquibase formatted sql

-- Changeset pedro.tenorio:add location to employees

ALTER table job_applications
    ADD COLUMN similarity FLOAT