-- V4: PK switch for identity-service users table to UUID
-- Preconditions:
--  - V3__sprint16_uuid_backfill.sql has run and id_uuid populated.
--  - RBAC tables (roles, permissions) keep their existing PK types (BIGSERIAL, VARCHAR).
--  - user_roles, role_permissions join tables are updated to reference users by UUID.

-- 1) Create tmp_users with UUID PK
CREATE TABLE IF NOT EXISTS tmp_users (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    email VARCHAR(320) NOT NULL,
    phone VARCHAR(32),
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(120),
    last_name VARCHAR(120),
    status VARCHAR(20) NOT NULL,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_login_at TIMESTAMP WITH TIME ZONE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO tmp_users (id, id_legacy, email, phone, password_hash, first_name, last_name, status, email_verified, phone_verified, created_at, updated_at, last_login_at, deleted)
SELECT id_uuid, id, email, phone, password_hash, first_name, last_name, status, email_verified, phone_verified, created_at, updated_at, last_login_at, deleted FROM users;

CREATE INDEX IF NOT EXISTS idx_tmp_users_email ON tmp_users(email);
CREATE INDEX IF NOT EXISTS idx_tmp_users_status ON tmp_users(status);

-- 2) Backfill user_id_uuid in child tables
ALTER TABLE user_roles ADD COLUMN IF NOT EXISTS user_id_uuid UUID;
UPDATE user_roles ur SET user_id_uuid = u.id_uuid FROM users u WHERE u.id = ur.user_id;

ALTER TABLE refresh_tokens ADD COLUMN IF NOT EXISTS user_id_uuid UUID;
UPDATE refresh_tokens rt SET user_id_uuid = u.id_uuid FROM users u WHERE u.id = rt.user_id;

ALTER TABLE otp_requests ADD COLUMN IF NOT EXISTS user_id_uuid UUID;
UPDATE otp_requests o SET user_id_uuid = u.id_uuid FROM users u WHERE u.id = o.user_id;

ALTER TABLE audit_events ADD COLUMN IF NOT EXISTS actor_id_uuid UUID;
UPDATE audit_events ae SET actor_id_uuid = u.id_uuid FROM users u WHERE u.id = ae.actor_id;

-- 3) Automated swap
ALTER TABLE users RENAME TO old_users;
ALTER TABLE tmp_users RENAME TO users;

-- 4) Update FK references to use UUID columns
-- Drop old FK constraints if any
ALTER TABLE user_roles DROP CONSTRAINT IF EXISTS fk_user_roles_user;

-- The user_roles join table keeps the VARCHAR user_id for backward compat during monitoring,
-- but new entries should use user_id_uuid.
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id_uuid ON user_roles(user_id_uuid);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user_id_uuid ON refresh_tokens(user_id_uuid);
CREATE INDEX IF NOT EXISTS idx_otp_requests_user_id_uuid ON otp_requests(user_id_uuid);
CREATE INDEX IF NOT EXISTS idx_audit_events_actor_id_uuid ON audit_events(actor_id_uuid);
