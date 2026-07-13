CREATE TABLE IF NOT EXISTS ai_decisions (
    id UUID PRIMARY KEY,
    request_id UUID,
    action VARCHAR(50),
    status VARCHAR(50),
    confidence VARCHAR(50),
    summary TEXT,
    processing_time_ms BIGINT,
    requires_approval BOOLEAN DEFAULT FALSE,
    overrideable BOOLEAN DEFAULT FALSE,
    timestamp TIMESTAMP,
    created_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_decision_rules (
    id UUID PRIMARY KEY,
    name VARCHAR(200),
    description TEXT,
    action VARCHAR(50),
    priority INT DEFAULT 0,
    weight INT DEFAULT 1,
    conditions TEXT,
    overrides TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_decision_audit (
    id UUID PRIMARY KEY,
    request_id UUID,
    decision_id UUID,
    action VARCHAR(50),
    status VARCHAR(50),
    confidence VARCHAR(50),
    reasons TEXT,
    context TEXT,
    user_id VARCHAR(100),
    processing_time_ms BIGINT,
    success BOOLEAN,
    timestamp TIMESTAMP,
    created_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_decision_explanations (
    id UUID PRIMARY KEY,
    decision_id UUID,
    summary TEXT,
    matched_policies TEXT,
    triggered_rules TEXT,
    confidence VARCHAR(50),
    reasoning TEXT,
    evidence TEXT,
    recommended_action VARCHAR(200),
    audit_metadata TEXT,
    explanation_text TEXT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_decision_history (
    id UUID PRIMARY KEY,
    decision_id UUID,
    from_status VARCHAR(50),
    to_status VARCHAR(50),
    triggered_by VARCHAR(100),
    reason TEXT,
    timestamp TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_decision_registry (
    id UUID PRIMARY KEY,
    name VARCHAR(200),
    module VARCHAR(100),
    endpoint VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    is_registered BOOLEAN DEFAULT FALSE,
    config TEXT,
    registered_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_decision_request_id ON ai_decisions(request_id);
CREATE INDEX IF NOT EXISTS idx_decision_action ON ai_decisions(action);
CREATE INDEX IF NOT EXISTS idx_decision_status ON ai_decisions(status);
CREATE INDEX IF NOT EXISTS idx_decision_confidence ON ai_decisions(confidence);
CREATE INDEX IF NOT EXISTS idx_decision_created_at ON ai_decisions(created_at);

CREATE INDEX IF NOT EXISTS idx_decision_rules_action ON ai_decision_rules(action);
CREATE INDEX IF NOT EXISTS idx_decision_rules_created_at ON ai_decision_rules(created_at);

CREATE INDEX IF NOT EXISTS idx_decision_audit_request ON ai_decision_audit(request_id);
CREATE INDEX IF NOT EXISTS idx_decision_audit_decision_id ON ai_decision_audit(decision_id);
CREATE INDEX IF NOT EXISTS idx_decision_audit_user ON ai_decision_audit(user_id);
CREATE INDEX IF NOT EXISTS idx_decision_audit_action ON ai_decision_audit(action);
CREATE INDEX IF NOT EXISTS idx_decision_audit_status ON ai_decision_audit(status);
CREATE INDEX IF NOT EXISTS idx_decision_audit_confidence ON ai_decision_audit(confidence);
CREATE INDEX IF NOT EXISTS idx_decision_audit_created_at ON ai_decision_audit(created_at);

CREATE INDEX IF NOT EXISTS idx_decision_explanations_decision ON ai_decision_explanations(decision_id);

CREATE INDEX IF NOT EXISTS idx_decision_history_decision ON ai_decision_history(decision_id);

CREATE INDEX IF NOT EXISTS idx_decision_registry_module ON ai_decision_registry(module);
CREATE INDEX IF NOT EXISTS idx_decision_registry_created_at ON ai_decision_registry(created_at);
