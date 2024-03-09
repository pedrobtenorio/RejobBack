-- liquibase formatted sql

-- Changeset pedro:fix course table

ALTER TABLE course RENAME COLUMN courseTitle TO course_title;
