# SporeKart Enterprise AI Platform — Performance Review (Gate Step 12)

**Document:** Performance Review — Phase 5 Entry Gate, Step 12
**Prepared by:** Enterprise Architecture Review Board (ARB)
**Date:** 2026-07-12
**Service under review:** `services/ai-service` (Spring Boot 3.x, Java 21)
**Deployment topology:** Modular monolith (single deployable, in-process modules)
**Method:** Architectural evaluation only — **Maven not installed; no runtime benchmark, JMH run, or load test executed.** Findings derive from static review of the architecture, configuration, and in-suite performance tests.

---

## 1. Purpose

This document is **Step 12 of the Phase 5 entry gate**. It evaluates the performance characteristics of the SporeKart AI platform by reasoning about its architecture, caching strategy, data-access patterns, messaging topology, and concurrency model. Because the build environment lacks Maven, the in-suite performance tests (`performance/ApiEndpointLatencyTest`, `RedisCachePerformanceTest`, `KafkaThroughputTest`, `GovernancePerformanceBenchmark`) were **not executed**; they are treated as design intent and smoke-level guards. All numeric values below are **architecture-derived estimates and design targets**, not measured results, and must be validated by a load test in CI/staging before production sign-off.

---

## 2. Evaluation Dimensions

### 2.1 API latency (REST + cache-first)
The REST surface is served in-process by a Spring MVC layer that delegates to application services following a **cache-first** pattern: reads consult namespaced Redis caches before falling through to the database. For cache-hit read paths, end-to-end latency is dominated by serialization and network round-trip and is expected to be low (single-digit to low-tens of milliseconds). Cache-miss and write paths incur database and, where applicable, event-publish costs. Because the platform is a modular monolith, there is **no inter-service network hop** between modules — governance, risk, and AI-capability calls resolve as in-process method invocations, which is a structural latency advantage over a microservice decomposition.

**Assessment:** Sound. Cache-first design keeps hot read paths fast; no architectural latency red flags.

### 2.2 Database performance (indexed queries, Flyway)
Persistence uses JPA repositories over a relational store with schema managed by **Flyway** migrations (guarded by `FlywayMigrationTest` and `DatabaseMigrationIntegrationTest`). Aggregates use UUID primary keys with audit and soft-delete columns. Repository slices (`@DataJpaTest`) exercise derived and custom queries. Indexed access on primary keys and foreign keys keeps typical lookups efficient; the main watch-item is ensuring supporting indexes exist for high-cardinality filter/sort columns used by registry and analytics queries.

**Assessment:** Sound, with a recommendation to verify composite indexes for analytics/reporting query patterns under production data volumes.

### 2.3 Redis hit ratio (namespaced TTLs)
Each module ships a dedicated Redis cache service (e.g. `GovernanceRedisCacheService`, `RiskRedisCacheService`, `SemanticRedisCacheService`, `WorkflowRedisCacheService`, plus the shared `AiCacheService`). Caches are **namespaced per module** with explicit TTLs, which prevents key collisions and enables per-domain eviction tuning. Hit ratios depend on workload locality; for reference/config-style data (registries, policy, compliance frameworks) hit ratios should be high, while for user-specific semantic queries they will be lower and benefit from embedding-cache reuse (`EmbeddingCacheService`).

**Assessment:** Strong caching architecture. Hit ratio is workload-dependent and should be monitored per namespace.

### 2.4 Kafka throughput (3 partitions/topics)
Asynchronous domain events are published through per-module Kafka publishers (`GovernanceKafkaEventPublisher`, `RiskKafkaEventPublisher`, `WorkflowKafkaEventPublisher`, etc.) and validated by `KafkaEventFlowIntegrationTest` and `KafkaThroughputTest`. Topics are provisioned with **3 partitions**, enabling up to three-way consumer parallelism per topic. For current governance/audit event volumes this is adequate; partition count is the primary horizontal-throughput lever should event rates grow.

**Assessment:** Adequate for expected volumes. Partition count is a straightforward future scaling knob.

### 2.5 AI response latency (provider abstraction + fallback)
AI calls go through a **provider abstraction** (`provider` module: `ProviderSelectorImpl`, `ProviderFactoryImpl`, `ProviderFailoverImpl`, `ProviderHealthServiceImpl`) that selects a provider, monitors health, and **fails over** on error. External model-provider latency dominates this path and is outside the platform's control; the abstraction's value is resilience (fallback) and the ability to route to faster/cheaper providers. Prompt orchestration and embedding caching reduce redundant calls.

**Assessment:** Well-architected. Latency is bounded by upstream providers; fallback protects tail latency and availability.

### 2.6 Governance latency (9-stage pipeline)
The governance pipeline applies a multi-stage control flow (policy → decision → approval → compliance → risk → audit and related stages; ~9 stages) before an action is permitted. This **adds latency by design** — each stage performs evaluation and audit work. This cost is **necessary and accepted**: it is the platform's core control-plane guarantee. The pipeline runs in-process (no network hops between stages) and is cache-assisted (policy/compliance/decision caches), which keeps the added latency bounded. `GovernancePipelineIntegrationTest` and `GovernancePerformanceBenchmark` cover this path.

