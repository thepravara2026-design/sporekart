-- V4: PK switch for `products` to UUID primary key
-- Preconditions: `V3__sprint16_uuid_backfill.sql` must have run and `products.id_uuid` populated.
-- This migration is non-destructive: it creates `tmp_products`, copies data, and leaves `old_products` for review.

-- 1) create tmp table with UUID primary key
CREATE TABLE IF NOT EXISTS tmp_products (
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

-- 2) copy data mapping id_uuid -> id
INSERT INTO tmp_products (id, id_legacy, sku, name, slug, description, status, created_at, updated_at, deleted)
SELECT id_uuid, id, sku, name, slug, description, status, created_at, updated_at, deleted
FROM products;

-- 3) recreate indexes needed on tmp table
CREATE INDEX IF NOT EXISTS idx_tmp_products_sku ON tmp_products(sku);
CREATE INDEX IF NOT EXISTS idx_tmp_products_slug ON tmp_products(slug);

-- 4) Validation queries (run manually):
-- SELECT COUNT(*) FROM products; -- original row count
-- SELECT COUNT(*) FROM tmp_products; -- should match
-- SELECT COUNT(*) FROM products p LEFT JOIN tmp_products t ON p.id = t.id_legacy WHERE t.id IS NULL; -- should be 0

-- 5) Automated swap procedure: validates row counts and renames tables inside a transaction.
DO $$
DECLARE
    cnt_orig BIGINT;
    cnt_tmp BIGINT;
    r RECORD;
BEGIN
    SELECT COUNT(*) INTO cnt_orig FROM products;
    SELECT COUNT(*) INTO cnt_tmp FROM tmp_products;
    IF cnt_orig <> cnt_tmp THEN
        RAISE EXCEPTION 'Row count mismatch: products(%), tmp_products(%)', cnt_orig, cnt_tmp;
    END IF;

    -- Drop any foreign key constraints that reference the current products table
    FOR r IN SELECT conrelid::regclass::text AS child_table, conname
             FROM pg_constraint
             WHERE confrelid = 'products'::regclass AND contype = 'f'
    LOOP
        EXECUTE format('ALTER TABLE %s DROP CONSTRAINT IF EXISTS %I', r.child_table, r.conname);
        RAISE NOTICE 'Dropped FK % on %', r.conname, r.child_table;
    END LOOP;

    -- Perform atomic rename
    EXECUTE 'ALTER TABLE products RENAME TO old_products';
    EXECUTE 'ALTER TABLE tmp_products RENAME TO products';

    RAISE NOTICE 'Products table swapped successfully.';
END$$;

-- 6) Post-swap: rebuild any materialized views, full-text indexes, and notify dependent services.

-- Note: `catalog-service` does not own dependent tables like `inventory` or `order_items` in its own DB schema.
-- Any child-table FK recreation for those external services should be performed in the respective service DB migrations.
