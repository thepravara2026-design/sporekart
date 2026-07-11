-- V4: PK switch for notifications table to UUID
-- Preconditions: V3__sprint16_uuid_backfill.sql has run and id_uuid populated.

-- 1) Create tmp_notifications with UUID PK
CREATE TABLE IF NOT EXISTS tmp_notifications (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    recipient VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    channel VARCHAR(40) NOT NULL,
    status VARCHAR(40) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tmp_notifications (id, id_legacy, recipient, subject, body, channel, status, created_at)
SELECT id_uuid, id, recipient, subject, body, channel, status, created_at FROM notifications;

-- 2) Swap
ALTER TABLE notifications RENAME TO old_notifications;
ALTER TABLE tmp_notifications RENAME TO notifications;
