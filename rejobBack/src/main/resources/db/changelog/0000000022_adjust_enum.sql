-- liquibase formatted sql

-- Changeset pedro.tenorio:adjust enum

UPDATE companies
SET company_type = 'PRIVATE_ENTERPRISE'
WHERE company_type = 'EMPRESA_COMERCIAL';

