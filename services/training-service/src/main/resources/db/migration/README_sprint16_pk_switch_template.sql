-- Template PK-switch for training-service
-- Replace `<table_name>` with the actual table (e.g., `training_sessions`, `courses`).

CREATE TABLE tmp_<table_name> (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(255),
    -- copy other columns here
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

INSERT INTO tmp_<table_name> (id, id_legacy /*, other cols */)
SELECT id_uuid, id /*, other cols */ FROM <table_name>;

-- Validate, rebuild indexes, then swap tables.
