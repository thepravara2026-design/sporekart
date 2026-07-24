-- SporeKart Database Maintenance Configuration
-- Vacuum, Analyze, and Autovacuum Tuning

-- 1. Table statistics age
SELECT
    schemaname,
    relname,
    n_live_tup,
    n_dead_tup,
    ROUND(COALESCE(n_dead_tup::numeric / NULLIF(n_live_tup, 0), 0) * 100, 2) AS dead_tuple_pct,
    last_autovacuum,
    last_autoanalyze,
    last_vacuum,
    last_analyze,
    CASE
        WHEN COALESCE(n_dead_tup::numeric / NULLIF(n_live_tup, 0), 0) > 0.2 THEN 'CRITICAL - Vacuum required'
        WHEN COALESCE(n_dead_tup::numeric / NULLIF(n_live_tup, 0), 0) > 0.1 THEN 'WARNING - Vacuum recommended'
        WHEN last_analyze IS NULL OR last_analyze < NOW() - INTERVAL '1 day' THEN 'INFO - Analyze recommended'
        ELSE 'OK'
    END AS status
FROM pg_stat_user_tables
WHERE n_live_tup > 1000
ORDER BY n_dead_tup DESC
LIMIT 30;

-- 2. Autovacuum current activity
SELECT
    pid,
    datname,
    relid::regclass AS table_name,
    phase,
    heap_blks_total,
    heap_blks_scanned,
    heap_blks_vacuumed,
    index_vacuum_count,
    max_dead_tuples,
    num_dead_tuples
FROM pg_stat_progress_vacuum;

-- 3. Autovacuum configuration
SELECT
    name,
    setting,
    unit,
    short_desc
FROM pg_settings
WHERE name LIKE '%autovacuum%'
ORDER BY name;

-- 4. Recommended autovacuum tuning for SporeKart workload
SELECT
    'autovacuum' AS parameter_group,
    'autovacuum_max_workers' AS parameter,
    '5' AS recommended_value,
    '3' AS current_value,
    'Increase for concurrent table maintenance'
FROM pg_settings WHERE name = 'autovacuum_max_workers'
UNION ALL
SELECT 'autovacuum', 'autovacuum_naptime', '30s', setting, 'Faster vacuum cycle'
FROM pg_settings WHERE name = 'autovacuum_naptime'
UNION ALL
SELECT 'autovacuum', 'autovacuum_vacuum_threshold', '100', setting, 'Lower threshold for smaller tables'
FROM pg_settings WHERE name = 'autovacuum_vacuum_threshold'
UNION ALL
SELECT 'autovacuum', 'autovacuum_vacuum_scale_factor', '0.05', setting, 'More aggressive vacuum trigger (5% dead tuples)'
FROM pg_settings WHERE name = 'autovacuum_vacuum_scale_factor'
UNION ALL
SELECT 'autovacuum', 'autovacuum_analyze_threshold', '50', setting, 'Lower analyze threshold'
FROM pg_settings WHERE name = 'autovacuum_analyze_threshold'
UNION ALL
SELECT 'autovacuum', 'autovacuum_analyze_scale_factor', '0.025', setting, 'More frequent analyze (2.5% changed)'
FROM pg_settings WHERE name = 'autovacuum_analyze_scale_factor';

-- 5. Table-level autovacuum tuning for high-traffic tables
ALTER TABLE IF EXISTS orders SET (
    autovacuum_vacuum_scale_factor = 0.02,
    autovacuum_analyze_scale_factor = 0.01,
    autovacuum_vacuum_threshold = 1000,
    autovacuum_vacuum_cost_limit = 2000
);

ALTER TABLE IF EXISTS order_items SET (
    autovacuum_vacuum_scale_factor = 0.02,
    autovacuum_analyze_scale_factor = 0.01,
    autovacuum_vacuum_threshold = 1000
);

ALTER TABLE IF EXISTS products SET (
    autovacuum_vacuum_scale_factor = 0.03,
    autovacuum_analyze_scale_factor = 0.02,
    autovacuum_vacuum_threshold = 500
);

ALTER TABLE IF EXISTS inventory SET (
    autovacuum_vacuum_scale_factor = 0.02,
    autovacuum_analyze_scale_factor = 0.01,
    autovacuum_vacuum_threshold = 500,
    autovacuum_vacuum_cost_limit = 2000
);

ALTER TABLE IF EXISTS cart SET (
    autovacuum_vacuum_scale_factor = 0.05,
    autovacuum_analyze_scale_factor = 0.02,
    autovacuum_vacuum_threshold = 500
);

ALTER TABLE IF EXISTS notifications SET (
    autovacuum_vacuum_scale_factor = 0.02,
    autovacuum_analyze_scale_factor = 0.01,
    autovacuum_vacuum_threshold = 1000
);

ALTER TABLE IF EXISTS events SET (
    autovacuum_vacuum_scale_factor = 0.01,
    autovacuum_analyze_scale_factor = 0.005,
    autovacuum_vacuum_threshold = 2000,
    autovacuum_vacuum_cost_limit = 2000
);

ALTER TABLE IF EXISTS ai_queries SET (
    autovacuum_vacuum_scale_factor = 0.03,
    autovacuum_analyze_scale_factor = 0.02,
    autovacuum_vacuum_threshold = 500
);

-- 6. Manual vacuum recommendations
SELECT
    schemaname,
    relname,
    n_dead_tup,
    ROUND(n_dead_tup::numeric * 100 / NULLIF(n_live_tup + n_dead_tup, 0), 2) AS dead_pct,
    CASE
        WHEN COALESCE(n_dead_tup, 0) > 100000 THEN 'VACUUM ANALYZE immediately'
        WHEN COALESCE(n_dead_tup, 0) > 50000 THEN 'VACUUM ANALYZE recommended soon'
        WHEN COALESCE(n_dead_tup, 0) > 10000 THEN 'Monitor'
        ELSE 'OK'
    END AS action,
    'VACUUM ANALYZE ' || relname || ';' AS vacuum_command
FROM pg_stat_user_tables
WHERE n_dead_tup > 10000
ORDER BY n_dead_tup DESC;

-- 7. Dead row accumulation rate (for capacity planning)
SELECT
    schemaname,
    relname,
    n_tup_ins,
    n_tup_upd,
    n_tup_del,
    n_dead_tup,
    ROUND(COALESCE(n_dead_tup::numeric / NULLIF(GREATEST(n_tup_ins + n_tup_upd + n_tup_del, 1), 0), 0) * 100, 2) AS dead_accumulation_rate
FROM pg_stat_user_tables
WHERE n_tup_ins + n_tup_upd + n_tup_del > 10000
ORDER BY dead_accumulation_rate DESC
LIMIT 10;
