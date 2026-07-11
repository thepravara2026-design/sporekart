-- Template PK-switch for admin-service
-- Replace `<table_name>` with the actual admin table (e.g., `users`, `roles`).

CREATE TABLE tmp_<table_name> (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(255),
    -- preserve columns and types from original
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

INSERT INTO tmp_<table_name> (id, id_legacy /*, other cols */)
SELECT id_uuid, id /*, other cols */ FROM <table_name>;

-- Ensure identity/roles tables are reviewed for RBAC impacts before swap.
