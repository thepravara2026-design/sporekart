-- Materialized view for quick ERP sync history read model

-- Create a read-model view for ERP sync history. Declare columns explicitly for H2 compatibility.
CREATE OR REPLACE VIEW erp_sync_history_mv (
    sync_id,
    sync_type,
    erp_provider,
    sync_status,
    records_synced,
    records_failed,
    sync_start_time,
    sync_end_time,
    error_message,
    retry_count,
    max_retries,
    created_at
) AS
SELECT
    id,
    sync_type,
    erp_provider,
    sync_status,
    records_synced,
    records_failed,
    sync_start_time,
    sync_end_time,
    error_message,
    retry_count,
    max_retries,
    created_at
FROM erp_sync_logs
WHERE is_deleted = false;

-- Index the underlying table to support efficient queries used by the view.
CREATE INDEX IF NOT EXISTS idx_erp_sync_logs_provider_status ON erp_sync_logs(erp_provider, sync_status);

-- Note: H2 does not allow adding indexes on views; index is created on base table instead.
