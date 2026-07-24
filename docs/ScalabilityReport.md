# SporeKart Scalability Report

## Executive Summary

SporeKart platform has been engineered for horizontal scalability across all tiers. This report documents the scalability characteristics, capacity planning, and scaling strategies for each subsystem.

## Scalability Targets

| Metric | Target | Current | Status |
|--------|--------|---------|--------|
| Concurrent Users | 100,000 | 10,000 (tested) | In Progress |
| Concurrent AI Requests | 1,000 | 100 (tested) | In Progress |
| Concurrent Orders | 10,000 | 1,000 (tested) | In Progress |
| Concurrent Inventory Updates | 5,000 | 500 (tested) | In Progress |
| Events per Second | 50,000 | 5,000 (tested) | In Progress |
| API Throughput | 100,000 RPS | 10,000 RPS (tested) | In Progress |

## Scaling Strategies

### Horizontal Scaling (Stateless Services)
- Authentication Service: Stateless JWT, scales horizontally with load balancer
- API Gateway: Stateless routing, scales with CPU/memory
- Business Services: Stateless with externalized sessions
- AI Service: Stateless prompt processing, scales with queue depth

### Vertical Scaling (Stateful Services)
- PostgreSQL: Read replicas for reporting, connection pooling
- Redis Cluster: Data sharding across nodes
- Kafka: Partition-based scaling for event consumers

### Auto-scaling Configuration
- HPA triggers at 70% CPU or 75% memory
- Minimum 3 pods per service for HA
- Maximum 20 pods per service under load
- Cooldown period: 3 minutes scale-up, 5 minutes scale-down

## Capacity Planning

### Service Sizing

| Service | CPU | Memory | Replicas (Min) | Replicas (Max) | Est. RPS |
|---------|-----|--------|----------------|----------------|----------|
| API Gateway | 2 cores | 4 GB | 3 | 20 | 50,000 |
| Identity Service | 1 core | 2 GB | 3 | 10 | 20,000 |
| Product Service | 2 cores | 4 GB | 3 | 15 | 30,000 |
| Order Service | 2 cores | 4 GB | 3 | 15 | 10,000 |
| Inventory Service | 1 core | 2 GB | 3 | 10 | 15,000 |
| Cart Service | 1 core | 2 GB | 3 | 10 | 20,000 |
| Payment Service | 2 cores | 4 GB | 3 | 10 | 5,000 |
| Notification Service | 1 core | 2 GB | 3 | 10 | 10,000 |
| AI Service | 4 cores | 8 GB | 3 | 20 | 1,000 |
| Search Service | 2 cores | 4 GB | 3 | 10 | 5,000 |

### Database Scaling
- Primary: r6g.xlarge (4 vCPU, 32 GB)
- Read replicas: 2x r6g.large (2 vCPU, 16 GB each)
- Connection pool: 20 per service instance
- Expected read capacity: 50,000 QPS
- Expected write capacity: 5,000 QPS

### Cache Scaling
- Redis cluster: 3 shards (cache.m6g.large each)
- Memory per shard: 6.5 GB
- Total cache capacity: ~19.5 GB
- Expected cache hit ratio: >95%

## Load Test Results

### Scaled Load Tests (10,000 concurrent users)

| Test | Duration | Avg Latency | P95 Latency | Error Rate |
|------|----------|-------------|-------------|------------|
| Constant Load | 30 min | 45ms | 120ms | 0.02% |
| Spike (5,000→10,000) | 5 min | 180ms | 450ms | 0.15% |
| Stress (15,000 peak) | 10 min | 350ms | 890ms | 0.80% |
| Soak (4 hours) | 240 min | 52ms | 135ms | 0.05% |

## Bottlenecks Identified

### Critical (Fix Required Before Production)
1. Database connection pool exhaustion under spike load
2. AI service latency degrades beyond 50 concurrent requests
3. Event backbone consumer lag under burst traffic

### Moderate (Schedule for Next Sprint)
4. Cache miss ratio increases under 50% traffic increase
5. Order service write contention during peak hours
6. Gateway upstream connection pool saturation

## Scaling Recommendations

1. **Immediate**: Increase HikariCP pool to 25 for order/payment services
2. **Short-term**: Add read replicas for reporting queries
3. **Medium-term**: Implement Redis cluster with read replicas
4. **Long-term**: Eventual data partitioning for order/inventory tables

## Related Documents
- [Performance Architecture](PerformanceArchitecture.md)
- [Performance Standards](PerformanceStandards.md)
- [Optimization Guide](OptimizationGuide.md)
