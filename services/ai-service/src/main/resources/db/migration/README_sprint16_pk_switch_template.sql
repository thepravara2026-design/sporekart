-- Template PK-switch for ai-service
-- Replace `<table_name>` with the actual table to convert (e.g., `ai_jobs`, `models`).
-- Steps:
-- 1) Ensure `V3__sprint16_uuid_backfill.sql` ran and `id_uuid` populated
-- 2) Create tmp table with UUID PK, copy rows, validate, then swap

-- Example template (replace <table_name> everywhere):
CREATE TABLE tmp_<table_name> (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(255),
    -- add other columns from <table_name> here (copy definition)
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

INSERT INTO tmp_<table_name> (id, id_legacy /*, other cols */)
SELECT id_uuid, id /*, other cols */ FROM <table_name>;

-- Create indexes and constraints on tmp_<table_name> as needed.
-- After validation: perform atomic rename/swap in maintenance window.

-- Note: Update child tables to have backfilled `*_id_uuid` FK columns before swap.
