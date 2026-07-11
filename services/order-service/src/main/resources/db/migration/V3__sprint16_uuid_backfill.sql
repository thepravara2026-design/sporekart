-- Backfill UUIDs for orders and order_items

ALTER TABLE orders
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();
UPDATE orders SET id_uuid = random_uuid() WHERE id_uuid IS NULL;
ALTER TABLE orders ADD CONSTRAINT uq_orders_id_uuid UNIQUE (id_uuid);
CREATE INDEX IF NOT EXISTS idx_orders_id_uuid ON orders(id_uuid);

ALTER TABLE order_items
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();
UPDATE order_items SET id_uuid = random_uuid() WHERE id_uuid IS NULL;
ALTER TABLE order_items ADD CONSTRAINT uq_order_items_id_uuid UNIQUE (id_uuid);
CREATE INDEX IF NOT EXISTS idx_order_items_id_uuid ON order_items(id_uuid);
