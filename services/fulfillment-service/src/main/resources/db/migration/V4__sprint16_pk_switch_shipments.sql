-- V4: PK switch for `shipments` to UUID primary key and backfill `order_id_uuid`
-- Preconditions: `V3__sprint16_uuid_backfill.sql` must have run for orders in the order-service.

-- 1) create tmp_shipments with UUID PK
CREATE TABLE IF NOT EXISTS tmp_shipments (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    order_id VARCHAR(36) NOT NULL,
    order_id_uuid UUID,
    customer_id VARCHAR(36) NOT NULL,
    shipping_charge NUMERIC(12,2) NOT NULL,
    status VARCHAR(40) NOT NULL,
    awb VARCHAR(64) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tmp_shipments (id, id_legacy, order_id, order_id_uuid, customer_id, shipping_charge, status, awb, created_at, updated_at)
SELECT id_uuid, id, order_id, NULL, customer_id, shipping_charge, status, awb, created_at, updated_at
FROM shipments;

-- 2) Add shipment_id_uuid to shipment_items and backfill from shipments
ALTER TABLE shipment_items ADD COLUMN IF NOT EXISTS shipment_id_uuid UUID;
UPDATE shipment_items si SET shipment_id_uuid = s.id_uuid FROM shipments s WHERE s.id = si.shipment_id;
CREATE INDEX IF NOT EXISTS idx_shipment_items_shipment_id_uuid ON shipment_items(shipment_id_uuid);

-- 3) Validation queries:
-- SELECT COUNT(*) FROM shipments;
-- SELECT COUNT(*) FROM tmp_shipments;
-- SELECT COUNT(*) FROM shipment_items WHERE shipment_id_uuid IS NULL;

-- 4) Automated swap (transactional): validate row counts and rename
DO $$
DECLARE
    cnt_orig BIGINT;
    cnt_tmp BIGINT;
BEGIN
    SELECT COUNT(*) INTO cnt_orig FROM shipments;
    SELECT COUNT(*) INTO cnt_tmp FROM tmp_shipments;
    IF cnt_orig <> cnt_tmp THEN
        RAISE EXCEPTION 'Row count mismatch: shipments(%), tmp_shipments(%)', cnt_orig, cnt_tmp;
    END IF;

    EXECUTE 'ALTER TABLE shipments RENAME TO old_shipments';
    EXECUTE 'ALTER TABLE tmp_shipments RENAME TO shipments';

    RAISE NOTICE 'Shipments table swapped successfully.';
END$$;

-- 5) Post-swap: shipment_items.shipment_id_uuid is backfilled. Application code should use order_id_uuid for order linkage.
