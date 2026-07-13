CREATE TABLE IF NOT EXISTS ai_risk_assessments (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    module VARCHAR(100) NOT NULL,
    action VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    context TEXT,
    reviewer_id UUID,
    assessed_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_risk_scores (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    assessment_id UUID REFERENCES ai_risk_assessments(id),
    overall_score DOUBLE NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    category_scores TEXT,
    factor_count INT DEFAULT 0,
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_risk_factors (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    assessment_id UUID REFERENCES ai_risk_assessments(id),
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    weight DOUBLE DEFAULT 1.0,
    score DOUBLE DEFAULT 0.0,
    evidence TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_trust_scores (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    assessment_id UUID REFERENCES ai_risk_assessments(id),
    overall_trust_score DOUBLE NOT NULL,
    factor_scores TEXT,
    factor_reasons TEXT,
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_confidence_scores (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    assessment_id UUID REFERENCES ai_risk_assessments(id),
    overall_confidence DOUBLE NOT NULL,
    factor_scores TEXT,
    explanation TEXT,
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_risk_history (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    assessment_id UUID REFERENCES ai_risk_assessments(id),
    event_type VARCHAR(100) NOT NULL,
    description TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_risk_recommendations (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    assessment_id UUID REFERENCES ai_risk_assessments(id),
    type VARCHAR(50) NOT NULL,
    title VARCHAR(255),
    description TEXT,
    details TEXT,
    priority INT DEFAULT 5,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_risk_audit (
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

CREATE INDEX IF NOT EXISTS idx_risk_assessments_module ON ai_risk_assessments(module);
CREATE INDEX IF NOT EXISTS idx_risk_assessments_status ON ai_risk_assessments(status);
CREATE INDEX IF NOT EXISTS idx_risk_scores_assessment ON ai_risk_scores(assessment_id);
CREATE INDEX IF NOT EXISTS idx_risk_scores_level ON ai_risk_scores(risk_level);
CREATE INDEX IF NOT EXISTS idx_risk_factors_assessment ON ai_risk_factors(assessment_id);
CREATE INDEX IF NOT EXISTS idx_trust_scores_assessment ON ai_trust_scores(assessment_id);
CREATE INDEX IF NOT EXISTS idx_confidence_scores_assessment ON ai_confidence_scores(assessment_id);
CREATE INDEX IF NOT EXISTS idx_risk_history_assessment ON ai_risk_history(assessment_id);
CREATE INDEX IF NOT EXISTS idx_risk_recommendations_assessment ON ai_risk_recommendations(assessment_id);
CREATE INDEX IF NOT EXISTS idx_risk_audit_entity ON ai_risk_audit(entity_id);
CREATE INDEX IF NOT EXISTS idx_risk_audit_timestamp ON ai_risk_audit(timestamp);
