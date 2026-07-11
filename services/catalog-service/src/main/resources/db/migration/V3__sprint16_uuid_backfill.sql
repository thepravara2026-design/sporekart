-- Backfill UUIDs for catalog-service primary entities

ALTER TABLE products
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();
UPDATE products SET id_uuid = random_uuid() WHERE id_uuid IS NULL;
ALTER TABLE products ADD CONSTRAINT uq_products_id_uuid UNIQUE (id_uuid);
CREATE INDEX IF NOT EXISTS idx_products_id_uuid ON products(id_uuid);

ALTER TABLE categories
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();
UPDATE categories SET id_uuid = random_uuid() WHERE id_uuid IS NULL;
ALTER TABLE categories ADD CONSTRAINT uq_categories_id_uuid UNIQUE (id_uuid);
CREATE INDEX IF NOT EXISTS idx_categories_id_uuid ON categories(id_uuid);

ALTER TABLE brands
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();
UPDATE brands SET id_uuid = random_uuid() WHERE id_uuid IS NULL;
ALTER TABLE brands ADD CONSTRAINT uq_brands_id_uuid UNIQUE (id_uuid);
CREATE INDEX IF NOT EXISTS idx_brands_id_uuid ON brands(id_uuid);
