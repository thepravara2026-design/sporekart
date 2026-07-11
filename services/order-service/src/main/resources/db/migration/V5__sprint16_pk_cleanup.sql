-- V5: Cleanup after PK switch monitoring period

DROP TABLE IF EXISTS old_orders;

ALTER TABLE orders DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE orders DROP COLUMN IF EXISTS id_legacy;
