-- Add audit columns, UUID id, and soft-delete flag to align with enterprise persistence design
ALTER TABLE inventory
ADD COLUMN id UUID DEFAULT random_uuid();

ALTER TABLE inventory ADD COLUMN created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE inventory ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE inventory ADD COLUMN created_by UUID;
ALTER TABLE inventory ADD COLUMN updated_by UUID;
ALTER TABLE inventory ADD COLUMN is_deleted BOOLEAN DEFAULT false;

-- Ensure new id column is unique (do not change existing primary key to avoid migration risk)
ALTER TABLE inventory ADD CONSTRAINT uq_inventory_id UNIQUE (id);

CREATE INDEX IF NOT EXISTS idx_inventory_is_deleted ON inventory(is_deleted);
CREATE INDEX IF NOT EXISTS idx_inventory_updated_at ON inventory(updated_at);

-- Inventory movements: add audit and soft-delete columns
ALTER TABLE inventory_movements ADD COLUMN created_by UUID;
ALTER TABLE inventory_movements ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_inventory_movements_product ON inventory_movements(product_id);
CREATE INDEX IF NOT EXISTS idx_inventory_movements_is_deleted ON inventory_movements(is_deleted);

-- Backfill `id` for existing rows where supported (best-effort - some DBs ignore if column already populated)
UPDATE inventory SET id = random_uuid() WHERE id IS NULL;
