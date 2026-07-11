-- V5: Cleanup after PK switch monitoring period

DROP TABLE IF EXISTS old_products;

ALTER TABLE products DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE products DROP COLUMN IF EXISTS id_legacy;
