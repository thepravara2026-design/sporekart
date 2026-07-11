-- Add audit columns and soft-delete where missing for order-service
ALTER TABLE orders ADD COLUMN created_by UUID;
ALTER TABLE orders ADD COLUMN updated_by UUID;

CREATE INDEX IF NOT EXISTS idx_orders_deleted ON orders(deleted);

ALTER TABLE order_items ADD COLUMN created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE order_items ADD COLUMN created_by UUID;
ALTER TABLE order_items ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_order_items_is_deleted ON order_items(is_deleted);
