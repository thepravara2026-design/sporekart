# SporeKart Observability Architecture

## Overview

The SporeKart Observability Platform implements the four pillars of observability — Metrics, Logs, Distributed Traces, and Health Signals — across all platform subsystems. Every request, event, workflow, AI execution, and infrastructure component is fully observable.

## Architecture

### Four Pillars

| Pillar | Tool | Collection | Storage | Visualization |
|--------|------|------------|---------|---------------|
| Metrics | Micrometer + Prometheus | /actuator/prometheus | Prometheus TSDB | Grafana |
| Logs | Logback + Logstash | JSON via appender | Elasticsearch | Kibana/Grafana |
| Traces | OpenTelemetry + Micrometer | OTLP exporter | Jaeger/Tempo | Grafana |
| Health | Spring Boot Actuator | /actuator/health | In-memory | Grafana |

### Data Correlation

All telemetry shares common correlation IDs:
- **Trace ID**: End-to-end request tracking across services
- **Span ID**: Individual operation within a trace
- **Correlation ID**: Business transaction correlation
- **Request ID**: Unique request identifier

## Metrics Architecture

### Metric Types Collected
- **Counters**: Request counts, error counts, business events
- **Timers**: Latency distributions with percentiles
- **Gauges**: Current resource utilization
- **Distribution Summaries**: Token counts, order values, prompt sizes

### Metric Namespaces
- `sporekart.business.*` — Business KPIs
- `sporekart.ai.*` — AI platform metrics
- `sporekart.events.*` — Event backbone metrics
- `sporekart.security.*` — Security observability
- `sporekart.infrastructure.*` — Infrastructure health
- `jvm.*` — JVM runtime metrics
- `hikaricp.*` — Connection pool metrics
- `http.server.requests.*` — HTTP metrics

### Export Configuration
- Prometheus scrape: 15s interval
- Percentiles: P50, P95, P99
- Histogram buckets: 10ms, 50ms, 100ms, 200ms, 500ms, 1s, 2s, 5s
- SLO tracking: 10ms, 50ms, 100ms, 200ms, 500ms, 1s, 2s

## Logging Architecture

### Structured JSON Logging
Every log entry contains:
- `@timestamp`, `level`, `logger`, `message`, `thread`, `service`
- `traceId`, `spanId`, `correlationId`, `requestId`
- `userId`, `workspaceId`, `method`, `path`, `status`, `duration`
- `environment`, `node`, `version`, `event_type`

### Log Levels by Environment
| Environment | Root Level | App Level | Console | File |
|-------------|-----------|-----------|---------|------|
| Development | DEBUG | DEBUG | Yes | No |
| Staging | INFO | DEBUG | Yes | Yes |
| Production | WARN | INFO | Yes | Yes |

### Async Logging
- Console queue: 2048 entries
- File queue: 4096 entries
- Discard policy: 0 (never discard)
- Max file size: 500MB
- Retention: 30 days
- Total size cap: 10GB

## Distributed Tracing

### Trace Context Propagation
- Incoming: X-Trace-Id, X-Span-Id, X-Correlation-Id, X-Request-Id headers
- Internal: ThreadLocal TraceContext
- Outgoing: Propagated via HTTP headers
- Sampling: 10% head-based, configurable

### Trace Coverage
- All HTTP requests from gateway
- All database operations
- All AI service calls
- All event publications
- All async task executions

## Health Platform

### Probe Endpoints
| Endpoint | Purpose | Frequency |
|----------|---------|-----------|
| /actuator/health | Overall health | 10s |
| /actuator/health/liveness | Is container alive? | 10s |
| /actuator/health/readiness | Can container serve traffic? | 15s |
| /actuator/health/startup | Has startup completed? | Once |

### Dependency Checks
- Database (PostgreSQL)
- Cache (Redis)
- Event backbone (Kafka)
- AI providers
- Storage
- Upstream services

## Alerting Architecture

### Alert Severity
| Severity | Response Time | Channel | Resolve Time |
|----------|--------------|---------|--------------|
| CRITICAL | 15 min | PagerDuty + Slack + Phone | 4 hours |
| WARNING | 30 min | Slack | 8 hours |
| INFO | 2 hours | Slack | 24 hours |

### Alert Routing
- **critical**: PagerDuty + #sporekart-sev1
- **warning**: #sporekart-warnings
- **info**: #sporekart-info
- **ai**: #sporekart-ai
- **events**: #sporekart-events
- **security**: #sporekart-security

## Dashboards

| Dashboard | Audience | Panels | Purpose |
|-----------|----------|--------|---------|
| Executive | Leadership | 14 | Platform health, revenue, SLOs |
| Business | Product | 14 | Business KPIs, orders, payments |
| Engineering | Developers | 14 | Service perf, errors, resources |
| SRE | Operations | 14 | SLOs, incidents, alerting |
| Infrastructure | Platform | 14 | Cluster, nodes, networking |
| AI Platform | AI Team | 15 | AI perf, providers, costs |
| Marketplace | Marketplace | 8 | Plugin, copilot, revenue |
| Security | Security | 14 | Auth, abuse, violations |
| Database | DBA | 14 | Queries, connections, indexes |
| Deployment | DevOps | 14 | Pipelines, versions, rollbacks |

## Related Documents
- [Metrics Catalog](MetricsCatalog.md)
- [Logging Standards](LoggingStandards.md)
- [Tracing Guide](TracingGuide.md)
- [Dashboard Guide](DashboardGuide.md)
- [Alert Policy](AlertPolicy.md)
- [SLO-SLI](SLO-SLI.md)
- [Incident Response](IncidentResponse.md)
