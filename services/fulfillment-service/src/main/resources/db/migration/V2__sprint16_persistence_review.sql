-- Add audit and soft-delete columns to fulfillment-service
ALTER TABLE shipments ADD COLUMN created_by UUID;
ALTER TABLE shipments ADD COLUMN updated_by UUID;
ALTER TABLE shipments ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_shipments_is_deleted ON shipments(is_deleted);

ALTER TABLE shipment_items ADD COLUMN created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE shipment_items ADD COLUMN created_by UUID;
ALTER TABLE shipment_items ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_shipment_items_is_deleted ON shipment_items(is_deleted);
