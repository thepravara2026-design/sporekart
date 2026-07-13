CREATE TABLE ai_lifecycle_definitions (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    initial_state VARCHAR(50) NOT NULL,
    transitions TEXT,
    config TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE ai_lifecycle_history (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    entity_id UUID NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    from_state VARCHAR(50),
    to_state VARCHAR(50) NOT NULL,
    triggered_by VARCHAR(255),
    reason TEXT,
    transitioned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ai_automation_jobs (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    params TEXT,
    status VARCHAR(20) NOT NULL,
    retry_count INT DEFAULT 0,
    max_retries INT DEFAULT 3,
    scheduled_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE ai_scheduler_tasks (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    job_type VARCHAR(50) NOT NULL,
    frequency VARCHAR(50) NOT NULL,
    cron_expression VARCHAR(255),
    params TEXT,
    active BOOLEAN DEFAULT TRUE,
    last_run_at TIMESTAMP,
    next_run_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE ai_workflow_history (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    execution_id UUID NOT NULL,
    step VARCHAR(255),
    action VARCHAR(100),
    status VARCHAR(20),
    message TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ai_retry_policies (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    max_retries INT DEFAULT 3,
    initial_delay_ms BIGINT DEFAULT 1000,
    max_delay_ms BIGINT DEFAULT 60000,
    backoff_multiplier DOUBLE DEFAULT 2.0,
    retry_on_failure BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE ai_escalation_policies (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    max_levels INT DEFAULT 3,
    escalation_delay_ms BIGINT DEFAULT 300000,
    escalation_target VARCHAR(255),
    notification_channel VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE ai_expiration_policies (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    ttl_ms BIGINT NOT NULL,
    action_on_expiry VARCHAR(100) DEFAULT 'ARCHIVE',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE ai_automation_audit (
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

CREATE INDEX idx_lifecycle_history_entity ON ai_lifecycle_history(entity_id, entity_type);
CREATE INDEX idx_automation_jobs_status ON ai_automation_jobs(status);
CREATE INDEX idx_automation_jobs_type ON ai_automation_jobs(type);
CREATE INDEX idx_scheduler_active ON ai_scheduler_tasks(active);
CREATE INDEX idx_scheduler_frequency ON ai_scheduler_tasks(frequency);
CREATE INDEX idx_workflow_history_execution ON ai_workflow_history(execution_id);
CREATE INDEX idx_automation_audit_entity ON ai_automation_audit(entity_id);
