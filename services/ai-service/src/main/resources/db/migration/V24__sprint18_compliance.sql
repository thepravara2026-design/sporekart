CREATE TABLE IF NOT EXISTS ai_compliance_frameworks (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    version VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    description TEXT,
    authority VARCHAR(255),
    controls TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_compliance_rules (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    framework_id UUID REFERENCES ai_compliance_frameworks(id),
    rule_id VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    risk_level VARCHAR(20) NOT NULL,
    expression TEXT,
    active BOOLEAN DEFAULT TRUE,
    effective_from TIMESTAMP,
    effective_to TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_compliance_assessments (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    framework_id UUID REFERENCES ai_compliance_frameworks(id),
    module VARCHAR(100) NOT NULL,
    action VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    result VARCHAR(20),
    context TEXT,
    reviewer_id UUID,
    assessed_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_compliance_evidence (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    assessment_id UUID REFERENCES ai_compliance_assessments(id),
    evidence_type VARCHAR(100) NOT NULL,
    source VARCHAR(255),
    data TEXT,
    verified BOOLEAN DEFAULT FALSE,
    verified_by UUID,
    collected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verified_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_compliance_reports (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    framework_id UUID REFERENCES ai_compliance_frameworks(id),
    title VARCHAR(255) NOT NULL,
    overall_status VARCHAR(20),
    findings TEXT,
    violations TEXT,
    summary TEXT,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    generated_by UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_compliance_violations (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    rule_id UUID REFERENCES ai_compliance_rules(id),
    assessment_id UUID REFERENCES ai_compliance_assessments(id),
    module VARCHAR(100),
    severity VARCHAR(20) NOT NULL,
    description TEXT,
    details TEXT,
    remediated BOOLEAN DEFAULT FALSE,
    detected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remediated_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_compliance_exceptions (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    rule_id UUID REFERENCES ai_compliance_rules(id),
    assessment_id UUID REFERENCES ai_compliance_assessments(id),
    reason VARCHAR(255) NOT NULL,
    justification TEXT,
    requested_by VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    approved_by VARCHAR(255),
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    decision_at TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_compliance_audit (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id UUID,
    performed_by UUID,
    details TEXT,
    ip_address VARCHAR(45),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_compliance_rules_framework ON ai_compliance_rules(framework_id);
CREATE INDEX IF NOT EXISTS idx_compliance_rules_active ON ai_compliance_rules(active);
CREATE INDEX IF NOT EXISTS idx_compliance_assessments_framework ON ai_compliance_assessments(framework_id);
CREATE INDEX IF NOT EXISTS idx_compliance_assessments_module ON ai_compliance_assessments(module);
CREATE INDEX IF NOT EXISTS idx_compliance_assessments_status ON ai_compliance_assessments(status);
CREATE INDEX IF NOT EXISTS idx_compliance_evidence_assessment ON ai_compliance_evidence(assessment_id);
CREATE INDEX IF NOT EXISTS idx_compliance_reports_framework ON ai_compliance_reports(framework_id);
CREATE INDEX IF NOT EXISTS idx_compliance_violations_assessment ON ai_compliance_violations(assessment_id);
CREATE INDEX IF NOT EXISTS idx_compliance_violations_rule ON ai_compliance_violations(rule_id);
CREATE INDEX IF NOT EXISTS idx_compliance_exceptions_rule ON ai_compliance_exceptions(rule_id);
CREATE INDEX IF NOT EXISTS idx_compliance_exceptions_status ON ai_compliance_exceptions(status);
CREATE INDEX IF NOT EXISTS idx_compliance_audit_entity ON ai_compliance_audit(entity_id);
CREATE INDEX IF NOT EXISTS idx_compliance_audit_timestamp ON ai_compliance_audit(timestamp);
