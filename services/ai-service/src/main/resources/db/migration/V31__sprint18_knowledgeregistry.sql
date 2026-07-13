-- Sprint 18: knowledgeregistry
-- Knowledge source main table
CREATE TABLE IF NOT EXISTS kr_knowledge_source (
    source_id     VARCHAR(255) NOT NULL,
    source_name   VARCHAR(255) NOT NULL,
    source_type   VARCHAR(64)  NOT NULL,
    description   TEXT,
    owner         VARCHAR(255),
    version       INT          NOT NULL,
    refresh_policy VARCHAR(32),
    refresh_cron  VARCHAR(128),
    health_status VARCHAR(32),
    sync_status   VARCHAR(32),
    last_sync_at  TIMESTAMP,
    next_sync_at  TIMESTAMP,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    PRIMARY KEY (source_id)
);

-- Element collection: metadata (Map<String,String>)
CREATE TABLE IF NOT EXISTS kr_knowledge_source_metadata (
    source_id      VARCHAR(255) NOT NULL,
    metadata_key   VARCHAR(255),
    metadata_value VARCHAR(2048),
    FOREIGN KEY (source_id) REFERENCES kr_knowledge_source (source_id)
);

-- Knowledge source health records
CREATE TABLE IF NOT EXISTS kr_knowledge_health (
    id           BIGINT       NOT NULL,
    source_id    VARCHAR(255) NOT NULL,
    status       VARCHAR(32)  NOT NULL,
    checked_at   TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
);
