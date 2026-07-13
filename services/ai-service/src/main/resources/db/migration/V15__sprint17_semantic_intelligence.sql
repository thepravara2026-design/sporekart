CREATE TABLE semantic_embeddings (
    id UUID PRIMARY KEY,
    content TEXT,
    embedding TEXT,
    provider VARCHAR(100),
    model VARCHAR(255),
    dimensions INT,
    metadata TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    version INT NOT NULL DEFAULT 1,
    created_by UUID,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by UUID,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE TABLE semantic_vector_index (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'CREATING',
    vector_count INT DEFAULT 0,
    dimensions INT NOT NULL,
    index_config TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE TABLE semantic_search_history (
    id UUID PRIMARY KEY,
    query TEXT NOT NULL,
    search_type VARCHAR(50) NOT NULL,
    filters TEXT,
    result_count INT DEFAULT 0,
    latency_ms BIGINT DEFAULT 0,
    created_by UUID,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE TABLE semantic_similarity_scores (
    id UUID PRIMARY KEY,
    source_embedding_id UUID NOT NULL REFERENCES semantic_embeddings(id),
    target_embedding_id UUID NOT NULL REFERENCES semantic_embeddings(id),
    similarity DOUBLE PRECISION NOT NULL,
    algorithm VARCHAR(50) NOT NULL DEFAULT 'COSINE',
    metadata TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE TABLE semantic_embedding_jobs (
    id UUID PRIMARY KEY,
    type VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    total_items INT DEFAULT 0,
    processed_items INT DEFAULT 0,
    failed_items INT DEFAULT 0,
    config TEXT,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    error_message TEXT,
    created_by UUID,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE TABLE semantic_index_statistics (
    id UUID PRIMARY KEY,
    index_name VARCHAR(255) NOT NULL,
    stat_key VARCHAR(255) NOT NULL,
    stat_value DOUBLE PRECISION NOT NULL,
    recorded_at TIMESTAMP NOT NULL DEFAULT NOW(),
    metadata TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_semantic_embeddings_status ON semantic_embeddings(status, is_deleted);
CREATE INDEX idx_semantic_embeddings_provider ON semantic_embeddings(provider, is_deleted);
CREATE INDEX idx_semantic_embeddings_created_at ON semantic_embeddings(created_at DESC);
CREATE INDEX idx_semantic_embeddings_model ON semantic_embeddings(model, is_deleted);
CREATE INDEX idx_semantic_embeddings_created_by ON semantic_embeddings(created_by);

CREATE INDEX idx_semantic_vector_index_status ON semantic_vector_index(status, is_deleted);
CREATE INDEX idx_semantic_vector_index_name ON semantic_vector_index(name);

CREATE INDEX idx_semantic_search_history_type ON semantic_search_history(search_type, created_at DESC);
CREATE INDEX idx_semantic_search_history_created_by ON semantic_search_history(created_by);
CREATE INDEX idx_semantic_search_history_created_at ON semantic_search_history(created_at DESC);

CREATE INDEX idx_semantic_similarity_source ON semantic_similarity_scores(source_embedding_id, is_deleted);
CREATE INDEX idx_semantic_similarity_target ON semantic_similarity_scores(target_embedding_id, is_deleted);
CREATE INDEX idx_semantic_similarity_algorithm ON semantic_similarity_scores(algorithm, is_deleted);
CREATE INDEX idx_semantic_similarity_score ON semantic_similarity_scores(similarity DESC);

CREATE INDEX idx_semantic_embedding_jobs_status ON semantic_embedding_jobs(status, is_deleted);
CREATE INDEX idx_semantic_embedding_jobs_type ON semantic_embedding_jobs(type, is_deleted);
CREATE INDEX idx_semantic_embedding_jobs_created_by ON semantic_embedding_jobs(created_by);
CREATE INDEX idx_semantic_embedding_jobs_created_at ON semantic_embedding_jobs(created_at DESC);

CREATE INDEX idx_semantic_index_statistics_name ON semantic_index_statistics(index_name, stat_key);
CREATE INDEX idx_semantic_index_statistics_recorded ON semantic_index_statistics(recorded_at DESC);
