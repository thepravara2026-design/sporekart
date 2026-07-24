-- SporeKart Connection Pool Analysis
-- Monitor and optimize database connection pool

-- 1. Current connection count by state
SELECT
    state,
    COUNT(*) AS count,
    ROUND(COUNT(*)::numeric * 100 / SUM(COUNT(*)) OVER (), 2) AS percentage
FROM pg_stat_activity
WHERE backend_type = 'client backend'
GROUP BY state
ORDER BY count DESC;

-- 2. Connections by database
SELECT
    datname,
    state,
    COUNT(*) AS count,
    ROUND(COUNT(*)::numeric * 100 / SUM(COUNT(*)) OVER (PARTITION BY datname), 2) AS db_pct
FROM pg_stat_activity
WHERE backend_type = 'client backend'
GROUP BY datname, state
ORDER BY datname, count DESC;

-- 3. Connection age analysis
SELECT
    pid,
    state,
    usename,
    application_name,
    client_addr,
    state_change,
    NOW() - state_change AS state_duration,
    LEFT(query, 100) AS current_query
FROM pg_stat_activity
WHERE backend_type = 'client backend'
  AND state = 'idle'
  AND state_change < NOW() - INTERVAL '10 minutes'
ORDER BY state_change
LIMIT 20;

-- 4. Max connections and usage
SELECT
    current_setting('max_connections')::int AS max_connections,
    COUNT(*) AS active_connections,
    ROUND(COUNT(*)::numeric * 100 / current_setting('max_connections')::int, 2) AS usage_pct
FROM pg_stat_activity
WHERE backend_type = 'client backend';

-- 5. Waiting connections (pool exhaustion indicator)
SELECT
    COUNT(*) AS waiting_count,
    ROUND(COUNT(*)::numeric * 100 / NULLIF(current_setting('max_connections')::int, 0), 2) AS waiting_pct
FROM pg_stat_activity
WHERE wait_event_type IS NOT NULL
  AND state = 'active';

-- 6. Connection pool sizing recommendation
WITH connection_stats AS (
    SELECT
        COUNT(*) AS total_connections,
        COUNT(*) FILTER (WHERE state = 'active') AS active_connections,
        COUNT(*) FILTER (WHERE state = 'idle') AS idle_connections,
        COUNT(*) FILTER (WHERE wait_event_type IS NOT NULL) AS waiting_connections,
        ROUND(AVG(EXTRACT(EPOCH FROM (NOW() - query_start))) FILTER (WHERE state = 'active'), 2) AS avg_query_duration_seconds
    FROM pg_stat_activity
    WHERE backend_type = 'client backend'
)
SELECT
    total_connections,
    active_connections,
    idle_connections,
    waiting_connections,
    avg_query_duration_seconds,
    GREATEST(active_connections + 5, ROUND(active_connections * 1.5)::int) AS recommended_min_pool,
    LEAST(GREATEST(active_connections * 4, 20), 100) AS recommended_max_pool,
    CASE
        WHEN waiting_connections > 0 THEN 'POOL EXHAUSTION - Increase max pool size'
        WHEN idle_connections > active_connections * 2 THEN 'OVER-PROVISIONED - Reduce pool size'
        WHEN idle_connections < 2 AND active_connections > total_connections * 0.8 THEN 'NEAR CAPACITY - Monitor closely'
        ELSE 'Pool size adequate'
    END AS recommendation
FROM connection_stats;

-- 7. Transaction duration analysis
SELECT
    pid,
    datname,
    usename,
    application_name,
    state,
    NOW() - query_start AS transaction_duration,
    CASE
        WHEN NOW() - query_start > INTERVAL '30 seconds' THEN 'CRITICAL - Long transaction'
        WHEN NOW() - query_start > INTERVAL '10 seconds' THEN 'WARNING - Extended transaction'
        WHEN NOW() - query_start > INTERVAL '5 seconds' THEN 'ATTENTION - Long query'
        ELSE 'Normal'
    END AS severity,
    LEFT(query, 200) AS query
FROM pg_stat_activity
WHERE state = 'active'
  AND query NOT LIKE '%pg_stat%'
  AND query_start < NOW() - INTERVAL '5 seconds'
ORDER BY query_start
LIMIT 20;

-- 8. Prepared statement cache usage
SELECT
    database,
    COUNT(*) AS prepared_statements
FROM pg_prepared_statements
GROUP BY database;

-- 9. Recommended HikariCP configuration based on workload
WITH workload AS (
    SELECT
        COUNT(*) FILTER (WHERE state = 'active') AS peak_active,
        COUNT(*) FILTER (WHERE state = 'idle') AS peak_idle,
        ROUND(AVG(EXTRACT(EPOCH FROM (NOW() - query_start))) FILTER (WHERE state = 'active'), 3) AS avg_query_sec
    FROM pg_stat_activity
    WHERE backend_type = 'client backend'
      AND query_start > NOW() - INTERVAL '5 minutes'
)
SELECT
    'HikariCP Configuration' AS config,
    'maximum-pool-size' AS parameter,
    GREATEST(LEAST(peak_active * 2, 100), 10)::text AS recommended_value,
    peak_active::text || ' peak active connections' AS rationale
FROM workload
UNION ALL
SELECT 'HikariCP Configuration', 'minimum-idle',
    GREATEST(ROUND(peak_idle * 0.5)::int, 5)::text,
    peak_idle::text || ' peak idle connections'
FROM workload
UNION ALL
SELECT 'HikariCP Configuration', 'connection-timeout',
    CASE WHEN avg_query_sec > 1 THEN '5000' ELSE '2000' END,
    avg_query_sec::text || 's avg query duration'
FROM workload
UNION ALL
SELECT 'HikariCP Configuration', 'max-lifetime',
    '1200000', '20 minutes (standard)'
FROM workload;
