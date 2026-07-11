-- Backfill UUIDs for payment-service where `id` is VARCHAR

ALTER TABLE payments
    ADD COLUMN id_uuid UUID DEFAULT random_uuid();
UPDATE payments SET id_uuid = random_uuid() WHERE id_uuid IS NULL;
ALTER TABLE payments ADD CONSTRAINT uq_payments_id_uuid UNIQUE (id_uuid);
CREATE INDEX IF NOT EXISTS idx_payments_id_uuid ON payments(id_uuid);

-- payment_transactions uses BIGSERIAL, skip
