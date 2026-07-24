# SporeKart Performance Architecture

## Overview

The SporeKart Performance Architecture defines a comprehensive framework for achieving enterprise-grade latency, throughput, and scalability targets across all platform subsystems. This document describes the performance engineering approach, optimization strategies, and measurement framework.

## Performance Targets

| Subsystem | Metric | Target P95 | Target P99 |
|-----------|--------|------------|------------|
| Authentication | Login/Validate | < 100ms | < 200ms |
| Authorization | Permission Check | < 10ms | < 20ms |
| API Response | All Endpoints | < 200ms | < 500ms |
| Repository Read | Data Access | < 50ms | < 100ms |
| Repository Write | Data Mutation | < 100ms | < 200ms |
| AI Request | Completion | < 3s | < 5s |
| Knowledge Retrieval | Search | < 200ms | < 500ms |
| Prompt Build | Context Assembly | < 100ms | < 200ms |
| Event Publish | Kafka/Backbone | < 5ms | < 10ms |
| Event Dispatch | Handler Routing | < 10ms | < 20ms |
| Database Query | All Queries | < 50ms | < 100ms |
| Cache Access | Hit/Miss | < 1ms | < 5ms |

## Architecture Layers

### 1. Application Layer
- **Virtual Threading**: Spring Boot 3.3 + Java 21 virtual threads for IO-bound operations
- **Async Execution**: Dedicated thread pools for events, AI, and background tasks
- **Caching**: Multi-tier cache (local + Redis) with configurable TTL per domain
- **Connection Pooling**: HikariCP with tuned pool sizes per service profile

### 2. API Gateway Layer
- **Rate Limiting**: Redis-based token bucket at service route level
- **Circuit Breakers**: Resilience4j per upstream service with fallback routes
- **Connection Pooling**: Elastic HTTP client pool with 1000 max connections
- **Request Buffering**: Configurable buffer size for large payloads

### 3. Database Layer
- **Connection Pool**: HikariCP with 20 max connections, 2s timeout
- **Query Optimization**: Batch operations, JDBC batching, query plan caching
- **Index Strategy**: Composite + covering + partial indexes for hot paths
- **Connection Monitoring**: Leak detection, MBean registration

### 4. Caching Layer
- **Local Cache**: ConcurrentHashMap-based with TTL expiry for hot data
- **Redis Cache**: Lettuce client with connection pooling for shared state
- **Cache Domains**: Separate cache regions per business domain
- **Eviction Policies**: LRU for products, LFU for AI responses, TTL for sessions

### 5. Event Layer
- **Async Publishing**: Dedicated event executor with backpressure
- **Batch Processing**: Configurable batch sizes for event consumption
- **Dead Letter Queue**: Isolated DLQ with retry policies
- **Idempotency Cache**: Event deduplication within TTL window

## Optimization Strategies

### Memory Optimization
- Object pooling for frequently created objects
- Direct buffer allocation in Undertow
- GC tuning for throughput (G1GC + parallel)
- String interning for repeated values

### CPU Optimization
- Virtual threads for lightweight concurrency
- Asynchronous IO throughout the stack
- Lazy initialization for expensive resources
- Computation caching for repeated calculations

### IO Optimization
- Connection pooling across all data stores
- Response compression (gzip) for text payloads
- Batch reads/writes where applicable
- Read replicas for reporting queries

## Monitoring & Observability

### Metrics Collection
- Prometheus metrics exported at 15s intervals
- Histogram percentiles (P50, P95, P99) for all operations
- SLO tracking at 10ms/50ms/100ms/200ms/500ms/1s/2s buckets
- JMX MBeans for HikariCP, Tomcat, and JVM

### Alert Thresholds
- P95 latency > 500ms for 2 minutes → Critical
- Error rate > 1% for 2 minutes → Critical
- Connection pool > 80% for 2 minutes → Warning
- Cache hit ratio < 80% for 5 minutes → Warning
- GC overhead > 10% for 5 minutes → Warning

## Failure Resilience

### Circuit Breaker Configuration
- Sliding window: 100 calls
- Failure threshold: 50%
- Slow call threshold: 2s (50%)
- Half-open wait: 10s
- Per-service circuit breakers with fallback routes

### Retry Policy
- Max attempts: 3
- Backoff: Exponential (500ms base, 2x multiplier)
- Retryable: ConnectException, SocketTimeoutException, DataAccessException

### Bulkhead Pattern
- Max concurrent: 20 calls per service
- Queue wait: 100ms max
- Thread pool: 8 max threads per service

## Automation

### CI/CD Integration
- Smoke perf tests on every PR
- Full benchmark suite on scheduled runs
- Regression comparison against baseline
- Performance certification on sporetest merge

### Performance Testing
- k6-based load testing framework
- Constant load, spike, stress, and soak scenarios
- Automated threshold validation
- Baseline generation and comparison

## Related Documents
- [Scalability Report](ScalabilityReport.md)
- [Benchmark Results](BenchmarkResults.md)
- [Optimization Guide](OptimizationGuide.md)
- [Load Testing Report](LoadTestingReport.md)
- [Performance Standards](PerformanceStandards.md)
- [Performance Certification](PerformanceCertification.md)
