-- Sprint 18: apiregistry
-- API registry main table
CREATE TABLE IF NOT EXISTS ar_api_registry (
    id                 VARCHAR(36)  NOT NULL,
    api_id             VARCHAR(255) NOT NULL,
    api_name           VARCHAR(255) NOT NULL,
    api_path           VARCHAR(512) NOT NULL,
    http_method        VARCHAR(20)  NOT NULL,
    module             VARCHAR(255),
    owner              VARCHAR(255),
    description        TEXT,
    version            VARCHAR(100),
    deprecated         BOOLEAN      NOT NULL,
    deprecation_notice TEXT,
    auth_required      BOOLEAN      NOT NULL,
    health_status      VARCHAR(20),
    open_api_spec      TEXT,
    created_at         TIMESTAMP,
    updated_at         TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_ar_api_registry_api_id UNIQUE (api_id)
);

-- Element collection: rolesAllowed (List<String>)
CREATE TABLE IF NOT EXISTS ar_api_registry_roles (
    api_registry_id VARCHAR(36) NOT NULL,
    role            VARCHAR(255),
    FOREIGN KEY (api_registry_id) REFERENCES ar_api_registry (id)
);

-- Element collection: consumers (List<String>)
CREATE TABLE IF NOT EXISTS ar_api_registry_consumers (
    api_registry_id VARCHAR(36) NOT NULL,
    consumer        VARCHAR(255),
    FOREIGN KEY (api_registry_id) REFERENCES ar_api_registry (id)
);

-- Element collection: dependencies (List<String>)
CREATE TABLE IF NOT EXISTS ar_api_registry_dependencies (
    api_registry_id VARCHAR(36) NOT NULL,
    dependency      VARCHAR(255),
    FOREIGN KEY (api_registry_id) REFERENCES ar_api_registry (id)
);

-- API health records
CREATE TABLE IF NOT EXISTS ar_api_health (
    id          VARCHAR(36)  NOT NULL,
    api_id      VARCHAR(255) NOT NULL,
    status      VARCHAR(20)  NOT NULL,
    checked_at  TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_ar_health_api_id ON ar_api_health (api_id);
