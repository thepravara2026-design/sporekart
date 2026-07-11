-- Add audit and soft-delete columns to analytics-service tables
ALTER TABLE analytics_snapshots ADD COLUMN created_by UUID;
ALTER TABLE analytics_snapshots ADD COLUMN updated_by UUID;
ALTER TABLE analytics_snapshots ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_analytics_snapshots_is_deleted ON analytics_snapshots(is_deleted);

ALTER TABLE dashboard_widgets ADD COLUMN created_by UUID;
ALTER TABLE dashboard_widgets ADD COLUMN updated_by UUID;
ALTER TABLE dashboard_widgets ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_dashboard_widgets_is_deleted ON dashboard_widgets(is_deleted);

ALTER TABLE reports ADD COLUMN created_by UUID;
ALTER TABLE reports ADD COLUMN updated_by UUID;
ALTER TABLE reports ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_reports_is_deleted ON reports(is_deleted);

ALTER TABLE seo_metadata ADD COLUMN created_by UUID;
ALTER TABLE seo_metadata ADD COLUMN updated_by UUID;
ALTER TABLE seo_metadata ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_seo_metadata_is_deleted ON seo_metadata(is_deleted);
