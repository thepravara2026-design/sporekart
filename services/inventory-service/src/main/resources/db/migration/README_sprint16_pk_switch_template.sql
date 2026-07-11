-- Template: PK switch for `inventory` table

CREATE TABLE tmp_inventory (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    product_id VARCHAR(36) PRIMARY KEY,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    reserved_quantity INTEGER NOT NULL DEFAULT 0,
    available_quantity INTEGER NOT NULL DEFAULT 0,
    min_stock INTEGER NOT NULL DEFAULT 0,
    max_stock INTEGER NOT NULL DEFAULT 0,
    reorder_level INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tmp_inventory (id, id_legacy, product_id, stock_quantity, reserved_quantity, available_quantity, min_stock, max_stock, reorder_level, status, updated_at)
SELECT id_uuid, product_id, product_id, stock_quantity, reserved_quantity, available_quantity, min_stock, max_stock, reorder_level, status, updated_at FROM inventory;

-- Validate and swap tables in maintenance window.
