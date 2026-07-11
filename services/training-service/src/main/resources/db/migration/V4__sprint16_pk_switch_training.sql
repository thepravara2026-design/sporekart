-- V4: PK switch for training-service tables to UUID
-- Preconditions: V3__sprint16_uuid_backfill.sql has run and id_uuid populated.

-- 1) Create tmp_training_programs with UUID PK
CREATE TABLE IF NOT EXISTS tmp_training_programs (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    title VARCHAR(255) NOT NULL,
    category VARCHAR(80) NOT NULL,
    difficulty VARCHAR(40) NOT NULL,
    language VARCHAR(40) NOT NULL,
    duration_hours INTEGER NOT NULL,
    max_seats INTEGER NOT NULL,
    status VARCHAR(40) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tmp_training_programs (id, id_legacy, title, category, difficulty, language, duration_hours, max_seats, status, created_at, updated_at)
SELECT id_uuid, id, title, category, difficulty, language, duration_hours, max_seats, status, created_at, updated_at FROM training_programs;

-- 2) Create tmp_growers with UUID PK
CREATE TABLE IF NOT EXISTS tmp_growers (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    full_name VARCHAR(255) NOT NULL,
    village VARCHAR(120) NOT NULL,
    district VARCHAR(120) NOT NULL,
    state VARCHAR(120) NOT NULL,
    experience VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tmp_growers (id, id_legacy, full_name, village, district, state, experience, created_at, updated_at)
SELECT id_uuid, id, full_name, village, district, state, experience, created_at, updated_at FROM growers;

-- 3) Swap both tables
ALTER TABLE training_programs RENAME TO old_training_programs;
ALTER TABLE tmp_training_programs RENAME TO training_programs;

ALTER TABLE growers RENAME TO old_growers;
ALTER TABLE tmp_growers RENAME TO growers;
