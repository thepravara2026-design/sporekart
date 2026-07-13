CREATE TABLE IF NOT EXISTS ai_assistants (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    type VARCHAR(50),
    status VARCHAR(50),
    config TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_by UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_assistant_profiles (
    id UUID PRIMARY KEY,
    assistant_id UUID NOT NULL,
    display_name VARCHAR(200),
    welcome_message TEXT,
    capabilities TEXT,
    prompt_overrides TEXT,
    security_config TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_assistant_sessions (
    id UUID PRIMARY KEY,
    assistant_id UUID NOT NULL,
    user_id UUID,
    conversation_id UUID,
    context TEXT,
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    expires_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_assistant_intents (
    id UUID PRIMARY KEY,
    session_id UUID NOT NULL,
    user_input TEXT,
    resolved_intent VARCHAR(100),
    confidence DOUBLE,
    status VARCHAR(50),
    priority VARCHAR(20),
    metadata TEXT,
    entities TEXT,
    fallback_intent VARCHAR(100),
    created_at TIMESTAMP,
    resolved_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_assistant_tasks (
    id UUID PRIMARY KEY,
    session_id UUID NOT NULL,
    intent_id UUID,
    name VARCHAR(200),
    description TEXT,
    status VARCHAR(50),
    priority INT,
    input TEXT,
    output TEXT,
    dependencies TEXT,
    retry_count INT DEFAULT 0,
    max_retries INT DEFAULT 3,
    timeout_ms BIGINT DEFAULT 30000,
    error_message TEXT,
    created_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_assistant_executions (
    id UUID PRIMARY KEY,
    assistant_id UUID NOT NULL,
    session_id UUID NOT NULL,
    task_id UUID,
    copilot_type VARCHAR(50),
    action VARCHAR(200),
    request TEXT,
    response TEXT,
    success BOOLEAN,
    latency_ms BIGINT,
    error_message TEXT,
    created_at TIMESTAMP,
    completed_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_assistant_feedback (
    id UUID PRIMARY KEY,
    session_id UUID NOT NULL,
    user_id UUID,
    rating INT,
    comment TEXT,
    category VARCHAR(100),
    metadata TEXT,
    helpful BOOLEAN,
    created_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_assistant_metrics (
    id UUID PRIMARY KEY,
    assistant_id UUID NOT NULL,
    total_requests BIGINT,
    successful_requests BIGINT,
    failed_requests BIGINT,
    avg_latency_ms DOUBLE,
    intent_accuracy DOUBLE,
    tasks_created BIGINT,
    tasks_completed BIGINT,
    tasks_failed BIGINT,
    recorded_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_ai_assistants_status ON ai_assistants(status);
CREATE INDEX IF NOT EXISTS idx_ai_assistants_created_at ON ai_assistants(created_at);

CREATE INDEX IF NOT EXISTS idx_ai_profiles_assistant ON ai_assistant_profiles(assistant_id);
CREATE INDEX IF NOT EXISTS idx_ai_profiles_created_at ON ai_assistant_profiles(created_at);

CREATE INDEX IF NOT EXISTS idx_ai_sessions_assistant ON ai_assistant_sessions(assistant_id);
CREATE INDEX IF NOT EXISTS idx_ai_sessions_user ON ai_assistant_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_ai_sessions_status ON ai_assistant_sessions(status);
CREATE INDEX IF NOT EXISTS idx_ai_sessions_created_at ON ai_assistant_sessions(created_at);

CREATE INDEX IF NOT EXISTS idx_ai_intents_session ON ai_assistant_intents(session_id);
CREATE INDEX IF NOT EXISTS idx_ai_intents_status ON ai_assistant_intents(status);
CREATE INDEX IF NOT EXISTS idx_ai_intents_created_at ON ai_assistant_intents(created_at);

CREATE INDEX IF NOT EXISTS idx_ai_tasks_session ON ai_assistant_tasks(session_id);
CREATE INDEX IF NOT EXISTS idx_ai_tasks_status ON ai_assistant_tasks(status);
CREATE INDEX IF NOT EXISTS idx_ai_tasks_created_at ON ai_assistant_tasks(created_at);

CREATE INDEX IF NOT EXISTS idx_ai_executions_assistant ON ai_assistant_executions(assistant_id);
CREATE INDEX IF NOT EXISTS idx_ai_executions_session ON ai_assistant_executions(session_id);
CREATE INDEX IF NOT EXISTS idx_ai_executions_created_at ON ai_assistant_executions(created_at);

CREATE INDEX IF NOT EXISTS idx_ai_feedback_session ON ai_assistant_feedback(session_id);
CREATE INDEX IF NOT EXISTS idx_ai_feedback_user ON ai_assistant_feedback(user_id);
CREATE INDEX IF NOT EXISTS idx_ai_feedback_created_at ON ai_assistant_feedback(created_at);

CREATE INDEX IF NOT EXISTS idx_ai_metrics_assistant ON ai_assistant_metrics(assistant_id);
CREATE INDEX IF NOT EXISTS idx_ai_metrics_recorded_at ON ai_assistant_metrics(recorded_at);
