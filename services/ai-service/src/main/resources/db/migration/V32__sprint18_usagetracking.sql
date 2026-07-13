-- Sprint 18: usagetracking
-- Usage record main table
CREATE TABLE IF NOT EXISTS ut_usage_record (
    usage_id         VARCHAR(64)  NOT NULL,
    request_id       VARCHAR(64),
    provider_id      VARCHAR(128),
    model_id         VARCHAR(128),
    prompt_tokens    INT,
    completion_tokens INT,
    total_tokens     INT,
    execution_time_ms BIGINT,
    success          BOOLEAN,
    failure_reason   VARCHAR(1024),
    timestamp        TIMESTAMP,
    user_id          VARCHAR(64),
    session_id       VARCHAR(64),
    module           VARCHAR(128),
    PRIMARY KEY (usage_id)
);

-- Daily usage summary
CREATE TABLE IF NOT EXISTS ut_daily_usage_summary (
    id                   BIGINT    NOT NULL,
    usage_date           VARCHAR(10),
    provider_id          VARCHAR(128),
    model_id             VARCHAR(128),
    total_requests       INT,
    total_success        INT,
    total_failure        INT,
    total_tokens         BIGINT,
    avg_execution_time_ms DOUBLE PRECISION,
    PRIMARY KEY (id)
);
