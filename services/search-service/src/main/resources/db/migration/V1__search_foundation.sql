CREATE TABLE IF NOT EXISTS search_documents (
    id VARCHAR(36) PRIMARY KEY,
    entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100) NOT NULL,
    title VARCHAR(500),
    description TEXT,
    content TEXT,
    tags TEXT,
    metadata JSONB,
    score DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    indexed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_search_entity ON search_documents(entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_search_entity_type ON search_documents(entity_type);
