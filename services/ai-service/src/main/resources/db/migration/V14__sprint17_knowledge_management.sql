CREATE TABLE knowledge_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_by UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by UUID,
    updated_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMPTZ
);

CREATE TABLE knowledge_documents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category_id UUID REFERENCES knowledge_categories(id),
    title VARCHAR(500) NOT NULL,
    description TEXT,
    content TEXT,
    language VARCHAR(10) DEFAULT 'en',
    author VARCHAR(255),
    source VARCHAR(255),
    source_type VARCHAR(100),
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    visibility VARCHAR(50) NOT NULL DEFAULT 'INTERNAL',
    business_module VARCHAR(100),
    region VARCHAR(100),
    current_version INT NOT NULL DEFAULT 1,
    file_path VARCHAR(1000),
    file_size BIGINT,
    mime_type VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_by UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by UUID,
    updated_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMPTZ
);

CREATE TABLE knowledge_document_versions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_id UUID NOT NULL REFERENCES knowledge_documents(id),
    version_number INT NOT NULL,
    content TEXT,
    change_notes TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    created_by UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    approved_by UUID,
    approved_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    UNIQUE(document_id, version_number)
);

CREATE TABLE knowledge_chunks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_id UUID NOT NULL REFERENCES knowledge_documents(id),
    version_id UUID REFERENCES knowledge_document_versions(id),
    chunk_index INT NOT NULL,
    content TEXT NOT NULL,
    token_count INT DEFAULT 0,
    char_count INT DEFAULT 0,
    chunk_size_strategy VARCHAR(50) DEFAULT 'FIXED',
    parent_chunk_id UUID REFERENCES knowledge_chunks(id),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    UNIQUE(document_id, chunk_index)
);

CREATE TABLE knowledge_metadata (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_id UUID NOT NULL REFERENCES knowledge_documents(id),
    meta_key VARCHAR(255) NOT NULL,
    meta_value TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    UNIQUE(document_id, meta_key)
);

CREATE TABLE knowledge_tags (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_id UUID NOT NULL REFERENCES knowledge_documents(id),
    tag VARCHAR(100) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(document_id, tag)
);

CREATE TABLE knowledge_sources (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    source_type VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMPTZ
);

CREATE TABLE knowledge_access_log (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_id UUID REFERENCES knowledge_documents(id),
    action VARCHAR(100) NOT NULL,
    user_id UUID,
    user_role VARCHAR(50),
    ip_address VARCHAR(45),
    timestamp TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    duration_ms BIGINT,
    details TEXT
);

CREATE TABLE knowledge_citations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_id UUID NOT NULL REFERENCES knowledge_documents(id),
    retrieval_request_id UUID,
    chunk_ids TEXT[],
    excerpts TEXT[],
    relevance_score DOUBLE PRECISION,
    retrieval_context TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by UUID
);

CREATE INDEX idx_knowledge_documents_status ON knowledge_documents(status, is_deleted);
CREATE INDEX idx_knowledge_documents_category ON knowledge_documents(category_id, is_deleted);
CREATE INDEX idx_knowledge_documents_visibility ON knowledge_documents(visibility, is_active);
CREATE INDEX idx_knowledge_documents_business_module ON knowledge_documents(business_module, is_deleted);
CREATE INDEX idx_knowledge_documents_title_search ON knowledge_documents USING gin(to_tsvector('english', title));
CREATE INDEX idx_knowledge_documents_created_at ON knowledge_documents(created_at DESC);
CREATE INDEX idx_knowledge_versions_document ON knowledge_document_versions(document_id, version_number DESC);
CREATE INDEX idx_knowledge_chunks_document ON knowledge_chunks(document_id, chunk_index);
CREATE INDEX idx_knowledge_metadata_document ON knowledge_metadata(document_id);
CREATE INDEX idx_knowledge_tags_document ON knowledge_tags(document_id);
CREATE INDEX idx_knowledge_access_log_timestamp ON knowledge_access_log(timestamp DESC);
CREATE INDEX idx_knowledge_access_log_action ON knowledge_access_log(action, timestamp DESC);
CREATE INDEX idx_knowledge_citations_document ON knowledge_citations(document_id);
CREATE INDEX idx_knowledge_citations_request ON knowledge_citations(retrieval_request_id);
