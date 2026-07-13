CREATE TABLE IF NOT EXISTS ai_approval_requests (
    id UUID PRIMARY KEY,
    module VARCHAR(100),
    action VARCHAR(200),
    payload TEXT,
    context TEXT,
    user_id VARCHAR(100),
    roles TEXT,
    reason TEXT,
    urgency VARCHAR(50),
    decision_id UUID,
    metadata TEXT,
    deadline TIMESTAMP,
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_approval_workflows (
    id UUID PRIMARY KEY,
    name VARCHAR(200),
    description TEXT,
    module VARCHAR(100),
    allowed_transitions TEXT,
    max_levels INT DEFAULT 1,
    parallel_enabled BOOLEAN DEFAULT FALSE,
    sequential_enabled BOOLEAN DEFAULT TRUE,
    strategy VARCHAR(50),
    sla_minutes INT DEFAULT 60,
    config TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_approval_reviewers (
    id UUID PRIMARY KEY,
    user_id VARCHAR(100),
    name VARCHAR(200),
    email VARCHAR(200),
    department VARCHAR(100),
    roles TEXT,
    type VARCHAR(50),
    priority INT DEFAULT 0,
    max_assignments INT DEFAULT 10,
    current_assignments INT DEFAULT 0,
    is_available BOOLEAN DEFAULT TRUE,
    last_assigned TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_approval_assignments (
    id UUID PRIMARY KEY,
    request_id UUID,
    reviewer_id UUID,
    reviewer_user_id VARCHAR(100),
    strategy VARCHAR(50),
    level INT DEFAULT 1,
    status VARCHAR(50),
    assigned_at TIMESTAMP,
    responded_at TIMESTAMP,
    deadline TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_approval_history (
    id UUID PRIMARY KEY,
    request_id UUID,
    reviewer_id UUID,
    decision VARCHAR(50),
    comment TEXT,
    details TEXT,
    timestamp TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_approval_comments (
    id UUID PRIMARY KEY,
    request_id UUID,
    reviewer_id UUID,
    comment TEXT,
    type VARCHAR(50),
    timestamp TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_approval_audit (
    id UUID PRIMARY KEY,
    request_id UUID,
    reviewer_id UUID,
    action VARCHAR(200),
    decision VARCHAR(50),
    status VARCHAR(50),
    details TEXT,
    user_id VARCHAR(100),
    processing_time_ms BIGINT,
    success BOOLEAN,
    timestamp TIMESTAMP,
    created_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_approval_escalations (
    id UUID PRIMARY KEY,
    request_id UUID,
    from_reviewer_id UUID,
    to_reviewer_id UUID,
    reason VARCHAR(50),
    details TEXT,
    level INT DEFAULT 1,
    escalated_at TIMESTAMP,
    resolved_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_approval_delegations (
    id UUID PRIMARY KEY,
    request_id UUID,
    from_reviewer_id UUID,
    to_reviewer_id UUID,
    reason TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    delegated_at TIMESTAMP,
    expires_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_approval_requests_user ON ai_approval_requests(user_id);
CREATE INDEX IF NOT EXISTS idx_approval_requests_status ON ai_approval_requests(status);
CREATE INDEX IF NOT EXISTS idx_approval_requests_module ON ai_approval_requests(module);
CREATE INDEX IF NOT EXISTS idx_approval_requests_created_at ON ai_approval_requests(created_at);

CREATE INDEX IF NOT EXISTS idx_approval_workflows_module ON ai_approval_workflows(module);
CREATE INDEX IF NOT EXISTS idx_approval_workflows_created_at ON ai_approval_workflows(created_at);

CREATE INDEX IF NOT EXISTS idx_approval_reviewers_user ON ai_approval_reviewers(user_id);
CREATE INDEX IF NOT EXISTS idx_approval_reviewers_department ON ai_approval_reviewers(department);
CREATE INDEX IF NOT EXISTS idx_approval_reviewers_available ON ai_approval_reviewers(is_available);

CREATE INDEX IF NOT EXISTS idx_approval_assignments_request ON ai_approval_assignments(request_id);
CREATE INDEX IF NOT EXISTS idx_approval_assignments_reviewer_user ON ai_approval_assignments(reviewer_user_id);
CREATE INDEX IF NOT EXISTS idx_approval_assignments_status ON ai_approval_assignments(status);

CREATE INDEX IF NOT EXISTS idx_approval_history_request ON ai_approval_history(request_id);
CREATE INDEX IF NOT EXISTS idx_approval_history_reviewer ON ai_approval_history(reviewer_id);

CREATE INDEX IF NOT EXISTS idx_approval_comments_request ON ai_approval_comments(request_id);
CREATE INDEX IF NOT EXISTS idx_approval_comments_reviewer ON ai_approval_comments(reviewer_id);

CREATE INDEX IF NOT EXISTS idx_approval_audit_request ON ai_approval_audit(request_id);
CREATE INDEX IF NOT EXISTS idx_approval_audit_reviewer ON ai_approval_audit(reviewer_id);
CREATE INDEX IF NOT EXISTS idx_approval_audit_decision ON ai_approval_audit(decision);
CREATE INDEX IF NOT EXISTS idx_approval_audit_user ON ai_approval_audit(user_id);
CREATE INDEX IF NOT EXISTS idx_approval_audit_timestamp ON ai_approval_audit(timestamp);

CREATE INDEX IF NOT EXISTS idx_approval_escalations_request ON ai_approval_escalations(request_id);
CREATE INDEX IF NOT EXISTS idx_approval_escalations_from_reviewer ON ai_approval_escalations(from_reviewer_id);

CREATE INDEX IF NOT EXISTS idx_approval_delegations_request ON ai_approval_delegations(request_id);
CREATE INDEX IF NOT EXISTS idx_approval_delegations_from_reviewer ON ai_approval_delegations(from_reviewer_id);
CREATE INDEX IF NOT EXISTS idx_approval_delegations_active ON ai_approval_delegations(is_active);
