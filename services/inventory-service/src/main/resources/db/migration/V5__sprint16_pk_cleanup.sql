-- V5: Cleanup after PK switch monitoring period

DROP TABLE IF EXISTS old_inventory;

ALTER TABLE inventory DROP COLUMN IF EXISTS id_legacy;
ALTER TABLE inventory DROP COLUMN IF EXISTS product_id;
