-- Sprint 18: configregistry
-- Configuration main table
CREATE TABLE IF NOT EXISTS cr_configuration (
    config_id    VARCHAR(64)  NOT NULL,
    config_key   VARCHAR(255) NOT NULL,
    config_value TEXT,
    config_type  VARCHAR(50),
    description  TEXT,
    version      INT          NOT NULL,
    module       VARCHAR(255),
    environment  VARCHAR(128),
    snapshot_id  VARCHAR(64),
    is_valid     BOOLEAN      NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NOT NULL,
    created_by   VARCHAR(255),
    PRIMARY KEY (config_id),
    CONSTRAINT uk_cr_configuration_config_key UNIQUE (config_key)
);

-- Element collection: metadata (Map<String,String>)
CREATE TABLE IF NOT EXISTS cr_configuration_metadata (
    config_id       VARCHAR(64) NOT NULL,
    metadata_key    VARCHAR(255),
    metadata_value  TEXT,
    FOREIGN KEY (config_id) REFERENCES cr_configuration (config_id)
);

-- Config snapshot main table
CREATE TABLE IF NOT EXISTS cr_config_snapshot (
    snapshot_id  VARCHAR(64)  NOT NULL,
    name         VARCHAR(255) NOT NULL,
    description  TEXT,
    version      INT          NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    created_by   VARCHAR(255),
    PRIMARY KEY (snapshot_id)
);

-- Element collection: configurations (Map<String,String>)
CREATE TABLE IF NOT EXISTS cr_snapshot_configurations (
    snapshot_id  VARCHAR(64) NOT NULL,
    config_key   VARCHAR(255),
    config_value TEXT,
    FOREIGN KEY (snapshot_id) REFERENCES cr_config_snapshot (snapshot_id)
);
