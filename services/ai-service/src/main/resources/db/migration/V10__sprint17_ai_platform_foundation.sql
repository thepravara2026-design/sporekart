-- ===================================================================
-- Sprint 17 — AI Platform Foundation
-- Placeholder migration for AI Platform infrastructure tables
-- ===================================================================

-- AI Provider Registry
CREATE TABLE IF NOT EXISTS ai_provider_registry (
    id              UUID PRIMARY KEY,
    provider_type   VARCHAR(50)  NOT NULL,
    name            VARCHAR(255) NOT NULL,
    vendor          VARCHAR(100),
    enabled         BOOLEAN      NOT NULL DEFAULT TRUE,
    configuration   TEXT,
    endpoint_url    VARCHAR(500),
    api_key_ref     VARCHAR(255),
    model           VARCHAR(100),
    max_tokens      INT          DEFAULT 2048,
    temperature     NUMERIC(3,2) DEFAULT 0.7,
    created_by      UUID,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_by      UUID,
    updated_at      TIMESTAMPTZ,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_provider_registry_type
    ON ai_provider_registry (provider_type) WHERE NOT is_deleted;

-- AI Feature Flags
CREATE TABLE IF NOT EXISTS ai_feature_flags (
    id              UUID PRIMARY KEY,
    flag_key        VARCHAR(100) NOT NULL,
    flag_name       VARCHAR(255) NOT NULL,
    enabled         BOOLEAN      NOT NULL DEFAULT FALSE,
    description     TEXT,
    rollout_pct     INT          DEFAULT 100,
    created_by      UUID,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_by      UUID,
    updated_at      TIMESTAMPTZ,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_feature_flags_key
    ON ai_feature_flags (flag_key) WHERE NOT is_deleted;

-- AI Module Configuration
CREATE TABLE IF NOT EXISTS ai_module_configuration (
    id              UUID PRIMARY KEY,
    module_name     VARCHAR(100) NOT NULL,
    enabled         BOOLEAN      NOT NULL DEFAULT TRUE,
    rate_limit      INT          DEFAULT 100,
    rate_limit_unit VARCHAR(10)  DEFAULT 'MINUTE',
    configuration   JSONB,
    created_by      UUID,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_by      UUID,
    updated_at      TIMESTAMPTZ,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_module_config_name
    ON ai_module_configuration (module_name) WHERE NOT is_deleted;
