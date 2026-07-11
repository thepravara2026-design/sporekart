-- ===================================================================
-- Sprint 17 Part 2 — AI Gateway Foundation
-- Gateway configuration, request audit, and execution history tables
-- ===================================================================

-- AI Gateway Configuration
CREATE TABLE IF NOT EXISTS ai_gateway_configuration (
    id              UUID PRIMARY KEY,
    config_key      VARCHAR(255) NOT NULL,
    config_value    TEXT,
    config_type     VARCHAR(50)  DEFAULT 'STRING',
    module          VARCHAR(100),
    description     TEXT,
    created_by      UUID,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_by      UUID,
    updated_at      TIMESTAMPTZ,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_gateway_config_key
    ON ai_gateway_configuration (config_key) WHERE NOT is_deleted;

CREATE INDEX IF NOT EXISTS idx_gateway_config_module
    ON ai_gateway_configuration (module) WHERE NOT is_deleted;

-- AI Request Audit Log
CREATE TABLE IF NOT EXISTS ai_request_audit (
    id              UUID PRIMARY KEY,
    request_id      VARCHAR(100) NOT NULL,
    correlation_id  VARCHAR(100),
    execution_id    VARCHAR(100),
    module          VARCHAR(100) NOT NULL,
    provider        VARCHAR(100),
    user_id         VARCHAR(255),
    action          VARCHAR(100) NOT NULL,
    status          VARCHAR(50)  NOT NULL,
    prompt_length   INT,
    duration_ms     BIGINT,
    error_code      VARCHAR(50),
    error_message   TEXT,
    request_payload TEXT,
    response_payload TEXT,
    source_ip       VARCHAR(50),
    user_agent      TEXT,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_audit_correlation
    ON ai_request_audit (correlation_id);

CREATE INDEX IF NOT EXISTS idx_audit_module_status
    ON ai_request_audit (module, status);

CREATE INDEX IF NOT EXISTS idx_audit_created
    ON ai_request_audit (created_at DESC);

-- AI Execution History
CREATE TABLE IF NOT EXISTS ai_execution_history (
    id              UUID PRIMARY KEY,
    request_id      VARCHAR(100) NOT NULL,
    correlation_id  VARCHAR(100),
    execution_id    VARCHAR(100) UNIQUE,
    module          VARCHAR(100) NOT NULL,
    provider        VARCHAR(100),
    model           VARCHAR(100),
    status          VARCHAR(50)  NOT NULL,
    prompt_hash     VARCHAR(64),
    prompt_length   INT,
    response_length INT,
    input_tokens    INT,
    output_tokens   INT,
    duration_ms     BIGINT,
    success         BOOLEAN,
    error_code      VARCHAR(50),
    error_message   TEXT,
    retry_count     INT          DEFAULT 0,
    pipeline_stage  VARCHAR(100),
    created_by      UUID,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    completed_at    TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_exec_history_correlation
    ON ai_execution_history (correlation_id);

CREATE INDEX IF NOT EXISTS idx_exec_history_module
    ON ai_execution_history (module, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_exec_history_provider
    ON ai_execution_history (provider, created_at DESC);
