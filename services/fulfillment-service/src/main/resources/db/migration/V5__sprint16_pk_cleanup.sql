-- V5: Cleanup after PK switch monitoring period

DROP TABLE IF EXISTS old_shipments;

ALTER TABLE shipments DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE shipments DROP COLUMN IF EXISTS id_legacy;
