-- Backfill UUID column for admin-service tables

ALTER TABLE support_tickets
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();

UPDATE support_tickets SET id_uuid = random_uuid() WHERE id_uuid IS NULL;

ALTER TABLE support_tickets
    ADD CONSTRAINT uq_support_tickets_id_uuid UNIQUE (id_uuid);

CREATE INDEX IF NOT EXISTS idx_support_tickets_id_uuid ON support_tickets(id_uuid);

ALTER TABLE approval_requests
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();

UPDATE approval_requests SET id_uuid = random_uuid() WHERE id_uuid IS NULL;

ALTER TABLE approval_requests
    ADD CONSTRAINT uq_approval_requests_id_uuid UNIQUE (id_uuid);

CREATE INDEX IF NOT EXISTS idx_approval_requests_id_uuid ON approval_requests(id_uuid);
