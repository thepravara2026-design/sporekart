-- Add audit and soft-delete to notification-service
ALTER TABLE notifications ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE notifications ADD COLUMN created_by UUID;
ALTER TABLE notifications ADD COLUMN updated_by UUID;
ALTER TABLE notifications ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_notifications_is_deleted ON notifications(is_deleted);
