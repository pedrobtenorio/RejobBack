-- liquibase formatted sql

-- Changeset fred:company now has a fk to user

ALTER TABLE companies
    ADD COLUMN user_id BIGINT UNIQUE;

ALTER TABLE companies
    ADD CONSTRAINT fk_user_id
    FOREIGN KEY (user_id)
    REFERENCES users(id);