# SporeKart Metrics Catalog

## Overview

Complete catalog of all metrics collected across the SporeKart platform. Metrics are organized by domain with descriptions, tags, and instrumentation points.

## Business Metrics

| Metric Name | Type | Tags | Description |
|-------------|------|------|-------------|
| sporekart.business.orders.created | Counter | - | Total orders created |
| sporekart.business.orders.completed | Counter | - | Total orders completed |
| sporekart.business.orders.cancelled | Counter | - | Total orders cancelled |
| sporekart.business.payments.processed | Counter | - | Total payments processed |
| sporekart.business.payments.failed | Counter | - | Total payments failed |
| sporekart.business.training.registrations | Counter | - | Training registrations |
| sporekart.business.certificates.issued | Counter | - | Certificates issued |
| sporekart.business.product.views | Counter | - | Product page views |
| sporekart.business.inventory.updates | Counter | - | Inventory updates |
| sporekart.business.coupons.applied | Counter | - | Coupons applied |
| sporekart.business.notifications.sent | Counter | - | Notifications sent |
| sporekart.business.plugin.usage | Counter | plugin | Plugin invocations |
| sporekart.business.copilot.queries | Counter | - | Copilot queries |
| sporekart.business.users.active | Gauge | - | Active users |
| sporekart.business.revenue.total | Gauge | - | Total revenue |
| sporekart.business.order.processing.time | Timer | - | Order processing time |
| sporekart.business.payment.processing.time | Timer | - | Payment processing time |
| sporekart.business.order.value | DistributionSummary | - | Order value |

## AI Platform Metrics

| Metric Name | Type | Tags | Description |
|-------------|------|------|-------------|
| sporekart.ai.requests.total | Counter | provider | Total AI requests |
| sporekart.ai.completions.total | Counter | provider | AI completions |
| sporekart.ai.failures.total | Counter | provider, error | AI failures |
| sporekart.ai.provider.failures | Counter | provider | Provider failures |
| sporekart.ai.fallback.count | Counter | from, to | Fallback count |
| sporekart.ai.security.prompt_injections | Counter | source, pattern | Prompt injections |
| sporekart.ai.prompt.build.time | Timer | - | Prompt build time |
| sporekart.ai.knowledge.retrieval.time | Timer | - | Knowledge retrieval |
| sporekart.ai.memory.lookup.time | Timer | - | Memory lookup time |
| sporekart.ai.vector.search.time | Timer | - | Vector search time |
| sporekart.ai.embedding.generation.time | Timer | - | Embedding generation |
| sporekart.ai.completion.time | Timer | provider | Completion time |
| sporekart.ai.provider.latency | Timer | provider | Provider latency |
| sporekart.ai.tokens.count | DistributionSummary | - | Token count |
| sporekart.ai.prompt.size | DistributionSummary | - | Prompt size |
| sporekart.ai.conversation.length | DistributionSummary | - | Conversation turns |
| sporekart.ai.cost | DistributionSummary | provider | AI cost (cents) |

## Event Platform Metrics

| Metric Name | Type | Tags | Description |
|-------------|------|------|-------------|
| sporekart.events.published.total | Counter | type | Events published |
| sporekart.events.consumed.total | Counter | type | Events consumed |
| sporekart.events.failed.total | Counter | type, reason | Failed events |
| sporekart.events.retry.total | Counter | type | Event retries |
| sporekart.events.dlq.total | Counter | type | DLQ events |
| sporekart.events.replay.total | Counter | type | Replayed events |
| sporekart.events.processing.time | Timer | type | Processing time |
| sporekart.events.queue.length | Gauge | - | Queue length |

## Security Metrics

| Metric Name | Type | Tags | Description |
|-------------|------|------|-------------|
| sporekart.security.auth.failed_logins | Counter | username, ip | Failed logins |
| sporekart.security.auth.permission_denied | Counter | resource, action | Permission denied |
| sporekart.security.auth.jwt_errors | Counter | error_type | JWT errors |
| sporekart.security.secrets.access | Counter | secret | Secret access |
| sporekart.security.ai.prompt_injections | Counter | source, pattern | Prompt injection |
| sporekart.security.ai.abuse_attempts | Counter | pattern | AI abuse |
| sporekart.security.ratelimit.triggers | Counter | endpoint | Rate limit triggers |
| sporekart.security.plugin.violations | Counter | plugin | Plugin violations |
| sporekart.security.unauthorized.access | Counter | ip | Unauthorized access |
| sporekart.security.suspicious.activity | Counter | type, ip | Suspicious activity |

## Infrastructure Metrics

| Metric Name | Type | Tags | Description |
|-------------|------|------|-------------|
| sporekart.infrastructure.container.restarts | Counter | service | Container restarts |
| sporekart.infrastructure.service.down | Counter | service | Service down events |
| sporekart.infrastructure.database.failures | Counter | type | DB failures |
| sporekart.infrastructure.cache.failures | Counter | cache | Cache failures |
| sporekart.infrastructure.network.errors | Counter | type | Network errors |
| sporekart.infrastructure.connections.active | Gauge | - | Active connections |
| sporekart.infrastructure.files.open | Gauge | - | Open file handles |
| sporekart.infrastructure.threads.total | Gauge | - | Thread count |

## JVM Metrics (Automatic)

| Metric | Description |
|--------|-------------|
| jvm.memory.used | Heap + non-heap memory used |
| jvm.memory.max | Maximum memory available |
| jvm.memory.committed | Committed memory |
| jvm.gc.pause | GC pause duration |
| jvm.gc.memory.allocated | Memory allocated between GCs |
| jvm.threads.live | Live thread count |
| jvm.threads.peak | Peak thread count |
| jvm.classes.loaded | Loaded classes |
| jvm.buffer.memory.used | Direct buffer memory |

## HikariCP Metrics (Automatic)

| Metric | Description |
|--------|-------------|
| hikaricp.connections.active | Active connections |
| hikaricp.connections.idle | Idle connections |
| hikaricp.connections.pending | Pending connection requests |
| hikaricp.connections.max | Maximum pool size |
| hikaricp.connections.min | Minimum pool size |
| hikaricp.connections.timeout | Connection timeout count |
| hikaricp.connections.creation | Connection creation time |

## HTTP Metrics (Automatic)

| Metric | Description |
|--------|-------------|
| http.server.requests | Request count, duration, status |
| http.server.requests.active | Active request count |
| http.client.requests | Outbound request metrics |

## Instrumentation Points

### Java Services
All services automatically collect:
- JVM metrics via Micrometer
- HikariCP metrics via data source
- HTTP metrics via Spring Boot Actuator
- Custom metrics via MetricsCollectors

### Business Metrics
Instrumented in: BusinessMetrics.java
Trigger points: Service layer methods

### AI Metrics
Instrumented in: AIMetrics.java
Trigger points: AI service, provider calls

### Event Metrics
Instrumented in: EventMetrics.java
Trigger points: Publish, consume, retry, DLQ

### Security Metrics
Instrumented in: SecurityMetrics.java
Trigger points: Auth filter, rate limiter, AI security

### Infrastructure Metrics
Instrumented in: InfrastructureMetrics.java
Trigger points: Health checks, K8s events
