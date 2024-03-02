-- liquibase formatted sql

-- Changeset pedro.tenorio:add location to employees

ALTER table employees
    ADD COLUMN city VARCHAR(255),
    ADD COLUMN state VARCHAR(255),
    ADD COLUMN address TEXT;