CREATE TABLE IF NOT EXISTS prompt_templates (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    scope VARCHAR(50) NOT NULL DEFAULT 'GLOBAL',
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    owner UUID NOT NULL,
    created_by UUID NOT NULL,
    updated_by UUID,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS prompt_versions (
    id UUID PRIMARY KEY,
    template_id UUID NOT NULL REFERENCES prompt_templates(id) ON DELETE CASCADE,
    version INTEGER NOT NULL,
    prompt_body TEXT NOT NULL,
    system_prompt TEXT,
    developer_prompt TEXT,
    user_prompt TEXT,
    few_shot_examples TEXT,
    conversation_instructions TEXT,
    safety_constraints TEXT,
    provider_metadata TEXT,
    variables_json TEXT,
    provider_constraints VARCHAR(500),
    temperature DECIMAL(4,2),
    top_p DECIMAL(4,2),
    max_tokens INTEGER,
    is_published BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by UUID NOT NULL,
    change_notes TEXT,
    UNIQUE(template_id, version)
);

CREATE TABLE IF NOT EXISTS prompt_approvals (
    id UUID PRIMARY KEY,
    version_id UUID NOT NULL REFERENCES prompt_versions(id) ON DELETE CASCADE,
    template_id UUID NOT NULL REFERENCES prompt_templates(id) ON DELETE CASCADE,
    approver UUID NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    comments TEXT,
    approved_at TIMESTAMP WITH TIME ZONE,
    requested_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    step VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS prompt_usage (
    id UUID PRIMARY KEY,
    template_id UUID NOT NULL REFERENCES prompt_templates(id) ON DELETE CASCADE,
    version_id UUID REFERENCES prompt_versions(id),
    provider VARCHAR(100),
    model VARCHAR(100),
    execution_time_ms BIGINT,
    prompt_tokens INTEGER,
    completion_tokens INTEGER,
    total_tokens INTEGER,
    cost DECIMAL(12,6),
    user_id UUID,
    workspace VARCHAR(100),
    latency_ms BIGINT,
    is_success BOOLEAN DEFAULT TRUE,
    error_message TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS prompt_tags (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS prompt_template_tags (
    template_id UUID NOT NULL REFERENCES prompt_templates(id) ON DELETE CASCADE,
    tag_id UUID NOT NULL REFERENCES prompt_tags(id) ON DELETE CASCADE,
    PRIMARY KEY (template_id, tag_id)
);

CREATE TABLE IF NOT EXISTS prompt_audit_log (
    id UUID PRIMARY KEY,
    template_id UUID NOT NULL REFERENCES prompt_templates(id) ON DELETE CASCADE,
    version_id UUID REFERENCES prompt_versions(id),
    action VARCHAR(50) NOT NULL,
    performed_by UUID NOT NULL,
    details TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_prompt_versions_template ON prompt_versions(template_id, version DESC);
CREATE INDEX idx_prompt_approvals_version ON prompt_approvals(version_id);
CREATE INDEX idx_prompt_usage_template ON prompt_usage(template_id);
CREATE INDEX idx_prompt_audit_template ON prompt_audit_log(template_id);
CREATE INDEX idx_prompt_templates_status ON prompt_templates(status);
CREATE INDEX idx_prompt_templates_category ON prompt_templates(category);
CREATE INDEX idx_prompt_templates_slug ON prompt_templates(slug);
