-- Sprint 18: promptregistry
-- Prompt registry main table
CREATE TABLE IF NOT EXISTS pr_prompt_registry (
    id           VARCHAR(36)  NOT NULL,
    prompt_id    VARCHAR(255) NOT NULL,
    prompt_name  VARCHAR(255) NOT NULL,
    description  TEXT,
    prompt_text  TEXT         NOT NULL,
    version      INT          NOT NULL,
    owner        VARCHAR(255),
    status       VARCHAR(50)  NOT NULL,
    previous_version_id VARCHAR(255),
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_pr_prompt_registry_prompt_id UNIQUE (prompt_id)
);

-- Element collection: tags (List<String>)
CREATE TABLE IF NOT EXISTS pr_prompt_registry_tags (
    prompt_registry_id VARCHAR(36) NOT NULL,
    tag                VARCHAR(255),
    FOREIGN KEY (prompt_registry_id) REFERENCES pr_prompt_registry (id)
);

-- Element collection: metadata (Map<String,String>)
CREATE TABLE IF NOT EXISTS pr_prompt_registry_metadata (
    prompt_registry_id VARCHAR(36) NOT NULL,
    meta_key           VARCHAR(255),
    meta_value         TEXT,
    FOREIGN KEY (prompt_registry_id) REFERENCES pr_prompt_registry (id)
);

-- Prompt version history
CREATE TABLE IF NOT EXISTS pr_prompt_version_history (
    id            VARCHAR(36)  NOT NULL,
    prompt_id     VARCHAR(255) NOT NULL,
    version       INT          NOT NULL,
    status        VARCHAR(50)  NOT NULL,
    change_summary TEXT,
    prompt_text   TEXT,
    created_at    TIMESTAMP    NOT NULL,
    PRIMARY KEY (id)
);
