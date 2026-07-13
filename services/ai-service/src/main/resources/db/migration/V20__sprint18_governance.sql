CREATE TABLE IF NOT EXISTS ai_governance (
    id UUID PRIMARY KEY,
    name VARCHAR(200),
    description TEXT,
    scope VARCHAR(50),
    status VARCHAR(50),
    priority INT,
    rules TEXT,
    conditions TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_by UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_governance_configuration (
    id UUID PRIMARY KEY,
    config_key VARCHAR(200) UNIQUE,
    config_value TEXT,
    description TEXT,
    scope VARCHAR(50),
    mode VARCHAR(50),
    metadata TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    version INT DEFAULT 1,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_governance_registry (
    id UUID PRIMARY KEY,
    name VARCHAR(200),
    module VARCHAR(100),
    endpoint VARCHAR(500),
    scope VARCHAR(50),
    mode VARCHAR(50),
    config TEXT,
    is_registered BOOLEAN DEFAULT FALSE,
    registered_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_governance_scope (
    id UUID PRIMARY KEY,
    policy_id UUID,
    scope_type VARCHAR(50),
    scope_value VARCHAR(500),
    conditions TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_governance_audit (
    id UUID PRIMARY KEY,
    request_id UUID,
    action VARCHAR(200),
    module VARCHAR(100),
    decision VARCHAR(50),
    violations TEXT,
    context TEXT,
    user_id VARCHAR(100),
    processing_time_ms BIGINT,
    success BOOLEAN,
    timestamp TIMESTAMP,
    created_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_governance_metrics (
    id UUID PRIMARY KEY,
    metric_key VARCHAR(200),
    metric_value TEXT,
    recorded_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_governance_scope ON ai_governance(scope);
CREATE INDEX IF NOT EXISTS idx_governance_status ON ai_governance(status);
CREATE INDEX IF NOT EXISTS idx_governance_created_at ON ai_governance(created_at);

CREATE INDEX IF NOT EXISTS idx_governance_config_scope ON ai_governance_configuration(scope);
CREATE INDEX IF NOT EXISTS idx_governance_config_created_at ON ai_governance_configuration(created_at);

CREATE INDEX IF NOT EXISTS idx_governance_registry_module ON ai_governance_registry(module);
CREATE INDEX IF NOT EXISTS idx_governance_registry_scope ON ai_governance_registry(scope);
CREATE INDEX IF NOT EXISTS idx_governance_registry_created_at ON ai_governance_registry(created_at);

CREATE INDEX IF NOT EXISTS idx_governance_scope_policy ON ai_governance_scope(policy_id);
CREATE INDEX IF NOT EXISTS idx_governance_scope_type ON ai_governance_scope(scope_type);
CREATE INDEX IF NOT EXISTS idx_governance_scope_created_at ON ai_governance_scope(created_at);

CREATE INDEX IF NOT EXISTS idx_governance_audit_request ON ai_governance_audit(request_id);
CREATE INDEX IF NOT EXISTS idx_governance_audit_user ON ai_governance_audit(user_id);
CREATE INDEX IF NOT EXISTS idx_governance_audit_decision ON ai_governance_audit(decision);
CREATE INDEX IF NOT EXISTS idx_governance_audit_module ON ai_governance_audit(module);
CREATE INDEX IF NOT EXISTS idx_governance_audit_created_at ON ai_governance_audit(created_at);

CREATE INDEX IF NOT EXISTS idx_governance_metrics_key ON ai_governance_metrics(metric_key);
CREATE INDEX IF NOT EXISTS idx_governance_metrics_recorded_at ON ai_governance_metrics(recorded_at);
