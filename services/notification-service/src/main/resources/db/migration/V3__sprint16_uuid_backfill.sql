-- Backfill UUID for notifications

ALTER TABLE notifications
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();
UPDATE notifications SET id_uuid = random_uuid() WHERE id_uuid IS NULL;
ALTER TABLE notifications ADD CONSTRAINT uq_notifications_id_uuid UNIQUE (id_uuid);
CREATE INDEX IF NOT EXISTS idx_notifications_id_uuid ON notifications(id_uuid);
