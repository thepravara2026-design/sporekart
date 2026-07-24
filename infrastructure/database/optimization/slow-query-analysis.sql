-- SporeKart Database Performance Analysis
-- Slow Query Identification & Analysis

-- 1. Identify slow queries from pg_stat_statements
SELECT
    queryid,
    calls,
    ROUND(total_exec_time::numeric, 2) AS total_exec_time_ms,
    ROUND(mean_exec_time::numeric, 2) AS mean_exec_time_ms,
    ROUND(min_exec_time::numeric, 2) AS min_exec_time_ms,
    ROUND(max_exec_time::numeric, 2) AS max_exec_time_ms,
    ROUND(stddev_exec_time::numeric, 2) AS stddev_exec_time_ms,
    ROUND(rows::numeric / GREATEST(calls, 1), 2) AS avg_rows,
    ROUND(shared_blks_hit::numeric / GREATEST(shared_blks_hit + shared_blks_read, 1) * 100, 2) AS cache_hit_ratio,
    LEFT(query, 200) AS query_preview
FROM pg_stat_statements
WHERE mean_exec_time > 50
ORDER BY mean_exec_time DESC
LIMIT 50;

-- 2. Queries with most total execution time
SELECT
    queryid,
    calls,
    ROUND(total_exec_time::numeric, 2) AS total_exec_time_ms,
    ROUND(mean_exec_time::numeric, 2) AS mean_exec_time_ms,
    ROUND(total_exec_time::numeric / 60000, 2) AS total_exec_time_min,
    LEFT(query, 200) AS query_preview
FROM pg_stat_statements
ORDER BY total_exec_time DESC
LIMIT 20;

-- 3. Queries with high variance (potential performance instability)
SELECT
    queryid,
    calls,
    ROUND(mean_exec_time::numeric, 2) AS mean_ms,
    ROUND(stddev_exec_time::numeric, 2) AS stddev_ms,
    ROUND((stddev_exec_time::numeric / GREATEST(mean_exec_time, 1)) * 100, 2) AS cv_pct,
    LEFT(query, 200) AS query_preview
FROM pg_stat_statements
WHERE calls > 100 AND stddev_exec_time > mean_exec_time * 0.5
ORDER BY cv_pct DESC
LIMIT 20;

-- 4. Sequential scans (missing index indicator)
SELECT
    schemaname,
    relname,
    seq_scan,
    seq_tup_read,
    idx_scan,
    n_tup_ins,
    n_tup_upd,
    n_tup_del,
    ROUND(seq_tup_read::numeric / GREATEST(seq_scan, 1), 2) AS avg_tuples_per_seq_scan
FROM pg_stat_user_tables
WHERE seq_scan > 1000 AND idx_scan = 0
ORDER BY seq_scan DESC
LIMIT 20;

-- 5. Index usage statistics
SELECT
    schemaname,
    relname,
    indexrelname,
    idx_scan,
    idx_tup_read,
    idx_tup_fetch,
    ROUND(100.0 * idx_scan / GREATEST(seq_scan + idx_scan, 1), 2) AS index_usage_pct
FROM pg_stat_user_indexes
JOIN pg_stat_user_tables USING (schemaname, relname)
ORDER BY idx_scan DESC
LIMIT 30;

-- 6. Unused indexes (write overhead, no read benefit)
SELECT
    schemaname,
    relname,
    indexrelname,
    idx_scan,
    idx_tup_read,
    idx_tup_fetch
FROM pg_stat_user_indexes
WHERE idx_scan = 0
ORDER BY relname;

-- 7. Table bloat estimation
SELECT
    schemaname,
    tablename,
    ROUND(100 * (CASE WHEN otta > 0 THEN (relpages - otta)::numeric / relpages ELSE 0 END), 2) AS bloat_pct,
    ROUND((relpages - COALESCE(otta, 0)) * 8 / 1024, 2) AS bloat_mb
FROM (
    SELECT
        schemaname,
        tablename,
        cc.relpages,
        COALESCE(ceil(cc.reltuples / ((bs - page_hdr - tpl_hdr) / (4 + tpl_hdr::real))), 0) AS otta
    FROM pg_class cc
    JOIN pg_namespace nn ON cc.relnamespace = nn.oid
    CROSS JOIN (
        SELECT
            current_setting('block_size')::numeric AS bs,
            CASE WHEN current_setting('server_version') ~ '^1[0-8]' THEN 28 ELSE 24 END AS page_hdr,
            CASE WHEN current_setting('server_version') ~ '^1[0-8]' THEN 24 ELSE 23 END AS tpl_hdr
    ) AS constants
    WHERE cc.relkind = 'r' AND nn.nspname NOT IN ('pg_catalog', 'information_schema')
) AS bloat_data
WHERE bloat_pct > 20
ORDER BY bloat_pct DESC;

-- 8. Long-running transactions
SELECT
    pid,
    state,
    query_start,
    NOW() - query_start AS query_duration,
    wait_event_type,
    wait_event,
    LEFT(query, 200) AS query
FROM pg_stat_activity
WHERE state != 'idle'
  AND query_start < NOW() - INTERVAL '1 second'
  AND query NOT LIKE '%pg_stat%'
ORDER BY query_start
LIMIT 20;

-- 9. Lock contention analysis
SELECT
    blocked_locks.pid AS blocked_pid,
    blocked_activity.query AS blocked_query,
    blocking_locks.pid AS blocking_pid,
    blocking_activity.query AS blocking_query,
    blocked_activity.query_start AS blocked_start
FROM pg_locks blocked_locks
JOIN pg_stat_activity blocked_activity ON blocked_activity.pid = blocked_locks.pid
JOIN pg_locks blocking_locks ON blocking_locks.locktype = blocked_locks.locktype
    AND blocking_locks.database IS NOT DISTINCT FROM blocked_locks.database
    AND blocking_locks.relation IS NOT DISTINCT FROM blocked_locks.relation
    AND blocking_locks.page IS NOT DISTINCT FROM blocked_locks.page
    AND blocking_locks.tuple IS NOT DISTINCT FROM blocked_locks.tuple
    AND blocking_locks.virtualxid IS NOT DISTINCT FROM blocked_locks.virtualxid
    AND blocking_locks.transactionid IS NOT DISTINCT FROM blocked_locks.transactionid
    AND blocking_locks.classid IS NOT DISTINCT FROM blocked_locks.classid
    AND blocking_locks.objid IS NOT DISTINCT FROM blocked_locks.objid
    AND blocking_locks.objsubid IS NOT DISTINCT FROM blocked_locks.objsubid
    AND blocking_locks.pid != blocked_locks.pid
JOIN pg_stat_activity blocking_activity ON blocking_activity.pid = blocking_locks.pid
WHERE NOT blocked_locks.granted;

-- 10. Connection pool utilization
SELECT
    state,
    COUNT(*) AS connection_count,
    ROUND(COUNT(*)::numeric * 100 / SUM(COUNT(*)) OVER (), 2) AS pct
FROM pg_stat_activity
WHERE backend_type = 'client backend'
GROUP BY state
ORDER BY connection_count DESC;
