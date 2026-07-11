-- Backfill UUID for shipments and shipment_items

ALTER TABLE shipments
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();
UPDATE shipments SET id_uuid = random_uuid() WHERE id_uuid IS NULL;
ALTER TABLE shipments ADD CONSTRAINT uq_shipments_id_uuid UNIQUE (id_uuid);
CREATE INDEX IF NOT EXISTS idx_shipments_id_uuid ON shipments(id_uuid);

ALTER TABLE shipment_items
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();
UPDATE shipment_items SET id_uuid = random_uuid() WHERE id_uuid IS NULL;
ALTER TABLE shipment_items ADD CONSTRAINT uq_shipment_items_id_uuid UNIQUE (id_uuid);
CREATE INDEX IF NOT EXISTS idx_shipment_items_id_uuid ON shipment_items(id_uuid);
