CREATE TABLE IF NOT EXISTS operations_configuration (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    configuration_key VARCHAR(100) NOT NULL UNIQUE,
    configuration_value TEXT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by UUID,
    updated_by UUID,
    is_deleted BOOLEAN DEFAULT false
);

CREATE INDEX idx_operations_configuration_key ON operations_configuration(configuration_key);

ALTER TABLE purchase_orders
    ALTER COLUMN status SET DEFAULT 'DRAFT';

ALTER TABLE suppliers
    ALTER COLUMN status SET DEFAULT 'PENDING';

ALTER TABLE warehouses
    ALTER COLUMN status SET DEFAULT 'ACTIVE';
