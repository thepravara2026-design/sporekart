CREATE TABLE ai_admin_configuration (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    key VARCHAR(255) NOT NULL,
    value TEXT,
    module VARCHAR(100),
    environment VARCHAR(50),
    description TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    version INT DEFAULT 1,
    updated_by UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE ai_feature_flags (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    key VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    description TEXT,
    enabled BOOLEAN DEFAULT FALSE,
    environment VARCHAR(50),
    module VARCHAR(100),
    metadata TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE ai_environment_profiles (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    description TEXT,
    active BOOLEAN DEFAULT FALSE,
    config_source VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE ai_configuration_versions (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    config_id UUID REFERENCES ai_admin_configuration(id),
    version INT NOT NULL,
    value TEXT,
    change_reason TEXT,
    changed_by UUID,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ai_configuration_snapshots (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    configuration TEXT,
    environment VARCHAR(50),
    description TEXT,
    captured_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    captured_by UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE ai_admin_audit (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id UUID,
    performed_by UUID,
    details TEXT,
    ip_address VARCHAR(45),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ai_admin_operations (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    description TEXT,
    details TEXT,
    performed_by UUID,
    ip_address VARCHAR(45),
    successful BOOLEAN DEFAULT TRUE,
    performed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_admin_config_key ON ai_admin_configuration(key, module);
CREATE INDEX idx_admin_config_module ON ai_admin_configuration(module);
CREATE INDEX idx_admin_config_environment ON ai_admin_configuration(environment);
CREATE INDEX idx_feature_flags_key ON ai_feature_flags(key);
CREATE INDEX idx_feature_flags_module ON ai_feature_flags(module);
CREATE INDEX idx_environment_profiles_type ON ai_environment_profiles(type);
CREATE INDEX idx_config_versions_config ON ai_configuration_versions(config_id);
CREATE INDEX idx_snapshots_environment ON ai_configuration_snapshots(environment);
CREATE INDEX idx_admin_audit_entity ON ai_admin_audit(entity_id);
CREATE INDEX idx_admin_operations_type ON ai_admin_operations(type);
CREATE INDEX idx_admin_operations_performed ON ai_admin_operations(performed_at);
