CREATE TABLE IF NOT EXISTS ai_assistant_sessions (
    id UUID PRIMARY KEY,
    assistant_type VARCHAR(32) NOT NULL,
    owner VARCHAR(255) NOT NULL,
    context TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_recommendations (
    id UUID PRIMARY KEY,
    category VARCHAR(64) NOT NULL,
    recommendation_type VARCHAR(64) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_search_history (
    id UUID PRIMARY KEY,
    query TEXT NOT NULL,
    category VARCHAR(64),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS knowledge_feedback (
    id UUID PRIMARY KEY,
    document_id UUID,
    feedback_type VARCHAR(32) NOT NULL,
    message TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS conversation_feedback (
    id UUID PRIMARY KEY,
    conversation_id UUID,
    feedback_type VARCHAR(32) NOT NULL,
    message TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_ai_assistant_sessions_owner ON ai_assistant_sessions(owner);
CREATE INDEX IF NOT EXISTS idx_ai_recommendations_category ON ai_recommendations(category);
CREATE INDEX IF NOT EXISTS idx_ai_search_history_query ON ai_search_history(query);
CREATE INDEX IF NOT EXISTS idx_knowledge_feedback_document_id ON knowledge_feedback(document_id);
CREATE INDEX IF NOT EXISTS idx_conversation_feedback_conversation_id ON conversation_feedback(conversation_id);
