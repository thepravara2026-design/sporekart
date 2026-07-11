-- Template: PK switch for `orders` table

CREATE TABLE tmp_orders (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    customer_id VARCHAR(36) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    status VARCHAR(40) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO tmp_orders (id, id_legacy, customer_id, amount, status, created_at, updated_at, deleted)
SELECT id_uuid, id, customer_id, amount, status, created_at, updated_at, deleted FROM orders;

-- After validation: rename, rebuild FKs, update application to use UUIDs.
