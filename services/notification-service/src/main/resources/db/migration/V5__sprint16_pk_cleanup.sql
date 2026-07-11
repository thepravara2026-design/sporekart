-- V5: Cleanup after PK switch monitoring period

DROP TABLE IF EXISTS old_notifications;

ALTER TABLE notifications DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE notifications DROP COLUMN IF EXISTS id_legacy;
