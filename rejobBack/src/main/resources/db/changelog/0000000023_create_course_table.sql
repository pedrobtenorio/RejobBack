-- liquibase formatted sql

-- Changeset fred:create course table

CREATE TABLE course (
                        id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                        contact_person_id BIGINT,
                        courseTitle VARCHAR(255),
                        platform VARCHAR(255),
                        link TEXT,
                        description TEXT,
                        duration VARCHAR(50)
);

ALTER TABLE course
    ADD CONSTRAINT fk_contact_person
        FOREIGN KEY (contact_person_id)
            REFERENCES collaborators(id);