**Assessment:** Acceptable. The added latency is intrinsic to the governance value proposition and is mitigated by in-process execution and caching.

### 2.7 Memory usage
As a modular monolith, all modules share one JVM heap. This is memory-efficient overall (one runtime, shared caches, no per-service duplication) but concentrates footprint in a single process. Redis offloads cache state out of heap. The main heap-pressure sources are embedding/vector data in the semantic module and large in-flight batch operations (`EmbeddingBatchProcessor`); these should be sized with bounded batch limits.

**Assessment:** Efficient by design. Monitor heap under semantic/batch workloads.

### 2.8 Thread utilization
Synchronous REST handling uses the servlet thread pool; asynchronous work (scheduling, Kafka consumers, batch/index rebuild via `IndexRebuildScheduler`, `SchedulerServiceImpl`) uses separate executors. Java 21 enables virtual-thread adoption for I/O-bound handlers if needed. The primary risk is thread-pool saturation on the servlet pool during downstream (provider/DB) stalls; provider failover and timeouts mitigate this.

**Assessment:** Sound. Consider enabling virtual threads for I/O-bound endpoints if load testing shows servlet-pool contention.

### 2.9 Startup time (modular monolith)
A single Spring context initializes all modules, Flyway migrations, JPA, Redis, and Kafka clients at boot. Startup is therefore heavier than a thin microservice but incurred once per deploy. This is an acceptable trade for in-process module calls and operational simplicity. Lazy initialization and Spring AOT/native compilation are optional accelerators if cold-start becomes a constraint.

**Assessment:** Acceptable for a monolith. Not a production concern for long-running deployments.

---

## 3. Metrics Table — Current-State (Estimated) vs. Recommended

All current-state figures are **architecture-derived estimates**, not measured. Recommended values are load-test acceptance targets for Phase 5.

| Dimension | Current-state (estimated) | Recommended target | Status |
|---|---|---|---|
| API latency — cache hit (read) | ~5–20 ms (p95) | < 50 ms (p95) | On track |
| API latency — cache miss / write | ~30–120 ms (p95) | < 200 ms (p95) | On track |
| Database query (indexed lookup) | ~1–10 ms | < 25 ms | On track |
| Redis hit ratio — reference/config data | ~85–95% | > 85% | On track |
| Redis hit ratio — semantic/user data | ~40–60% | > 50% (with embedding cache) | Monitor |
| Kafka throughput per topic | 3 partitions (3-way parallel) | Scale partitions as volume grows | Adequate |
| AI response latency | Provider-bound + fallback overhead | Provider-bound; fallback < 1 added hop | Acceptable |
| Governance pipeline (9 stages) | ~20–80 ms in-process (cache-assisted) | < 150 ms (p95) | Acceptable (by design) |
| Memory (heap) | Single shared JVM; Redis off-heap | Bounded batch sizes; no OOM under load | On track |
| Thread utilization | Servlet pool + async executors | No servlet-pool saturation under load | On track |
| Startup time | Full context init (one-time) | < 60 s cold start | Acceptable |

---

## 4. Optimization Recommendations

Most recommendations are **optional** and become relevant only if load testing reveals pressure. None are required for the gate.

| ID | Priority | Recommendation | Trigger |
|---|---|---|---|
| P-01 | Required before prod sign-off | Execute the in-suite performance tests plus a staging load test to replace estimates with measured p50/p95/p99 figures. | Gate exit / production readiness |
| P-02 | Optional | Add/verify composite indexes for analytics and registry filter/sort queries. | If DB query p95 exceeds target at volume |
| P-03 | Optional | Increase Kafka partition count for high-volume topics. | If consumer lag grows |
| P-04 | Optional | Enable Java 21 virtual threads for I/O-bound REST handlers. | If servlet-pool contention appears |
| P-05 | Optional | Bound embedding batch sizes and monitor semantic-module heap. | If heap pressure appears under batch load |
| P-06 | Optional | Apply Spring AOT/lazy init if cold-start time becomes constraining. | If deploy cadence demands fast cold start |

---

## 5. Step 12 Gate Verdict

**PASS.**

The platform's performance architecture is sound and appropriate for its goals. The cache-first REST design, namespaced Redis TTLs, indexed Flyway-managed schema, partitioned Kafka topics, resilient provider abstraction with fallback, and in-process modular-monolith topology together yield a system with low read-path latency, controlled write-path cost, and no structural performance red flags. The 9-stage governance pipeline adds latency by design; this cost is necessary, in-process, and cache-mitigated, and is therefore accepted.

The only mandatory follow-up is **P-01**: run the in-suite performance tests and a staging load test (once Maven/CI is available) to convert the architecture-derived estimates in Section 3 into measured figures before production sign-off. All other optimizations are optional and load-triggered.

---

*End of Performance Review (Gate Step 12).*
