-- Add audit columns to catalog tables (they already use `deleted` boolean)
ALTER TABLE products ADD COLUMN created_by UUID;
ALTER TABLE products ADD COLUMN updated_by UUID;

CREATE INDEX IF NOT EXISTS idx_products_deleted ON products(deleted);

ALTER TABLE categories ADD COLUMN created_by UUID;
ALTER TABLE categories ADD COLUMN updated_by UUID;

CREATE INDEX IF NOT EXISTS idx_categories_deleted ON categories(deleted);

ALTER TABLE brands ADD COLUMN created_by UUID;
ALTER TABLE brands ADD COLUMN updated_by UUID;

CREATE INDEX IF NOT EXISTS idx_brands_deleted ON brands(deleted);
