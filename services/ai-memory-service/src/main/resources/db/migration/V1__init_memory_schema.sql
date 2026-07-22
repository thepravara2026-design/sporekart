-- V1__init_memory_schema.sql
-- Initial schema for the AI Memory Service

CREATE TABLE IF NOT EXISTS ai_memory_tags_ref (
    id          UUID PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS ai_memories (
    id              UUID PRIMARY KEY,
    title           VARCHAR(500),
    content         TEXT NOT NULL,
    summary         TEXT,
    memory_type     VARCHAR(30) NOT NULL DEFAULT 'NOTE',
    source          VARCHAR(50) NOT NULL DEFAULT 'MANUAL',
    priority        VARCHAR(20) DEFAULT 'MEDIUM',
    importance      INTEGER NOT NULL DEFAULT 5,
    confidence      DOUBLE PRECISION DEFAULT 0.0,
    owner_id        UUID NOT NULL,
    owner_type      VARCHAR(30) NOT NULL DEFAULT 'USER',
    workspace       VARCHAR(30) NOT NULL DEFAULT 'default',
    department      VARCHAR(100),
    visibility      VARCHAR(20) NOT NULL DEFAULT 'PRIVATE',
    entity_id       UUID,
    entity_type     VARCHAR(100),
    conversation_id UUID,
    is_ephemeral    BOOLEAN NOT NULL DEFAULT FALSE,
    is_encrypted    BOOLEAN NOT NULL DEFAULT FALSE,
    encryption_key_ref VARCHAR(255),
    retention_policy VARCHAR(20) NOT NULL DEFAULT 'STANDARD',
    expires_at      TIMESTAMP WITH TIME ZONE,
    embedding_id    UUID,
    created_by      UUID NOT NULL,
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,
    access_count    INTEGER NOT NULL DEFAULT 0,
    metadata_json   TEXT,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    last_accessed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS ai_memory_tags (
    memory_id UUID NOT NULL REFERENCES ai_memories(id) ON DELETE CASCADE,
    tag_id    UUID NOT NULL REFERENCES ai_memory_tags_ref(id) ON DELETE CASCADE,
    PRIMARY KEY (memory_id, tag_id)
);

CREATE TABLE IF NOT EXISTS ai_memory_chunks (
    id          UUID PRIMARY KEY,
    memory_id   UUID NOT NULL REFERENCES ai_memories(id) ON DELETE CASCADE,
    content     TEXT NOT NULL,
    chunk_index INTEGER NOT NULL,
    token_count INTEGER DEFAULT 0,
    embedding_id UUID,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS ai_memory_summaries (
    id           UUID PRIMARY KEY,
    memory_id    UUID NOT NULL REFERENCES ai_memories(id) ON DELETE CASCADE,
    summary_text TEXT NOT NULL,
    summary_type VARCHAR(30) NOT NULL DEFAULT 'AUTO_GENERATED',
    source_count INTEGER DEFAULT 0,
    version      INTEGER DEFAULT 1,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS ai_memory_relationships (
    id                UUID PRIMARY KEY,
    source_memory_id  UUID NOT NULL REFERENCES ai_memories(id) ON DELETE CASCADE,
    target_memory_id  UUID NOT NULL REFERENCES ai_memories(id) ON DELETE CASCADE,
    relationship_type VARCHAR(100) NOT NULL,
    strength          DOUBLE PRECISION DEFAULT 0.0,
    metadata_json     TEXT,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_memory_relationship UNIQUE (source_memory_id, target_memory_id, relationship_type)
);

CREATE TABLE IF NOT EXISTS ai_memory_audit_log (
    id          UUID PRIMARY KEY,
    memory_id   UUID NOT NULL REFERENCES ai_memories(id) ON DELETE CASCADE,
    action      VARCHAR(50) NOT NULL,
    actor_id    UUID,
    details_json TEXT,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_ai_memories_owner ON ai_memories(owner_id, is_deleted);
CREATE INDEX IF NOT EXISTS idx_ai_memories_workspace ON ai_memories(workspace, is_deleted);
CREATE INDEX IF NOT EXISTS idx_ai_memories_type ON ai_memories(memory_type, is_deleted);
CREATE INDEX IF NOT EXISTS idx_ai_memories_conversation ON ai_memories(conversation_id, is_deleted);
CREATE INDEX IF NOT EXISTS idx_ai_memories_entity ON ai_memories(entity_type, entity_id, is_deleted);
CREATE INDEX IF NOT EXISTS idx_ai_memories_source ON ai_memories(source, is_deleted);
CREATE INDEX IF NOT EXISTS idx_ai_memories_expires ON ai_memories(expires_at) WHERE is_deleted = FALSE;
CREATE INDEX IF NOT EXISTS idx_ai_memories_importance ON ai_memories(importance DESC) WHERE is_deleted = FALSE;
CREATE INDEX IF NOT EXISTS idx_ai_chunks_memory ON ai_memory_chunks(memory_id);
CREATE INDEX IF NOT EXISTS idx_ai_summaries_memory ON ai_memory_summaries(memory_id);
CREATE INDEX IF NOT EXISTS idx_ai_relationships_source ON ai_memory_relationships(source_memory_id);
CREATE INDEX IF NOT EXISTS idx_ai_relationships_target ON ai_memory_relationships(target_memory_id);
CREATE INDEX IF NOT EXISTS idx_ai_audit_memory ON ai_memory_audit_log(memory_id);
CREATE INDEX IF NOT EXISTS idx_ai_tags_name ON ai_memory_tags_ref(name);
