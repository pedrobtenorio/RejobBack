-- Changeset pedro.tenorio: Add has_new_applicant column to job table

ALTER TABLE job
    ADD COLUMN has_new_applicant BOOLEAN;
