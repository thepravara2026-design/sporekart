-- V5: Cleanup after PK switch monitoring period
-- Drops old_ tables and legacy columns superseded by UUID primary key.

DROP TABLE IF EXISTS old_support_tickets;
DROP TABLE IF EXISTS old_approval_requests;

ALTER TABLE support_tickets DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE support_tickets DROP COLUMN IF EXISTS id_legacy;

ALTER TABLE approval_requests DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE approval_requests DROP COLUMN IF EXISTS id_legacy;
