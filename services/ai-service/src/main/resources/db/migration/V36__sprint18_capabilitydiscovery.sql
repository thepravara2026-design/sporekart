-- Sprint 18: capabilitydiscovery
-- Capability main table
CREATE TABLE IF NOT EXISTS cd_capability (
    capability_id    VARCHAR(255) NOT NULL,
    capability_name  VARCHAR(255) NOT NULL,
    description      TEXT,
    capability_type  VARCHAR(64)  NOT NULL,
    module           VARCHAR(255),
    availability     VARCHAR(32),
    version          VARCHAR(64),
    feature_flag     VARCHAR(255),
    enabled          BOOLEAN      NOT NULL,
    created_at       TIMESTAMP,
    updated_at       TIMESTAMP,
    PRIMARY KEY (capability_id)
);

-- Element collection: supportedFeatures (List<String>)
CREATE TABLE IF NOT EXISTS cd_capability_features (
    capability_id VARCHAR(255) NOT NULL,
    feature       VARCHAR(255),
    FOREIGN KEY (capability_id) REFERENCES cd_capability (capability_id)
);

-- Element collection: dependencies (List<String>)
CREATE TABLE IF NOT EXISTS cd_capability_dependencies (
    capability_id VARCHAR(255) NOT NULL,
    dependency    VARCHAR(255),
    FOREIGN KEY (capability_id) REFERENCES cd_capability (capability_id)
);

-- Element collection: providerCompatibility (List<String>)
CREATE TABLE IF NOT EXISTS cd_capability_providers (
    capability_id VARCHAR(255) NOT NULL,
    provider      VARCHAR(255),
    FOREIGN KEY (capability_id) REFERENCES cd_capability (capability_id)
);

-- Element collection: metadata (Map<String,String>)
CREATE TABLE IF NOT EXISTS cd_capability_metadata (
    capability_id   VARCHAR(255) NOT NULL,
    metadata_key    VARCHAR(255),
    metadata_value  VARCHAR(2048),
    FOREIGN KEY (capability_id) REFERENCES cd_capability (capability_id)
);

-- Capability health records
CREATE TABLE IF NOT EXISTS cd_capability_health (
    id            BIGINT       NOT NULL,
    capability_id VARCHAR(255) NOT NULL,
    availability  VARCHAR(32)  NOT NULL,
    checked_at    TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
);
