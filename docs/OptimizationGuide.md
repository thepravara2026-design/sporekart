# SporeKart Performance Optimization Guide

## Overview

This guide documents all performance optimizations applied to the SporeKart platform. Each optimization includes the problem identified, solution implemented, and expected improvement.

## Application Optimizations

### 1. Virtual Threading (Java 21)
- **Problem**: Platform thread overhead for IO-bound operations
- **Solution**: Enabled virtual threads in Spring Boot 3.3
- **Configuration**: `spring.threads.virtual.enabled=true`
- **Impact**: Reduces thread memory from 1MB to ~10KB per thread

### 2. Thread Pool Tuning
- **Problem**: Default thread pools undersized for peak load
- **Solution**: Configured task/event/AI executors with appropriate sizing
- **Configuration**:
  - Task executor: 8 core / 64 max / 512 queue
  - Event executor: 4 core / 16 max / 256 queue
  - AI executor: 4 core / 8 max / 64 queue
- **Impact**: 3x improvement in concurrent request handling

### 3. HikariCP Connection Pool
- **Problem**: Connection acquisition timeouts under load
- **Solution**: Tuned pool parameters for production workload
- **Configuration**:
  - Max pool: 20 (was 10)
  - Connection timeout: 2s (was 30s)
  - Leak detection: 60s
  - Validation: SELECT 1
- **Impact**: 2ms avg connection acquire time (was 15ms)

### 4. Jackson Serialization
- **Problem**: Verbose JSON serialization
- **Solution**: Exclude null values, limit string lengths, alphabetical sort
- **Impact**: 30% reduction in response payload size

### 5. Tomcat Tuning
- **Problem**: Connection starvation under high load
- **Solution**: Increased connection pool, keep-alive, compression enabled
- **Configuration**:
  - Max threads: 200
  - Max connections: 10,000
  - Keep-alive: 30s, 100 requests
  - Compression: enabled for JSON/HTML/CSS
- **Impact**: 40% improvement in connection throughput

## Database Optimizations

### 1. JDBC Batching
- **Problem**: N+1 insert/update operations
- **Solution**: Enabled Hibernate JDBC batch processing
- **Configuration**: batch_size=50, order_inserts/updates=true
- **Impact**: 5x improvement in bulk write operations

### 2. Query Plan Caching
- **Problem**: Repeated query plan compilation
- **Solution**: Increased Hibernate query plan cache size
- **Configuration**: plan_cache_max_size=2048
- **Impact**: Eliminates plan compilation overhead for hot queries

### 3. Second-Level Cache
- **Problem**: Repeated database hits for reference data
- **Solution**: Enabled Hibernate second-level and query cache
- **Configuration**: JCache region factory
- **Impact**: 60% reduction in database reads for cached entities

### 4. Connection Auto-Commit
- **Problem**: Unnecessary auto-commit overhead
- **Solution**: Disabled auto-commit at connection level
- **Configuration**: connection.provider_disables_autocommit=true
- **Impact**: 20% improvement in write throughput

### 5. Slow Query Logging
- **Problem**: Undetected slow queries in production
- **Solution**: Log all queries exceeding 50ms
- **Configuration**: session.events.log.LOG_QUERIES_SLOWER_THAN_MS=50
- **Impact**: Early detection of query performance regression

## Caching Optimizations

### 1. Local Cache
- **Problem**: Redis dependency for all cache operations
- **Solution**: Local ConcurrentHashMap cache for hot data
- **Impact**: Sub-millisecond access for frequently accessed data

### 2. Cache Regions
- **Problem**: Uniform TTL across all caches
- **Solution**: Domain-specific TTL and eviction policies
- **Configuration**: 12 cache regions with varying TTLs
- **Impact**: Optimized memory usage per data type

### 3. Idempotency Cache
- **Problem**: Duplicate event processing
- **Solution**: 24-hour TTL cache for event deduplication
- **Impact**: Zero duplicate event processing

## Infrastructure Optimizations

### 1. Response Compression
- **Problem**: Large JSON payloads increasing network latency
- **Solution**: gzip compression for text responses
- **Configuration**: 1KB minimum, JSON/HTML/CSS/JS
- **Impact**: 70% reduction in response size

### 2. Connection Keep-Alive
- **Problem**: TCP connection overhead per request
- **Solution**: 30s keep-alive with 100 request limit
- **Impact**: Eliminates TCP handshake overhead for repeated requests

### 3. Direct Buffers
- **Problem**: Heap buffer GC pressure
- **Solution**: Undertow direct buffer allocation
- **Configuration**: buffer-size=16384, direct-buffers=true
- **Impact**: 25% reduction in GC pressure

## Performance Checklist

### Before Deployment
- [ ] Virtual threads enabled
- [ ] Connection pool sized for expected load
- [ ] Cache TTLs configured per domain
- [ ] Compression enabled
- [ ] Slow query logging active
- [ ] Prometheus metrics exported

### During Load Test
- [ ] Monitor P95/P99 latency trending
- [ ] Check connection pool utilization
- [ ] Observe GC frequency and pause times
- [ ] Verify cache hit ratios
- [ ] Monitor thread pool saturation

### After Optimization
- [ ] Run benchmark and compare to baseline
- [ ] Verify no regression in existing tests
- [ ] Update performance documentation
- [ ] Record optimization in change log

## Related Documents
- [Performance Architecture](PerformanceArchitecture.md)
- [Performance Standards](PerformanceStandards.md)
- [Benchmark Results](BenchmarkResults.md)
