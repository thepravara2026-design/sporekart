-- Backfill UUIDs for analytics-service entities with VARCHAR ids

ALTER TABLE analytics_snapshots
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();
UPDATE analytics_snapshots SET id_uuid = random_uuid() WHERE id_uuid IS NULL;
ALTER TABLE analytics_snapshots ADD CONSTRAINT uq_analytics_snapshots_id_uuid UNIQUE (id_uuid);
CREATE INDEX IF NOT EXISTS idx_analytics_snapshots_id_uuid ON analytics_snapshots(id_uuid);

ALTER TABLE dashboard_widgets
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();
UPDATE dashboard_widgets SET id_uuid = random_uuid() WHERE id_uuid IS NULL;
ALTER TABLE dashboard_widgets ADD CONSTRAINT uq_dashboard_widgets_id_uuid UNIQUE (id_uuid);
CREATE INDEX IF NOT EXISTS idx_dashboard_widgets_id_uuid ON dashboard_widgets(id_uuid);

ALTER TABLE reports
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();
UPDATE reports SET id_uuid = random_uuid() WHERE id_uuid IS NULL;
ALTER TABLE reports ADD CONSTRAINT uq_reports_id_uuid UNIQUE (id_uuid);
CREATE INDEX IF NOT EXISTS idx_reports_id_uuid ON reports(id_uuid);

ALTER TABLE seo_metadata
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();
UPDATE seo_metadata SET id_uuid = random_uuid() WHERE id_uuid IS NULL;
ALTER TABLE seo_metadata ADD CONSTRAINT uq_seo_metadata_id_uuid UNIQUE (id_uuid);
CREATE INDEX IF NOT EXISTS idx_seo_metadata_id_uuid ON seo_metadata(id_uuid);
