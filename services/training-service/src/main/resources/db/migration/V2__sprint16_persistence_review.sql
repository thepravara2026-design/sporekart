ALTER TABLE training_programs
ALTER TABLE training_programs ADD COLUMN created_by UUID;
ALTER TABLE training_programs ADD COLUMN updated_by UUID;
ALTER TABLE training_programs ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_training_programs_is_deleted ON training_programs(is_deleted);

ALTER TABLE growers
ALTER TABLE growers ADD COLUMN created_by UUID;
ALTER TABLE growers ADD COLUMN updated_by UUID;
ALTER TABLE growers ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_growers_is_deleted ON growers(is_deleted);
