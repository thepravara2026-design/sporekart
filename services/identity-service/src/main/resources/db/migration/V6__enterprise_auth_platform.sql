-- V6: Enterprise Authentication & Authorization Platform
-- Sessions, Devices, Workspaces, enhanced refresh_tokens, OTP enhancements

CREATE TABLE IF NOT EXISTS sessions (
    session_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    device_id VARCHAR(36),
    ip_address VARCHAR(45),
    device_fingerprint VARCHAR(255),
    workspace_context VARCHAR(36),
    issued_at TIMESTAMP WITH TIME ZONE NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_activity_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS devices (
    device_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    name VARCHAR(120),
    type VARCHAR(20),
    os VARCHAR(50),
    browser VARCHAR(50),
    device_identifier VARCHAR(255),
    trusted BOOLEAN NOT NULL DEFAULT FALSE,
    suspicious BOOLEAN NOT NULL DEFAULT FALSE,
    last_used_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS workspaces (
    workspace_id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    owner_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS workspace_members (
    workspace_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'MEMBER',
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (workspace_id, user_id)
);

-- Enhance refresh_tokens with rotation tracking
ALTER TABLE refresh_tokens ADD COLUMN IF NOT EXISTS replaced_by VARCHAR(512);
ALTER TABLE refresh_tokens ADD COLUMN IF NOT EXISTS revoked_at TIMESTAMP WITH TIME ZONE;

-- Enhance otp_requests with verified flag if not exists
ALTER TABLE otp_requests ADD COLUMN IF NOT EXISTS verified BOOLEAN NOT NULL DEFAULT FALSE;

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_sessions_user_id ON sessions (user_id);
CREATE INDEX IF NOT EXISTS idx_sessions_expires_at ON sessions (expires_at);
CREATE INDEX IF NOT EXISTS idx_sessions_revoked ON sessions (revoked);
CREATE INDEX IF NOT EXISTS idx_devices_user_id ON devices (user_id);
CREATE INDEX IF NOT EXISTS idx_devices_identifier ON devices (device_identifier);
CREATE INDEX IF NOT EXISTS idx_workspaces_owner ON workspaces (owner_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_revoked ON refresh_tokens (revoked);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_expires ON refresh_tokens (expires_at);
CREATE INDEX IF NOT EXISTS idx_otp_requests_verified ON otp_requests (verified);
