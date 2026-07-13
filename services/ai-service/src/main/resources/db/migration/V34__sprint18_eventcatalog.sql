-- Sprint 18: eventcatalog
-- Event catalog main table
CREATE TABLE IF NOT EXISTS ec_event_catalog (
    event_id        VARCHAR(255) NOT NULL,
    event_name      VARCHAR(255) NOT NULL,
    event_version   INT          NOT NULL,
    module          VARCHAR(255) NOT NULL,
    producer        VARCHAR(255) NOT NULL,
    payload_schema  TEXT,
    retention_days  INT          NOT NULL,
    retry_strategy  VARCHAR(50)  NOT NULL,
    max_retries     INT          NOT NULL,
    dlq_enabled     BOOLEAN      NOT NULL,
    dlq_topic       VARCHAR(255),
    description     TEXT,
    documentation   TEXT,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    PRIMARY KEY (event_id)
);

-- Element collection: consumers (List<String>)
CREATE TABLE IF NOT EXISTS ec_event_consumers (
    event_id   VARCHAR(255) NOT NULL,
    consumer   VARCHAR(255),
    FOREIGN KEY (event_id) REFERENCES ec_event_catalog (event_id)
);

-- Event subscription main table
CREATE TABLE IF NOT EXISTS ec_event_subscription (
    subscription_id VARCHAR(255) NOT NULL,
    event_id        VARCHAR(255) NOT NULL,
    consumer_name   VARCHAR(255) NOT NULL,
    consumer_group  VARCHAR(255) NOT NULL,
    enabled         BOOLEAN      NOT NULL,
    PRIMARY KEY (subscription_id)
);
