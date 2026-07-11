-- Sprint 15: Mobile Platform Tables

CREATE TABLE IF NOT EXISTS mobile_devices (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    device_name VARCHAR(255) NOT NULL,
    device_type VARCHAR(32) NOT NULL,
    os_version VARCHAR(64) NOT NULL,
    app_version VARCHAR(64) NOT NULL,
    fcm_token VARCHAR(1024) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    is_trusted BOOLEAN DEFAULT false,
    last_login TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS device_sessions (
    id UUID PRIMARY KEY,
    device_id UUID NOT NULL,
    user_id UUID NOT NULL,
    token VARCHAR(2048) NOT NULL,
    refresh_token VARCHAR(2048) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (device_id) REFERENCES mobile_devices(id)
);

CREATE TABLE IF NOT EXISTS offline_sync_logs (
    id UUID PRIMARY KEY,
    device_id UUID NOT NULL,
    status VARCHAR(32) NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE NOT NULL,
    completed_at TIMESTAMP WITH TIME ZONE,
    error_message TEXT,
    records_sync INTEGER DEFAULT 0,
    bytes_sync INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (device_id) REFERENCES mobile_devices(id)
);

CREATE TABLE IF NOT EXISTS push_notification_tokens (
    id UUID PRIMARY KEY,
    device_id UUID NOT NULL,
    fcm_token VARCHAR(1024) NOT NULL,
    topics TEXT DEFAULT '[]',
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (device_id) REFERENCES mobile_devices(id)
);

CREATE TABLE IF NOT EXISTS mobile_preferences (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    allow_order_notifications BOOLEAN DEFAULT true,
    allow_training_notifications BOOLEAN DEFAULT true,
    allow_system_notifications BOOLEAN DEFAULT true,
    allow_marketing_notifications BOOLEAN DEFAULT false,
    quiet_hours_start VARCHAR(5),
    quiet_hours_end VARCHAR(5),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id)
);

CREATE TABLE IF NOT EXISTS push_notifications (
    id UUID PRIMARY KEY,
    device_id UUID NOT NULL,
    user_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    data TEXT,
    notification_type VARCHAR(32) NOT NULL,
    action_url VARCHAR(1024),
    is_read BOOLEAN DEFAULT false,
    is_archived BOOLEAN DEFAULT false,
    sent_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (device_id) REFERENCES mobile_devices(id)
);

-- Indexes for performance

CREATE INDEX IF NOT EXISTS idx_mobile_devices_user_id ON mobile_devices(user_id);
CREATE INDEX IF NOT EXISTS idx_mobile_devices_fcm_token ON mobile_devices(fcm_token);
CREATE INDEX IF NOT EXISTS idx_device_sessions_device_id ON device_sessions(device_id);
CREATE INDEX IF NOT EXISTS idx_device_sessions_user_id ON device_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_device_sessions_expires_at ON device_sessions(expires_at);
CREATE INDEX IF NOT EXISTS idx_offline_sync_logs_device_id ON offline_sync_logs(device_id);
CREATE INDEX IF NOT EXISTS idx_offline_sync_logs_status ON offline_sync_logs(status);
CREATE INDEX IF NOT EXISTS idx_push_notification_tokens_device_id ON push_notification_tokens(device_id);
CREATE INDEX IF NOT EXISTS idx_push_notification_tokens_fcm_token ON push_notification_tokens(fcm_token);
CREATE INDEX IF NOT EXISTS idx_push_notifications_device_id ON push_notifications(device_id);
CREATE INDEX IF NOT EXISTS idx_push_notifications_user_id ON push_notifications(user_id);
CREATE INDEX IF NOT EXISTS idx_push_notifications_is_read ON push_notifications(is_read);
CREATE INDEX IF NOT EXISTS idx_mobile_preferences_user_id ON mobile_preferences(user_id);
