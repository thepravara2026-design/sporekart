-- V5: Cleanup after PK switch monitoring period
-- Requires RBAC validation and coordination with auth flows before executing.

DROP TABLE IF EXISTS old_users;

ALTER TABLE users DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE users DROP COLUMN IF EXISTS id_legacy;

-- Child table cleanup: drop VARCHAR legacy columns once the UUID columns are proven stable
-- ALTER TABLE user_roles DROP COLUMN IF EXISTS user_id;
-- ALTER TABLE refresh_tokens DROP COLUMN IF EXISTS user_id;
-- ALTER TABLE otp_requests DROP COLUMN IF EXISTS user_id;
-- ALTER TABLE audit_events DROP COLUMN IF EXISTS actor_id;
