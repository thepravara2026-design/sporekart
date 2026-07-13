CREATE TABLE IF NOT EXISTS ai_governance_metrics (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    module VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    value DOUBLE NOT NULL,
    labels TEXT,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_governance_dashboards (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    widgets TEXT,
    configuration TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_governance_reports (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    data TEXT,
    summary TEXT,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    generated_by UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_governance_kpis (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    module VARCHAR(100) NOT NULL,
    current_value DOUBLE NOT NULL,
    target_value DOUBLE NOT NULL,
    threshold DOUBLE DEFAULT 0.0,
    status VARCHAR(20) NOT NULL,
    dimensions TEXT,
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_governance_snapshots (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    data TEXT,
    captured_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_governance_exports (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    report_id UUID REFERENCES ai_governance_reports(id),
    format VARCHAR(20) NOT NULL,
    file_name VARCHAR(255),
    file_size BIGINT DEFAULT 0,
    metadata TEXT,
    exported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    exported_by UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_governance_report_schedule (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    report_type VARCHAR(50) NOT NULL,
    frequency VARCHAR(20) NOT NULL,
    configuration TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_by UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_governance_metrics_module ON ai_governance_metrics(module);
CREATE INDEX IF NOT EXISTS idx_governance_metrics_name ON ai_governance_metrics(name);
CREATE INDEX IF NOT EXISTS idx_governance_metrics_recorded ON ai_governance_metrics(recorded_at);
CREATE INDEX IF NOT EXISTS idx_governance_reports_type ON ai_governance_reports(type);
CREATE INDEX IF NOT EXISTS idx_governance_kpis_module ON ai_governance_kpis(module);
CREATE INDEX IF NOT EXISTS idx_governance_kpis_status ON ai_governance_kpis(status);
CREATE INDEX IF NOT EXISTS idx_governance_snapshots_name ON ai_governance_snapshots(name);
CREATE INDEX IF NOT EXISTS idx_governance_exports_report ON ai_governance_exports(report_id);
CREATE INDEX IF NOT EXISTS idx_governance_schedule_active ON ai_governance_report_schedule(active);
