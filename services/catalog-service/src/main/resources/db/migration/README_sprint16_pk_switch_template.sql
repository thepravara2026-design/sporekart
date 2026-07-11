-- Template: PK switch for `products` (catalog-service)
-- WARNING: Review and test carefully in staging before applying to production.

-- Phase: prepare (ensure backfilled `id_uuid` exists)
-- Step 1: create tmp table using UUID PK
CREATE TABLE tmp_products (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    sku VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Step 2: copy data mapping id_uuid -> new id
INSERT INTO tmp_products (id, id_legacy, sku, name, slug, description, status, created_at, updated_at, deleted)
SELECT id_uuid, id, sku, name, slug, description, status, created_at, updated_at, deleted
FROM products;

-- Step 3: create indexes and constraints needed on tmp table (add as required)
CREATE INDEX idx_tmp_products_sku ON tmp_products(sku);

-- Step 4: once validated, rename tables inside a maintenance window
-- RENAME TABLE products TO old_products; RENAME TABLE tmp_products TO products;

-- Step 5: rebuild foreign keys in referencing tables to point to new UUID PKs (after adding backfilled fk columns)

-- Cleanup: after sufficient verification, archive or drop `old_products`.
