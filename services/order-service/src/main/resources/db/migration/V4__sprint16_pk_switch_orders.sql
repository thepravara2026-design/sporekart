-- V4: PK switch for `orders` to UUID primary key and backfill order_items.order_id_uuid
-- Preconditions: `V3__sprint16_uuid_backfill.sql` has run and `orders.id_uuid` populated.

-- 1) create tmp_orders with UUID PK
CREATE TABLE IF NOT EXISTS tmp_orders (
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

CREATE INDEX IF NOT EXISTS idx_tmp_orders_customer ON tmp_orders(customer_id);

-- 2) Add order_id_uuid and product_id_uuid to order_items and backfill
ALTER TABLE order_items ADD COLUMN IF NOT EXISTS order_id_uuid UUID;
ALTER TABLE order_items ADD COLUMN IF NOT EXISTS product_id_uuid UUID;
UPDATE order_items oi SET order_id_uuid = o.id_uuid FROM orders o WHERE o.id = oi.order_id;
UPDATE order_items oi SET product_id_uuid = p.id_uuid FROM products p WHERE p.id = oi.product_id;
ALTER TABLE order_items ADD CONSTRAINT IF NOT EXISTS uq_order_items_order_id_uuid UNIQUE (order_id_uuid);
ALTER TABLE order_items ADD CONSTRAINT IF NOT EXISTS uq_order_items_product_id_uuid UNIQUE (product_id_uuid);
CREATE INDEX IF NOT EXISTS idx_order_items_order_id_uuid ON order_items(order_id_uuid);
CREATE INDEX IF NOT EXISTS idx_order_items_product_id_uuid ON order_items(product_id_uuid);

-- 3) Validation queries:
-- SELECT COUNT(*) FROM orders;
-- SELECT COUNT(*) FROM tmp_orders;
-- SELECT COUNT(*) FROM order_items WHERE order_id_uuid IS NULL; -- should be 0 after backfill
-- SELECT COUNT(*) FROM order_items WHERE product_id_uuid IS NULL; -- should be 0 after backfill

-- 4) Automated swap (transactional): validate counts and drop existing FKs before swap
DO $$
DECLARE
    cnt_orig BIGINT;
    cnt_tmp BIGINT;
    r RECORD;
BEGIN
    SELECT COUNT(*) INTO cnt_orig FROM orders;
    SELECT COUNT(*) INTO cnt_tmp FROM tmp_orders;
    IF cnt_orig <> cnt_tmp THEN
        RAISE EXCEPTION 'Row count mismatch: orders(%), tmp_orders(%)', cnt_orig, cnt_tmp;
    END IF;

    -- Drop any foreign key constraints that reference the current orders table
    FOR r IN SELECT conrelid::regclass::text AS child_table, conname
             FROM pg_constraint
             WHERE confrelid = 'orders'::regclass AND contype = 'f'
    LOOP
        EXECUTE format('ALTER TABLE %s DROP CONSTRAINT IF EXISTS %I', r.child_table, r.conname);
        RAISE NOTICE 'Dropped FK % on %', r.conname, r.child_table;
    END LOOP;

    EXECUTE 'ALTER TABLE orders RENAME TO old_orders';
    EXECUTE 'ALTER TABLE tmp_orders RENAME TO orders';

    RAISE NOTICE 'Orders table swapped successfully.';
END$$;

-- 5) Post-swap: recreate FK constraints for order_items to reference UUIDs
ALTER TABLE IF EXISTS order_items
    ADD CONSTRAINT IF NOT EXISTS fk_order_items_order_uuid FOREIGN KEY (order_id_uuid) REFERENCES orders(id);
ALTER TABLE IF EXISTS order_items
    ADD CONSTRAINT IF NOT EXISTS fk_order_items_product_uuid FOREIGN KEY (product_id_uuid) REFERENCES products(id);

-- 6) Post-swap: update application to return UUIDs and validate consumers.
