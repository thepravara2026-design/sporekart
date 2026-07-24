-- SporeKart N+1 Query Detection
-- Identifies repeated identical queries (N+1 pattern)

-- 1. Detect N+1 patterns from pg_stat_statements
WITH query_stats AS (
    SELECT
        queryid,
        query,
        calls,
        ROUND(mean_exec_time::numeric, 2) AS mean_exec_time_ms,
        ROUND(total_exec_time::numeric, 2) AS total_exec_time_ms,
        ROW_NUMBER() OVER (PARTITION BY LEFT(query, 100) ORDER BY calls DESC) AS rn
    FROM pg_stat_statements
    WHERE calls > 10
      AND query NOT LIKE '%pg_stat%'
      AND query NOT LIKE '%information_schema%'
)
SELECT
    queryid,
    LEFT(query, 150) AS query_preview,
    calls,
    mean_exec_time_ms,
    ROUND(total_exec_time_ms::numeric, 2) AS total_ms,
    ROUND(total_exec_time_ms::numeric / 1000, 2) AS total_seconds
FROM query_stats
WHERE rn = 1
ORDER BY calls DESC
LIMIT 30;

-- 2. Detect repeated SELECT by same connection (classic N+1)
SELECT
    pid,
    COUNT(*) AS query_count,
    MIN(query_start) AS first_query,
    MAX(query_start) AS last_query,
    ROUND(EXTRACT(EPOCH FROM MAX(query_start) - MIN(query_start))::numeric, 2) AS duration_seconds,
    STRING_AGG(DISTINCT LEFT(query, 100), ' | ') AS sample_queries
FROM pg_stat_activity
WHERE state = 'active'
  AND query NOT LIKE '%pg_stat%'
  AND query_start > NOW() - INTERVAL '5 minutes'
GROUP BY pid
HAVING COUNT(*) > 10
ORDER BY query_count DESC;

-- 3. Detect batch selects with similar WHERE patterns
SELECT
    queryid,
    calls,
    ROUND(mean_exec_time::numeric, 2) AS mean_ms,
    ROUND(total_exec_time::numeric, 2) AS total_ms,
    CASE
        WHEN query ~* 'WHERE.*IN\s*\(' THEN 'IN-clause (potential N+1)'
        WHEN query ~* 'WHERE.*=\s*\$1' AND calls > 100 THEN 'Single-param WHERE (potential N+1)'
        WHEN query ~* 'SELECT.*FROM.*WHERE.*id\s*=' THEN 'Entity lookup by ID (check batching)'
        ELSE 'Normal'
    END AS n_plus_one_risk,
    LEFT(query, 200) AS query_preview
FROM pg_stat_statements
WHERE calls > 50
  AND (
      query ~* 'WHERE.*IN\s*\('
      OR (query ~* 'WHERE.*=\s*\$1' AND query ~* 'SELECT')
  )
ORDER BY calls DESC
LIMIT 20;

-- 4. Analyze join patterns (missing JOIN batching)
SELECT
    schemaname,
    tablename,
    seq_scan,
    seq_tup_read,
    idx_scan,
    n_live_tup,
    n_dead_tup,
    ROUND(COALESCE(seq_tup_read::numeric / NULLIF(seq_scan, 0), 0), 2) AS tuples_per_seq
FROM pg_stat_user_tables
WHERE seq_scan > 50
  AND n_live_tup > 1000
ORDER BY seq_tup_read DESC
LIMIT 20;

-- 5. Detect lazy loading in parent-child relationships
-- High-call-count SELECTs on FK columns suggest N+1
SELECT
    queryid,
    calls,
    mean_exec_time,
    total_exec_time,
    CASE
        WHEN query ~* 'SELECT.*FROM.*WHERE.*_id\s*=' THEN 'FK lookup (potential lazy load)'
        WHEN query ~* 'SELECT.*FROM.*WHERE.*\.id\s*=' THEN 'PK lookup in loop'
        ELSE 'Review'
    END AS pattern,
    LEFT(query, 200) AS query_preview
FROM pg_stat_statements
WHERE calls > 100
  AND (query ~* 'WHERE.*_id\s*=' OR query ~* 'WHERE.*\.id\s*=')
  AND mean_exec_time > 1
ORDER BY calls * mean_exec_time DESC
LIMIT 20;

-- 6. Generate recommendations
SELECT
    'N+1 Query Risk' AS finding,
    COUNT(*) AS occurrence_count,
    ROUND(SUM(total_exec_time)::numeric / 1000, 2) AS total_seconds_consumed,
    CASE
        WHEN COUNT(*) > 50 THEN 'CRITICAL - Immediate optimization required'
        WHEN COUNT(*) > 20 THEN 'HIGH - Optimize in current sprint'
        WHEN COUNT(*) > 10 THEN 'MEDIUM - Schedule for next sprint'
        ELSE 'LOW - Monitor'
    END AS priority
FROM pg_stat_statements
WHERE calls > 100
  AND (query ~* 'WHERE.*_id\s*=' OR query ~* 'WHERE.*IN\s*\(')
HAVING COUNT(*) > 5;
