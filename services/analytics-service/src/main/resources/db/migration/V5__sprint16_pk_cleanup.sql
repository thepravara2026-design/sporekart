-- V5: Cleanup after PK switch monitoring period
-- Drops old_ tables and legacy id_uuid columns.

DROP TABLE IF EXISTS old_analytics_snapshots;
DROP TABLE IF EXISTS old_dashboard_widgets;
DROP TABLE IF EXISTS old_reports;
DROP TABLE IF EXISTS old_seo_metadata;

ALTER TABLE analytics_snapshots DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE analytics_snapshots DROP COLUMN IF EXISTS id_legacy;

ALTER TABLE dashboard_widgets DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE dashboard_widgets DROP COLUMN IF EXISTS id_legacy;

ALTER TABLE reports DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE reports DROP COLUMN IF EXISTS id_legacy;

ALTER TABLE seo_metadata DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE seo_metadata DROP COLUMN IF EXISTS id_legacy;
