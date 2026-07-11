-- Backfill UUID column for identity-service users table
-- users.id already stores UUID strings as VARCHAR(36); this adds a proper UUID column.

ALTER TABLE users
    ADD COLUMN id_uuid UUID;
UPDATE users SET id_uuid = CAST(id AS UUID) WHERE id_uuid IS NULL;
ALTER TABLE users ALTER COLUMN id_uuid SET NOT NULL;

ALTER TABLE users
    ADD CONSTRAINT uq_users_id_uuid UNIQUE (id_uuid);

CREATE INDEX IF NOT EXISTS idx_users_id_uuid ON users(id_uuid);
