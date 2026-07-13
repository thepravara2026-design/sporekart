CREATE TABLE IF NOT EXISTS ai_policies (
    id UUID PRIMARY KEY,
    name VARCHAR(200),
    description TEXT,
    type VARCHAR(50),
    status VARCHAR(50),
    severity VARCHAR(50),
    scope VARCHAR(50),
    priority INT DEFAULT 0,
    module VARCHAR(100),
    rules TEXT,
    conditions TEXT,
    metadata TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    is_system BOOLEAN DEFAULT FALSE,
    created_by UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_policy_rules (
    id UUID PRIMARY KEY,
    policy_id UUID,
    name VARCHAR(200),
    description TEXT,
    expression TEXT,
    parameters TEXT,
    decision VARCHAR(50),
    rule_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_policy_conditions (
    id UUID PRIMARY KEY,
    rule_id UUID,
    field VARCHAR(200),
    operator VARCHAR(50),
    condition_value TEXT,
    negate BOOLEAN DEFAULT FALSE,
    condition_order INT DEFAULT 0,
    created_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_policy_versions (
    id UUID PRIMARY KEY,
    policy_id UUID,
    version_number INT,
    name VARCHAR(200),
    description TEXT,
    content TEXT,
    status VARCHAR(50),
    change_notes TEXT,
    created_by UUID,
    created_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_policy_evaluations (
    id UUID PRIMARY KEY,
    request_id UUID,
    policy_id UUID,
    decision VARCHAR(50),
    violations TEXT,
    context TEXT,
    evaluation_time_ms BIGINT,
    rules_evaluated INT,
    rules_passed INT,
    rules_failed INT,
    matched BOOLEAN,
    timestamp TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_policy_audit (
    id UUID PRIMARY KEY,
    policy_id UUID,
    request_id UUID,
    action VARCHAR(200),
    decision VARCHAR(50),
    violations TEXT,
    details TEXT,
    user_id VARCHAR(100),
    processing_time_ms BIGINT,
    success BOOLEAN,
    timestamp TIMESTAMP,
    created_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_policy_registry (
    id UUID PRIMARY KEY,
    name VARCHAR(200),
    module VARCHAR(100),
    type VARCHAR(50),
    scope VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    is_registered BOOLEAN DEFAULT FALSE,
    config TEXT,
    registered_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_policies_type ON ai_policies(type);
CREATE INDEX IF NOT EXISTS idx_policies_status ON ai_policies(status);
CREATE INDEX IF NOT EXISTS idx_policies_scope ON ai_policies(scope);
CREATE INDEX IF NOT EXISTS idx_policies_module ON ai_policies(module);
CREATE INDEX IF NOT EXISTS idx_policies_created_at ON ai_policies(created_at);

CREATE INDEX IF NOT EXISTS idx_policy_rules_policy ON ai_policy_rules(policy_id);
CREATE INDEX IF NOT EXISTS idx_policy_rules_created_at ON ai_policy_rules(created_at);

CREATE INDEX IF NOT EXISTS idx_policy_conditions_rule ON ai_policy_conditions(rule_id);
CREATE INDEX IF NOT EXISTS idx_policy_conditions_created_at ON ai_policy_conditions(created_at);

CREATE INDEX IF NOT EXISTS idx_policy_versions_policy ON ai_policy_versions(policy_id);
CREATE INDEX IF NOT EXISTS idx_policy_versions_created_at ON ai_policy_versions(created_at);

CREATE INDEX IF NOT EXISTS idx_policy_evaluations_request ON ai_policy_evaluations(request_id);
CREATE INDEX IF NOT EXISTS idx_policy_evaluations_policy ON ai_policy_evaluations(policy_id);
CREATE INDEX IF NOT EXISTS idx_policy_evaluations_decision ON ai_policy_evaluations(decision);
CREATE INDEX IF NOT EXISTS idx_policy_evaluations_timestamp ON ai_policy_evaluations(timestamp);

CREATE INDEX IF NOT EXISTS idx_policy_audit_policy ON ai_policy_audit(policy_id);
CREATE INDEX IF NOT EXISTS idx_policy_audit_request ON ai_policy_audit(request_id);
CREATE INDEX IF NOT EXISTS idx_policy_audit_decision ON ai_policy_audit(decision);
CREATE INDEX IF NOT EXISTS idx_policy_audit_user ON ai_policy_audit(user_id);
CREATE INDEX IF NOT EXISTS idx_policy_audit_created_at ON ai_policy_audit(created_at);

CREATE INDEX IF NOT EXISTS idx_policy_registry_module ON ai_policy_registry(module);
CREATE INDEX IF NOT EXISTS idx_policy_registry_type ON ai_policy_registry(type);
CREATE INDEX IF NOT EXISTS idx_policy_registry_scope ON ai_policy_registry(scope);
CREATE INDEX IF NOT EXISTS idx_policy_registry_updated_at ON ai_policy_registry(updated_at);
