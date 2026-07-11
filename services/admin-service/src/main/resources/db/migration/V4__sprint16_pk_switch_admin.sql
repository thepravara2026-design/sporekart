-- V4: PK switch for support_tickets and approval_requests to UUID
-- Preconditions: V3__sprint16_uuid_backfill.sql has run and id_uuid populated.

-- 1) Create tmp_support_tickets with UUID PK
CREATE TABLE IF NOT EXISTS tmp_support_tickets (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    subject VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    requester VARCHAR(255) NOT NULL,
    status VARCHAR(40) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tmp_support_tickets (id, id_legacy, subject, description, requester, status, created_at)
SELECT id_uuid, id, subject, description, requester, status, created_at FROM support_tickets;

-- 2) Create tmp_approval_requests with UUID PK
CREATE TABLE IF NOT EXISTS tmp_approval_requests (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    target_type VARCHAR(100) NOT NULL,
    target_id VARCHAR(100) NOT NULL,
    status VARCHAR(40) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tmp_approval_requests (id, id_legacy, target_type, target_id, status, created_at)
SELECT id_uuid, id, target_type, target_id, status, created_at FROM approval_requests;

-- 3) Automated swap
ALTER TABLE support_tickets RENAME TO old_support_tickets;
ALTER TABLE tmp_support_tickets RENAME TO support_tickets;

ALTER TABLE approval_requests RENAME TO old_approval_requests;
ALTER TABLE tmp_approval_requests RENAME TO approval_requests;
