-- Add audit and soft-delete columns to payment-service tables
ALTER TABLE payments ADD COLUMN created_by UUID;
ALTER TABLE payments ADD COLUMN updated_by UUID;
ALTER TABLE payments ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_payments_is_deleted ON payments(is_deleted);

ALTER TABLE payment_transactions ADD COLUMN created_by UUID;
ALTER TABLE payment_transactions ADD COLUMN is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_payment_transactions_is_deleted ON payment_transactions(is_deleted);
