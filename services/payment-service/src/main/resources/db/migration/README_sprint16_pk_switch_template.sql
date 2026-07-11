-- Template PK-switch for payment-service
-- Replace `<table_name>` with the actual payment table (e.g., `payments`, `transactions`).

CREATE TABLE tmp_<table_name> (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(255),
    -- copy other columns
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

INSERT INTO tmp_<table_name> (id, id_legacy /*, other cols */)
SELECT id_uuid, id /*, other cols */ FROM <table_name>;

-- Rebuild constraints and ensure settlement integrations are validated.
