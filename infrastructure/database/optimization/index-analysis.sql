-- SporeKart Database Index Optimization
-- Recommended indexes based on query patterns

-- 1. Missing index detection (based on seq scans on large tables)
SELECT
    schemaname,
    relname,
    seq_scan,
    seq_tup_read,
    idx_scan,
    n_live_tup
FROM pg_stat_user_tables
WHERE seq_scan > 100
  AND n_live_tup > 10000
  AND COALESCE(idx_scan, 0) < seq_scan * 0.1
ORDER BY seq_scan DESC;

-- 2. Composite index recommendations for common query patterns
-- Orders by customer + date pattern
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_orders_customer_created
ON orders (customer_id, created_at DESC)
WHERE deleted_at IS NULL;

-- Orders by status + date (for status queries)
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_orders_status_created
ON orders (status, created_at DESC)
WHERE deleted_at IS NULL;

-- Order items by order (for order detail joins)
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_order_items_order_product
ON order_items (order_id, product_id);

-- Products by category + active filter
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_products_category_active
ON products (category_id, is_active)
WHERE deleted_at IS NULL;

-- Products search optimization
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_products_name_search
ON products USING gin (to_tsvector('english', name));

-- Inventory by SKU + warehouse
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_inventory_sku_warehouse
ON inventory (sku, warehouse_id)
WHERE deleted_at IS NULL;

-- Cart by user + status
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_cart_user_status
ON cart (user_id, status);

-- Cart items by cart (for cart detail joins)
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_cart_items_cart_product
ON cart_items (cart_id, product_id);

-- Payments by order
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_payments_order
ON payments (order_id, status);

-- Notifications by user + created (for notification listing)
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_notifications_user_created
ON notifications (user_id, created_at DESC);

-- Events by type + created (for event querying)
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_events_type_created
ON events (type, created_at DESC);

-- AI queries by user + created
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_ai_queries_user_created
ON ai_queries (user_id, created_at DESC);

-- Knowledge embeddings by content type
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_knowledge_embeddings_type
ON knowledge_embeddings (content_type, embedding_id);

-- Session by user (for session management)
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_sessions_user
ON sessions (user_id, expires_at)
WHERE expires_at > NOW();

-- 3. Partial indexes for filtered queries
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_orders_pending
ON orders (created_at)
WHERE status = 'PENDING';

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_orders_processing
ON orders (created_at)
WHERE status = 'PROCESSING';

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_inventory_low_stock
ON inventory (quantity, sku)
WHERE quantity < reorder_point;

-- 4. Covering indexes for high-frequency queries (include columns to avoid table lookups)
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_products_listing
ON products (category_id, is_active, price)
INCLUDE (name, image_url, rating)
WHERE deleted_at IS NULL;

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_orders_summary
ON orders (customer_id, created_at DESC)
INCLUDE (status, total_amount, currency)
WHERE deleted_at IS NULL;

-- 5. Concurrent index creation (zero-downtime):
-- CREATE INDEX CONCURRENTLY idx_example ON table_name (column1, column2);
-- DROP INDEX CONCURRENTLY IF EXISTS idx_old_index;

-- 6. Index maintenance
-- Rebuild bloated indexes:
-- REINDEX INDEX CONCURRENTLY idx_name;
-- REINDEX TABLE CONCURRENTLY table_name;

-- 7. Foreign key indexes (prevent FK scan deadlocks)
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_fk_order_customer ON orders (customer_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_fk_order_item_order ON order_items (order_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_fk_cart_item_cart ON cart_items (cart_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_fk_payment_order ON payments (order_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_fk_notification_user ON notifications (user_id);

-- 8. Duplicate index detection
SELECT
    pg_size_pretty(SUM(pg_relation_size(idx))::bigint) AS total_index_size,
    COUNT(*) AS duplicate_index_count
FROM pg_index i
JOIN pg_class c ON i.indexrelid = c.oid
WHERE EXISTS (
    SELECT 1
    FROM pg_index i2
    JOIN pg_class c2 ON i2.indexrelid = c2.oid
    WHERE i.indrelid = i2.indrelid
      AND i.indexrelid != i2.indexrelid
      AND i.indkey = i2.indkey
      AND i.indclass = i2.indclass
      AND i.indoption = i2.indoption
);
