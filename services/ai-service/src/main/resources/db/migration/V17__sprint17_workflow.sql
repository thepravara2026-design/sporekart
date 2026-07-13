CREATE TABLE IF NOT EXISTS ai_workflows (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    status VARCHAR(50) NOT NULL,
    version VARCHAR(50),
    trigger_type VARCHAR(50),
    trigger_config TEXT,
    metadata TEXT,
    is_template BOOLEAN DEFAULT FALSE,
    created_by UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_workflow_versions (
    id UUID PRIMARY KEY,
    workflow_id UUID NOT NULL,
    version VARCHAR(50) NOT NULL,
    definition_json TEXT NOT NULL,
    is_active BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS ai_workflow_steps (
    id UUID PRIMARY KEY,
    workflow_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    step_type VARCHAR(50) NOT NULL,
    order_index INT NOT NULL,
    config TEXT,
    metadata TEXT,
    is_optional BOOLEAN DEFAULT FALSE,
    timeout_ms BIGINT DEFAULT 30000,
    max_retries INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_workflow_conditions (
    id UUID PRIMARY KEY,
    workflow_id UUID NOT NULL,
    step_id UUID,
    condition_type VARCHAR(50) NOT NULL,
    expression TEXT,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_workflow_actions (
    id UUID PRIMARY KEY,
    workflow_id UUID NOT NULL,
    step_id UUID,
    action_type VARCHAR(50) NOT NULL,
    config TEXT,
    on_success VARCHAR(100),
    on_failure VARCHAR(100),
    created_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_workflow_executions (
    id UUID PRIMARY KEY,
    workflow_id UUID NOT NULL,
    workflow_version VARCHAR(50),
    status VARCHAR(50) NOT NULL,
    trigger_type VARCHAR(50),
    trigger_data TEXT,
    started_by UUID,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    error_message TEXT,
    retry_count INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_workflow_execution_history (
    id UUID PRIMARY KEY,
    execution_id UUID NOT NULL,
    workflow_id UUID,
    step_name VARCHAR(255),
    step_type VARCHAR(50),
    status VARCHAR(50) NOT NULL,
    input_data TEXT,
    output_data TEXT,
    error_message TEXT,
    duration_ms BIGINT,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS ai_workflow_execution_state (
    id UUID PRIMARY KEY,
    execution_id UUID NOT NULL,
    workflow_id UUID,
    current_step VARCHAR(255),
    context_data TEXT,
    variables TEXT,
    status VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS ai_workflow_schedules (
    id UUID PRIMARY KEY,
    workflow_id UUID NOT NULL,
    cron_expression VARCHAR(100) NOT NULL,
    start_at TIMESTAMP,
    end_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    timezone VARCHAR(50) DEFAULT 'UTC',
    last_executed_at TIMESTAMP,
    next_execution_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_ai_workflows_status ON ai_workflows(status);
CREATE INDEX IF NOT EXISTS idx_ai_workflow_steps_wf ON ai_workflow_steps(workflow_id);
CREATE INDEX IF NOT EXISTS idx_ai_workflow_executions_wf ON ai_workflow_executions(workflow_id);
CREATE INDEX IF NOT EXISTS idx_ai_workflow_executions_status ON ai_workflow_executions(status);
CREATE INDEX IF NOT EXISTS idx_ai_workflow_schedules_wf ON ai_workflow_schedules(workflow_id);
CREATE INDEX IF NOT EXISTS idx_ai_workflow_conditions_wf ON ai_workflow_conditions(workflow_id);
CREATE INDEX IF NOT EXISTS idx_ai_workflow_actions_wf ON ai_workflow_actions(workflow_id);
