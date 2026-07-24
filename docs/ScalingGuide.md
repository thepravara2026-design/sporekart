# Scaling Guide

**SporeKart Enterprise Platform v2.0**  
**Document:** ScalingGuide.md  
**Last Updated:** 2026-07-24

---

## Scaling Architecture

SporeKart uses a multi-dimensional scaling strategy:

1. **Horizontal Scaling** (pods/instances) — Primary strategy
2. **Vertical Scaling** (CPU/memory) — Secondary strategy
3. **Elastic Scaling** (event-driven) — For variable workloads

---

## Horizontal Pod Autoscaling

### Configuration

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
spec:
  minReplicas: 2
  maxReplicas: 20
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
    - type: Resource
      resource:
        name: memory
        target:
          type: Utilization
          averageUtilization: 80
```

### Service Scaling Parameters

| Service | Min | Max | CPU Target | Memory Target | Scale-Up Cooldown |
|---------|-----|-----|------------|--------------|-------------------|
| gateway-service | 2 | 10 | 70% | 80% | 3 min |
| identity-service | 2 | 6 | 70% | 80% | 3 min |
| catalog-service | 2 | 6 | 70% | 80% | 3 min |
| cart-service | 2 | 6 | 70% | 80% | 3 min |
| order-service | 2 | 8 | 70% | 80% | 3 min |
| payment-service | 2 | 8 | 70% | 80% | 3 min |
| inventory-service | 2 | 6 | 70% | 80% | 3 min |
| notification-service | 2 | 4 | 70% | 80% | 5 min |
| fulfillment-service | 2 | 4 | 70% | 80% | 5 min |
| ai-service | 2 | 20 | 60% | 75% | 5 min |
| training-service | 1 | 4 | 70% | 80% | 5 min |
| admin-service | 1 | 4 | 70% | 80% | 5 min |
| analytics-service | 1 | 4 | 70% | 80% | 5 min |
| search-service | 1 | 4 | 70% | 80% | 5 min |
| content-service | 1 | 4 | 70% | 80% | 5 min |
| risk-service | 1 | 4 | 70% | 80% | 5 min |
| support-service | 1 | 4 | 70% | 80% | 5 min |

---

## Vertical Scaling

### CPU/Memory Allocation

| Service | CPU Request | CPU Limit | Memory Request | Memory Limit |
|---------|-------------|-----------|----------------|--------------|
| gateway-service | 256m | 512m | 512Mi | 1Gi |
| identity-service | 256m | 1 | 512Mi | 1Gi |
| catalog-service | 128m | 256m | 256Mi | 512Mi |
| cart-service | 128m | 256m | 256Mi | 512Mi |
| order-service | 256m | 512m | 512Mi | 1Gi |
| payment-service | 256m | 512m | 512Mi | 1Gi |
| inventory-service | 128m | 256m | 256Mi | 512Mi |
| notification-service | 128m | 256m | 256Mi | 512Mi |
| fulfillment-service | 128m | 256m | 256Mi | 512Mi |
| ai-service | 512m | 2 | 1Gi | 4Gi |
| training-service | 256m | 512m | 512Mi | 1Gi |
| admin-service | 128m | 256m | 256Mi | 512Mi |
| analytics-service | 256m | 512m | 512Mi | 1Gi |
| all others | 128m | 256m | 256Mi | 512Mi |

### JVM Heap Settings

```yaml
JAVA_OPTS: "-Xmx512m -Xms256m -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"
```

AI service (larger heap):
```yaml
JAVA_OPTS: "-Xmx2g -Xms512m -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+ExitOnOutOfMemoryError"
```

---

## Database Scaling

### RDS Scaling

| Environment | Instance Type | vCPU | Memory | Storage | IOPS |
|-------------|--------------|------|--------|---------|------|
| Development | db.t3.medium | 2 | 4GB | 100GB gp3 | 3000 |
| Staging | db.r6g.large | 2 | 16GB | 200GB gp3 | 5000 |
| Production | db.r6g.xlarge | 4 | 32GB | 500GB gp3 | 10000 |
| Production (future) | db.r6g.2xlarge | 8 | 64GB | 1TB io2 | 20000 |

### Connection Pool Tuning

| Environment | Max Connections | HikariCP Pool | Statement Cache |
|-------------|----------------|---------------|-----------------|
| Development | 20 | 5 | 100 |
| Staging | 50 | 10 | 200 |
| Production | 200 | 20 | 500 |

---

## Caching Strategy

### Redis Cluster Scaling

| Environment | Node Type | Shards | Replicas | Memory |
|-------------|-----------|--------|----------|--------|
| Development | cache.t3.micro | 1 | 0 | 512MB |
| Staging | cache.r6g.large | 1 | 1 | 13GB |
| Production | cache.r6g.xlarge | 3 | 1 | 42GB |

### Cache TTLs

| Cache | TTL | Eviction Policy | Max Entries |
|-------|-----|-----------------|-------------|
| Session tokens | 15 min | LRU | 100000 |
| Product catalog | 5 min | LRU | 50000 |
| User profiles | 30 min | LRU | 100000 |
| API responses | 1 min | LRU | 10000 |
| AI model cache | 1 hour | LRU | 1000 |

---

## Event-Driven Scaling

### Kafka Scaling

| Environment | Brokers | Partitions per Topic | Replication Factor | Retention |
|-------------|---------|---------------------|-------------------|-----------|
| Development | 1 | 1 | 1 | 24h |
| Staging | 3 | 3 | 2 | 48h |
| Production | 3 | 6 | 3 | 7 days |

### Consumer Scaling

```
Topic: order-events
├── Partition 0 → Consumer instance 1
├── Partition 1 → Consumer instance 2
├── Partition 2 → Consumer instance 3
├── Partition 3 → Consumer instance 4
├── Partition 4 → Consumer instance 5
└── Partition 5 → Consumer instance 6
```

---

## Performance Testing Plan

| Test | Tool | Frequency | Target |
|------|------|-----------|--------|
| Load test (gateway) | k6 | Monthly | 1000 req/s, p99 < 200ms |
| Load test (auth) | k6 | Monthly | 100 req/s, p99 < 100ms |
| Load test (orders) | k6 | Monthly | 500 req/s, p99 < 500ms |
| Load test (AI) | k6 | Quarterly | 50 req/s, p99 < 5s |
| Stress test | k6 | Quarterly | 2x peak load, no crash |
| Endurance test | k6 | Quarterly | 2 hours at peak load |
