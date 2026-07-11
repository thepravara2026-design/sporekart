-- ===================================================================
-- Sprint 17 Part 4 — Enterprise Prompt Management Platform
-- Categories, templates, versions, variables, audit, execution logs
-- ===================================================================

-- AI Prompt Categories
CREATE TABLE IF NOT EXISTS ai_prompt_categories (
    id              UUID PRIMARY KEY,
    name            VARCHAR(100) NOT NULL UNIQUE,
    description     TEXT,
    icon            VARCHAR(50),
    display_order   INT DEFAULT 0,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_by      UUID,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by      UUID,
    updated_at      TIMESTAMPTZ,
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_prompt_categories_active
    ON ai_prompt_categories (is_active) WHERE NOT is_deleted;

-- AI Prompt Templates
CREATE TABLE IF NOT EXISTS ai_prompt_templates (
    id              UUID PRIMARY KEY,
    category_id     UUID NOT NULL REFERENCES ai_prompt_categories(id),
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    template_text   TEXT NOT NULL,
    status          VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    current_version INT NOT NULL DEFAULT 1,
    tags            TEXT[],
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_by      UUID,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by      UUID,
    updated_at      TIMESTAMPTZ,
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (name, category_id)
);

CREATE INDEX IF NOT EXISTS idx_prompt_templates_category
    ON ai_prompt_templates (category_id) WHERE NOT is_deleted;

CREATE INDEX IF NOT EXISTS idx_prompt_templates_status
    ON ai_prompt_templates (status) WHERE NOT is_deleted;

CREATE INDEX IF NOT EXISTS idx_prompt_templates_active
    ON ai_prompt_templates (is_active) WHERE NOT is_deleted;

-- AI Prompt Versions
CREATE TABLE IF NOT EXISTS ai_prompt_versions (
    id              UUID PRIMARY KEY,
    template_id     UUID NOT NULL REFERENCES ai_prompt_templates(id),
    version_number  INT NOT NULL,
    template_text   TEXT NOT NULL,
    status          VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    change_notes    TEXT,
    activation_date TIMESTAMPTZ,
    created_by      UUID,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    approved_by     UUID,
    approved_at     TIMESTAMPTZ,
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (template_id, version_number)
);

CREATE INDEX IF NOT EXISTS idx_prompt_versions_template
    ON ai_prompt_versions (template_id) WHERE NOT is_deleted;

CREATE INDEX IF NOT EXISTS idx_prompt_versions_status
    ON ai_prompt_versions (status) WHERE NOT is_deleted;

-- AI Prompt Variables
CREATE TABLE IF NOT EXISTS ai_prompt_variables (
    id              UUID PRIMARY KEY,
    template_id     UUID NOT NULL REFERENCES ai_prompt_templates(id),
    name            VARCHAR(255) NOT NULL,
    var_type        VARCHAR(50) NOT NULL DEFAULT 'STRING',
    required        BOOLEAN NOT NULL DEFAULT TRUE,
    default_value   TEXT,
    description     TEXT,
    validation_regex VARCHAR(500),
    display_order   INT DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ,
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (template_id, name)
);

CREATE INDEX IF NOT EXISTS idx_prompt_variables_template
    ON ai_prompt_variables (template_id) WHERE NOT is_deleted;

-- AI Prompt Audit
CREATE TABLE IF NOT EXISTS ai_prompt_audit (
    id              UUID PRIMARY KEY,
    template_id     UUID REFERENCES ai_prompt_templates(id),
    version_id      UUID REFERENCES ai_prompt_versions(id),
    action          VARCHAR(100) NOT NULL,
    entity_type     VARCHAR(50) NOT NULL,
    entity_id       UUID,
    previous_value  JSONB,
    new_value       JSONB,
    changed_by      UUID,
    changed_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    details         TEXT
);

CREATE INDEX IF NOT EXISTS idx_prompt_audit_template
    ON ai_prompt_audit (template_id);

CREATE INDEX IF NOT EXISTS idx_prompt_audit_action
    ON ai_prompt_audit (action);

CREATE INDEX IF NOT EXISTS idx_prompt_audit_changed_at
    ON ai_prompt_audit (changed_at DESC);

-- AI Prompt Execution Log
CREATE TABLE IF NOT EXISTS ai_prompt_execution_log (
    id              UUID PRIMARY KEY,
    template_id     UUID REFERENCES ai_prompt_templates(id),
    version_number  INT,
    prompt_text     TEXT NOT NULL,
    rendered_text   TEXT,
    variables       JSONB,
    duration_ms     BIGINT,
    success         BOOLEAN NOT NULL DEFAULT TRUE,
    error_message   TEXT,
    executed_by     UUID,
    source          VARCHAR(100),
    correlation_id  VARCHAR(255),
    executed_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_prompt_exec_template
    ON ai_prompt_execution_log (template_id);

CREATE INDEX IF NOT EXISTS idx_prompt_exec_success
    ON ai_prompt_execution_log (success);

CREATE INDEX IF NOT EXISTS idx_prompt_exec_at
    ON ai_prompt_execution_log (executed_at DESC);

CREATE INDEX IF NOT EXISTS idx_prompt_exec_correlation
    ON ai_prompt_execution_log (correlation_id);
