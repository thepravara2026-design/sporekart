-- V4: PK switch for `inventory` to use UUID `id` as primary key and add product_id_uuid FK
-- Preconditions:
--  - `V2__sprint16_persistence_review.sql` has added `id` UUID to `inventory`.
--  - `catalog-service` has run `V3__sprint16_uuid_backfill.sql` so `products.id_uuid` is available.

-- 1) create tmp_inventory with UUID primary key and product_id_uuid FK
CREATE TABLE IF NOT EXISTS tmp_inventory (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    product_id VARCHAR(36),
    product_id_uuid UUID,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    reserved_quantity INTEGER NOT NULL DEFAULT 0,
    available_quantity INTEGER NOT NULL DEFAULT 0,
    min_stock INTEGER NOT NULL DEFAULT 0,
    max_stock INTEGER NOT NULL DEFAULT 0,
    reorder_level INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 2) copy data mapping existing `id`/`product_id` and backfill `product_id_uuid` by joining products
INSERT INTO tmp_inventory (id, id_legacy, product_id, product_id_uuid, stock_quantity, reserved_quantity, available_quantity, min_stock, max_stock, reorder_level, status, created_at, updated_at, is_deleted)
SELECT i.id, i.product_id, i.product_id, p.id_uuid, i.stock_quantity, i.reserved_quantity, i.available_quantity, i.min_stock, i.max_stock, i.reorder_level, i.status, i.created_at, i.updated_at, i.is_deleted
FROM inventory i
LEFT JOIN products p ON p.id = i.product_id;

CREATE INDEX IF NOT EXISTS idx_tmp_inventory_product_id_uuid ON tmp_inventory(product_id_uuid);

-- 2b) Add product_id_uuid to inventory_movements and backfill
ALTER TABLE inventory_movements ADD COLUMN IF NOT EXISTS product_id_uuid UUID;
UPDATE inventory_movements im SET product_id_uuid = p.id_uuid FROM products p WHERE p.id = im.product_id;
CREATE INDEX IF NOT EXISTS idx_inventory_movements_product_id_uuid ON inventory_movements(product_id_uuid);

-- 3) Validation queries:
-- SELECT COUNT(*) FROM inventory;
-- SELECT COUNT(*) FROM tmp_inventory;
-- SELECT COUNT(*) FROM tmp_inventory WHERE product_id_uuid IS NULL; -- investigate any nulls
-- SELECT COUNT(*) FROM inventory_movements WHERE product_id_uuid IS NULL; -- investigate any nulls

-- 4) Automated swap (transactional): validate row counts then rename and drop FKs referencing inventory/products
DO $$
DECLARE
    cnt_orig BIGINT;
    cnt_tmp BIGINT;
    r RECORD;
BEGIN
    SELECT COUNT(*) INTO cnt_orig FROM inventory;
    SELECT COUNT(*) INTO cnt_tmp FROM tmp_inventory;
    IF cnt_orig <> cnt_tmp THEN
        RAISE EXCEPTION 'Row count mismatch: inventory(%), tmp_inventory(%)', cnt_orig, cnt_tmp;
    END IF;

    -- Drop any foreign key constraints that reference the current inventory table or products table
    FOR r IN SELECT conrelid::regclass::text AS child_table, conname
             FROM pg_constraint
             WHERE (confrelid = 'inventory'::regclass OR confrelid = 'products'::regclass) AND contype = 'f'
    LOOP
        EXECUTE format('ALTER TABLE %s DROP CONSTRAINT IF EXISTS %I', r.child_table, r.conname);
        RAISE NOTICE 'Dropped FK % on %', r.conname, r.child_table;
    END LOOP;

    EXECUTE 'ALTER TABLE inventory RENAME TO old_inventory';
    EXECUTE 'ALTER TABLE tmp_inventory RENAME TO inventory';

    RAISE NOTICE 'Inventory table swapped successfully.';
END$$;

-- 5) Post-swap: recreate FK constraints for inventory_movements to reference products by product_id_uuid
ALTER TABLE IF EXISTS inventory_movements
    ADD CONSTRAINT IF NOT EXISTS fk_inventory_movements_product_uuid FOREIGN KEY (product_id_uuid) REFERENCES products(id);

-- 6) Post-swap: update application layer to use `inventory.id` UUID if required and remove legacy columns after verification.
