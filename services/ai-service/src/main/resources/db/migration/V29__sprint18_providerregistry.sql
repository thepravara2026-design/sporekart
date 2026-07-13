-- Sprint 18: providerregistry
-- Provider registry main table
CREATE TABLE IF NOT EXISTS pr_provider_registry (
    provider_id     VARCHAR(64)  NOT NULL,
    provider_name   VARCHAR(255) NOT NULL,
    provider_type   VARCHAR(50)  NOT NULL,
    version         VARCHAR(50),
    status          VARCHAR(50)  NOT NULL,
    priority        INT,
    health_status   VARCHAR(50),
    metadata        TEXT,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    deprecated_at   TIMESTAMP,
    PRIMARY KEY (provider_id)
);

-- Element collection: supported models (embeddable with order column)
CREATE TABLE IF NOT EXISTS pr_provider_models (
    provider_id           VARCHAR(64)  NOT NULL,
    model_order           INT          NOT NULL,
    model_id              VARCHAR(255),
    model_name            VARCHAR(255),
    context_window        INT,
    max_tokens            INT,
    streaming_supported   BOOLEAN,
    tool_calling_supported BOOLEAN,
    embeddings_supported  BOOLEAN,
    image_supported       BOOLEAN,
    audio_supported       BOOLEAN,
    FOREIGN KEY (provider_id) REFERENCES pr_provider_registry (provider_id)
);

-- Element collection: capabilities (List<enum>)
CREATE TABLE IF NOT EXISTS pr_provider_capabilities (
    provider_id  VARCHAR(64) NOT NULL,
    capability   VARCHAR(50),
    FOREIGN KEY (provider_id) REFERENCES pr_provider_registry (provider_id)
);

-- Provider health records
CREATE TABLE IF NOT EXISTS pr_provider_health (
    id           VARCHAR(36)  NOT NULL,
    provider_id  VARCHAR(64)  NOT NULL,
    status       VARCHAR(50)  NOT NULL,
    checked_at   TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_pr_health_provider ON pr_provider_health (provider_id);
CREATE INDEX IF NOT EXISTS idx_pr_health_checked ON pr_provider_health (checked_at);
