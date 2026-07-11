-- Backfill UUID column for training-service tables (safe, non-destructive)
-- Adds `id_uuid` column populated with random UUIDs for future PK migration.

ALTER TABLE training_programs
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();

UPDATE training_programs SET id_uuid = random_uuid() WHERE id_uuid IS NULL;

ALTER TABLE training_programs
    ADD CONSTRAINT uq_training_programs_id_uuid UNIQUE (id_uuid);

CREATE INDEX IF NOT EXISTS idx_training_programs_id_uuid ON training_programs(id_uuid);

ALTER TABLE growers
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();

UPDATE growers SET id_uuid = random_uuid() WHERE id_uuid IS NULL;

ALTER TABLE growers
    ADD CONSTRAINT uq_growers_id_uuid UNIQUE (id_uuid);

CREATE INDEX IF NOT EXISTS idx_growers_id_uuid ON growers(id_uuid);

-- Note: Do not drop or alter existing `id` columns in this migration.
