-- V4: PK switch for analytics-service tables to UUID
-- Preconditions: V3__sprint16_uuid_backfill.sql has run and id_uuid populated.

-- 1) Create tmp tables with UUID PK and swap each table

CREATE TABLE IF NOT EXISTS tmp_analytics_snapshots (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    metric_name VARCHAR(100) NOT NULL,
    metric_value DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tmp_analytics_snapshots (id, id_legacy, metric_name, metric_value, created_at)
SELECT id_uuid, id, metric_name, metric_value, created_at FROM analytics_snapshots;

CREATE TABLE IF NOT EXISTS tmp_dashboard_widgets (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    name VARCHAR(100) NOT NULL,
    metric VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tmp_dashboard_widgets (id, id_legacy, name, metric, created_at)
SELECT id_uuid, id, name, metric, created_at FROM dashboard_widgets;

CREATE TABLE IF NOT EXISTS tmp_reports (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    report_type VARCHAR(100) NOT NULL,
    format VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tmp_reports (id, id_legacy, report_type, format, created_at)
SELECT id_uuid, id, report_type, format, created_at FROM reports;

CREATE TABLE IF NOT EXISTS tmp_seo_metadata (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    path VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tmp_seo_metadata (id, id_legacy, path, title, description, created_at)
SELECT id_uuid, id, path, title, description, created_at FROM seo_metadata;

-- 2) Swap all tables
ALTER TABLE analytics_snapshots RENAME TO old_analytics_snapshots;
ALTER TABLE tmp_analytics_snapshots RENAME TO analytics_snapshots;

ALTER TABLE dashboard_widgets RENAME TO old_dashboard_widgets;
ALTER TABLE tmp_dashboard_widgets RENAME TO dashboard_widgets;

ALTER TABLE reports RENAME TO old_reports;
ALTER TABLE tmp_reports RENAME TO reports;

ALTER TABLE seo_metadata RENAME TO old_seo_metadata;
ALTER TABLE tmp_seo_metadata RENAME TO seo_metadata;
