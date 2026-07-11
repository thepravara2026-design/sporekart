-- ===================================================================
-- Sprint 17 Part 3 — Enterprise AI Provider Abstraction Layer
-- Provider registry, models, configuration, capabilities, health
-- ===================================================================

-- AI Provider Registry
CREATE TABLE IF NOT EXISTS ai_provider_registry (
    id              UUID PRIMARY KEY,
    provider_type   VARCHAR(100) NOT NULL UNIQUE,
    display_name    VARCHAR(255) NOT NULL,
    version         VARCHAR(50),
    enabled         BOOLEAN      NOT NULL DEFAULT TRUE,
    priority        INT          DEFAULT 0,
    endpoint_url    VARCHAR(500),
    configuration   JSONB,
    created_by      UUID,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_by      UUID,
    updated_at      TIMESTAMPTZ,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_provider_registry_type
    ON ai_provider_registry (provider_type) WHERE NOT is_deleted;

CREATE INDEX IF NOT EXISTS idx_provider_registry_enabled
    ON ai_provider_registry (enabled) WHERE NOT is_deleted;

-- AI Provider Models
CREATE TABLE IF NOT EXISTS ai_provider_models (
    id                      UUID PRIMARY KEY,
    provider_type           VARCHAR(100) NOT NULL,
    model_id                VARCHAR(255) NOT NULL,
    display_name            VARCHAR(255),
    max_tokens              INT,
    supports_streaming      BOOLEAN      DEFAULT FALSE,
    supports_embedding      BOOLEAN      DEFAULT FALSE,
    supports_vision         BOOLEAN      DEFAULT FALSE,
    supports_function_calling BOOLEAN    DEFAULT FALSE,
    cost_per_input_token    DECIMAL(10,8) DEFAULT 0,
    cost_per_output_token   DECIMAL(10,8) DEFAULT 0,
    created_at              TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ,
    is_deleted              BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at              TIMESTAMPTZ,
    UNIQUE (provider_type, model_id)
);

CREATE INDEX IF NOT EXISTS idx_provider_models_type
    ON ai_provider_models (provider_type) WHERE NOT is_deleted;

-- AI Provider Configuration
CREATE TABLE IF NOT EXISTS ai_provider_configuration (
    id              UUID PRIMARY KEY,
    provider_type   VARCHAR(100) NOT NULL,
    config_key      VARCHAR(255) NOT NULL,
    config_value    TEXT,
    config_type     VARCHAR(50)  DEFAULT 'STRING',
    environment     VARCHAR(50)  DEFAULT 'production',
    is_secret       BOOLEAN      DEFAULT FALSE,
    description     TEXT,
    created_by      UUID,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_by      UUID,
    updated_at      TIMESTAMPTZ,
    is_deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ,
    UNIQUE (provider_type, config_key, environment)
);

CREATE INDEX IF NOT EXISTS idx_provider_config_type
    ON ai_provider_configuration (provider_type) WHERE NOT is_deleted;

-- AI Provider Capabilities
CREATE TABLE IF NOT EXISTS ai_provider_capabilities (
    id              UUID PRIMARY KEY,
    provider_type   VARCHAR(100) NOT NULL,
    capability      VARCHAR(100) NOT NULL,
    supported       BOOLEAN      DEFAULT TRUE,
    description     TEXT,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ,
    UNIQUE (provider_type, capability)
);

CREATE INDEX IF NOT EXISTS idx_provider_caps_type
    ON ai_provider_capabilities (provider_type);

-- AI Provider Health
CREATE TABLE IF NOT EXISTS ai_provider_health (
    id                  UUID PRIMARY KEY,
    provider_type       VARCHAR(100) NOT NULL,
    healthy             BOOLEAN      NOT NULL DEFAULT TRUE,
    degraded            BOOLEAN      DEFAULT FALSE,
    latency_ms          BIGINT       DEFAULT 0,
    last_checked_at     TIMESTAMPTZ,
    last_failure_at     TIMESTAMPTZ,
    consecutive_failures INT         DEFAULT 0,
    details             TEXT,
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ,
    UNIQUE (provider_type)
);

CREATE INDEX IF NOT EXISTS idx_provider_health_type
    ON ai_provider_health (provider_type);
