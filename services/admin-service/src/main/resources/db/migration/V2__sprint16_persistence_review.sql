-- Add audit and soft-delete columns to admin-service tables
ALTER TABLE support_tickets ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE support_tickets ADD COLUMN created_by UUID;
ALTER TABLE support_tickets ADD COLUMN updated_by UUID;
ALTER TABLE support_tickets ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_support_tickets_is_deleted ON support_tickets(is_deleted);

ALTER TABLE approval_requests ADD COLUMN created_by UUID;
ALTER TABLE approval_requests ADD COLUMN updated_by UUID;
ALTER TABLE approval_requests ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_approval_requests_is_deleted ON approval_requests(is_deleted);
