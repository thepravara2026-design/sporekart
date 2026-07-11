-- V5: Cleanup after PK switch monitoring period

DROP TABLE IF EXISTS old_training_programs;
DROP TABLE IF EXISTS old_growers;

ALTER TABLE training_programs DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE training_programs DROP COLUMN IF EXISTS id_legacy;

ALTER TABLE growers DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE growers DROP COLUMN IF EXISTS id_legacy;
