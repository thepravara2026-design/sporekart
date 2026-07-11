-- V5: Cleanup after PK switch monitoring period

DROP TABLE IF EXISTS old_payments;

ALTER TABLE payments DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE payments DROP COLUMN IF EXISTS id_legacy;
