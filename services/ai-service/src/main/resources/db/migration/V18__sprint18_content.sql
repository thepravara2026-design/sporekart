CREATE TABLE IF NOT EXISTS ai_content_requests (
    id UUID PRIMARY KEY,
    user_id UUID,
    content_type VARCHAR(50),
    category VARCHAR(50),
    prompt TEXT,
    tone VARCHAR(50),
    target_language VARCHAR(10),
    max_length INT,
    parameters TEXT,
    template_id UUID,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_content_responses (
    id UUID PRIMARY KEY,
    request_id UUID NOT NULL,
    content TEXT,
    content_type VARCHAR(50),
    category VARCHAR(50),
    token_count INT,
    confidence_score DOUBLE,
    human_review_required BOOLEAN DEFAULT FALSE,
    moderation_status VARCHAR(50),
    generated_at TIMESTAMP,
    latency_ms BIGINT,
    success BOOLEAN NOT NULL,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS ai_content_templates (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    category VARCHAR(50),
    content_type VARCHAR(50),
    template_content TEXT NOT NULL,
    variables TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    version INT DEFAULT 1,
    created_by UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_content_history (
    id UUID PRIMARY KEY,
    request_id UUID,
    user_id UUID,
    action VARCHAR(100) NOT NULL,
    content_type VARCHAR(50),
    prompt TEXT,
    result_summary VARCHAR(500),
    token_count INT,
    latency_ms BIGINT,
    success BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS ai_content_feedback (
    id UUID PRIMARY KEY,
    request_id UUID NOT NULL,
    user_id UUID,
    rating INT NOT NULL,
    comment TEXT,
    is_helpful BOOLEAN,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS ai_content_classification (
    id UUID PRIMARY KEY,
    request_id UUID NOT NULL,
    text TEXT,
    classifications TEXT NOT NULL,
    primary_category VARCHAR(100),
    confidence_score DOUBLE,
    keywords TEXT,
    created_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_translation_history (
    id UUID PRIMARY KEY,
    request_id UUID NOT NULL,
    source_text TEXT NOT NULL,
    translated_text TEXT NOT NULL,
    source_language VARCHAR(10),
    target_language VARCHAR(10) NOT NULL,
    detected_language VARCHAR(10),
    confidence_score DOUBLE,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS ai_generation_metrics (
    id UUID PRIMARY KEY,
    request_id UUID,
    content_type VARCHAR(50),
    category VARCHAR(50),
    pipeline_stage VARCHAR(100),
    stage_latency_ms BIGINT,
    total_latency_ms BIGINT,
    token_count INT,
    success BOOLEAN,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_content_requests_user ON ai_content_requests(user_id);
CREATE INDEX IF NOT EXISTS idx_content_requests_status ON ai_content_requests(status);
CREATE INDEX IF NOT EXISTS idx_content_responses_request ON ai_content_responses(request_id);
CREATE INDEX IF NOT EXISTS idx_content_history_user ON ai_content_history(user_id);
CREATE INDEX IF NOT EXISTS idx_content_feedback_request ON ai_content_feedback(request_id);
CREATE INDEX IF NOT EXISTS idx_content_classification_request ON ai_content_classification(request_id);
CREATE INDEX IF NOT EXISTS idx_translation_request ON ai_translation_history(request_id);
CREATE INDEX IF NOT EXISTS idx_generation_metrics_type ON ai_generation_metrics(content_type);
