-- liquibase formatted sql

-- Changeset pedro.tenorio:changes to company

ALTER TABLE companies
    ADD COLUMN email VARCHAR(255),
    ADD COLUMN city VARCHAR(255),
    ADD COLUMN state VARCHAR(255),
    ADD COLUMN address TEXT;
