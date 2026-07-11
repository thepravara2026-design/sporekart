-- V4: PK switch for `payments` to UUID primary key and backfill `order_id_uuid`
-- Preconditions: `V3__sprint16_uuid_backfill.sql` must have run for orders in the order-service.

-- 1) create tmp_payments with UUID PK
CREATE TABLE IF NOT EXISTS tmp_payments (
    id UUID PRIMARY KEY,
    id_legacy VARCHAR(36),
    order_id VARCHAR(36) NOT NULL,
    order_id_uuid UUID,
    amount NUMERIC(12,2) NOT NULL,
    status VARCHAR(40) NOT NULL,
    payment_method VARCHAR(40) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tmp_payments (id, id_legacy, order_id, order_id_uuid, amount, status, payment_method, created_at, updated_at)
SELECT id_uuid, id, order_id, NULL, amount, status, payment_method, created_at, updated_at
FROM payments;

-- 2) Add payment_id_uuid to payment_transactions and backfill from payments
ALTER TABLE payment_transactions ADD COLUMN IF NOT EXISTS payment_id_uuid UUID;
UPDATE payment_transactions pt SET payment_id_uuid = p.id_uuid FROM payments p WHERE p.id = pt.payment_id;
CREATE INDEX IF NOT EXISTS idx_payment_transactions_payment_id_uuid ON payment_transactions(payment_id_uuid);

-- 3) Validation queries:
-- SELECT COUNT(*) FROM payments;
-- SELECT COUNT(*) FROM tmp_payments;
-- SELECT COUNT(*) FROM payment_transactions WHERE payment_id_uuid IS NULL;

-- 4) Automated swap (transactional): validate row counts and rename
DO $$
DECLARE
    cnt_orig BIGINT;
    cnt_tmp BIGINT;
BEGIN
    SELECT COUNT(*) INTO cnt_orig FROM payments;
    SELECT COUNT(*) INTO cnt_tmp FROM tmp_payments;
    IF cnt_orig <> cnt_tmp THEN
        RAISE EXCEPTION 'Row count mismatch: payments(%), tmp_payments(%)', cnt_orig, cnt_tmp;
    END IF;

    EXECUTE 'ALTER TABLE payments RENAME TO old_payments';
    EXECUTE 'ALTER TABLE tmp_payments RENAME TO payments';

    RAISE NOTICE 'Payments table swapped successfully.';
END$$;

-- 5) Post-swap: payment_transactions.payment_id_uuid is backfilled.  Application-level code should use order_id_uuid
-- for cross-service order references if available, because this service likely cannot enforce cross-database FKs.
