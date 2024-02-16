-- liquibase formatted sql

-- Changeset fred:drop colum department or area

ALTER TABLE collaborator
    DROP COLUMN department_or_area;
