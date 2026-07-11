-- Add missing audit columns and soft-delete flags to Sprint 16 tables

ALTER TABLE warehouse_stock ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE warehouse_stock ADD COLUMN IF NOT EXISTS created_by UUID;
ALTER TABLE warehouse_stock ADD COLUMN IF NOT EXISTS updated_by UUID;
ALTER TABLE warehouse_stock ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_warehouse_stock_deleted ON warehouse_stock(is_deleted);

ALTER TABLE warehouse_transfer_items ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE warehouse_transfer_items ADD COLUMN IF NOT EXISTS created_by UUID;
ALTER TABLE warehouse_transfer_items ADD COLUMN IF NOT EXISTS updated_by UUID;
ALTER TABLE warehouse_transfer_items ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_warehouse_transfer_items_deleted ON warehouse_transfer_items(is_deleted);

ALTER TABLE purchase_order_items ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE purchase_order_items ADD COLUMN IF NOT EXISTS created_by UUID;
ALTER TABLE purchase_order_items ADD COLUMN IF NOT EXISTS updated_by UUID;
ALTER TABLE purchase_order_items ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_purchase_order_items_deleted ON purchase_order_items(is_deleted);

ALTER TABLE gst_line_items ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE gst_line_items ADD COLUMN IF NOT EXISTS created_by UUID;
ALTER TABLE gst_line_items ADD COLUMN IF NOT EXISTS updated_by UUID;
ALTER TABLE gst_line_items ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_gst_line_items_deleted ON gst_line_items(is_deleted);

ALTER TABLE supplier_performance_metrics ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE supplier_performance_metrics ADD COLUMN IF NOT EXISTS created_by UUID;
ALTER TABLE supplier_performance_metrics ADD COLUMN IF NOT EXISTS updated_by UUID;
ALTER TABLE supplier_performance_metrics ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_supplier_performance_deleted ON supplier_performance_metrics(is_deleted);

ALTER TABLE erp_provider_config ADD COLUMN IF NOT EXISTS created_by UUID;
ALTER TABLE erp_provider_config ADD COLUMN IF NOT EXISTS updated_by UUID;
ALTER TABLE erp_provider_config ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_erp_provider_config_deleted ON erp_provider_config(is_deleted);

ALTER TABLE feature_flags ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN DEFAULT false;

CREATE INDEX IF NOT EXISTS idx_feature_flags_deleted ON feature_flags(is_deleted);

ALTER TABLE ai_audit_logs ADD COLUMN IF NOT EXISTS performed_by UUID;

CREATE INDEX IF NOT EXISTS idx_ai_audit_logs_performed_by ON ai_audit_logs(performed_by);
CREATE INDEX IF NOT EXISTS idx_ai_audit_logs_entity ON ai_audit_logs(entity_type, entity_id);
