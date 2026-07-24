# SporeKart Dashboard Guide

## Overview

SporeKart provides 10 pre-configured Grafana dashboards covering executive, business, engineering, SRE, and domain-specific metrics. This guide documents each dashboard, its data sources, and usage instructions.

## Dashboard Catalog

### 1. Executive Dashboard (sporekart-exec)
**Audience**: CTO, VP Engineering, Leadership
**Purpose**: High-level platform health, revenue, SLO compliance
**Refresh**: 5 minutes
**Panels**: 14

Key Metrics:
- Platform availability (30d rolling)
- Error budget remaining
- Monthly revenue
- Active users
- Orders today
- AI requests today
- Open incidents
- SLO compliance
- Revenue/orders/AI trends (7d)
- Error budget burn rate
- P95 latency
- Container restarts

### 2. Business Dashboard (sporekart-business)
**Audience**: Product Managers, Business Analysts
**Purpose**: Business KPIs and operational metrics
**Refresh**: 1 minute
**Panels**: 14

Key Metrics:
- Orders created/completed/cancelled
- Revenue (real-time)
- Order processing time (avg/p95)
- Payments processed/failed
- Notifications sent
- Product views
- Training registrations, certificates
- Plugin and copilot usage
- Inventory updates, coupons applied
- Active users over time

### 3. Engineering Dashboard (sporekart-eng)
**Audience**: Software Engineers, Tech Leads
**Purpose**: Service performance, errors, resource allocation
**Refresh**: 30 seconds
**Panels**: 14

Key Metrics:
- Request rate by service
- Error rate by service
- P95/P99 latency by service
- CPU by service
- Memory by service
- GC pause by service
- Thread count by service
- DB connection pool
- Cache hit ratio
- Deployment frequency
- Change failure rate

### 4. SRE Dashboard (sporekart-sre)
**Audience**: Site Reliability Engineers
**Purpose**: SLO monitoring, incident management, alerting
**Refresh**: 15 seconds
**Panels**: 12

Key Metrics:
- SLO compliance (7d)
- Error budget remaining
- Burn rate (1h)
- Active incidents table
- Alert history (24h)
- Uptime (30d)
- MTTR, MTBF
- Health check status
- Container restarts
- Pod status
- On-call rotation info

### 5. Infrastructure Dashboard (sporekart-infra)
**Audience**: Platform Engineers, DevOps
**Purpose**: K8s cluster health, nodes, networking
**Refresh**: 30 seconds
**Panels**: 14

Key Metrics:
- Cluster CPU/memory/network usage
- Node status
- Pod distribution by node
- Container restarts
- Disk usage
- Service endpoints
- Certificate expiry
- Ingress traffic and latency

### 6. AI Platform Dashboard (sporekart-ai)
**Audience**: AI Engineers, ML Team
**Purpose**: AI service performance, provider metrics, costs
**Refresh**: 30 seconds
**Panels**: 15

Key Metrics:
- AI request/completion/failure rate
- AI latency (P50/P95/P99)
- Provider latency and failure rate
- Fallback count
- Prompt build, knowledge retrieval, memory lookup, vector search time
- Token count, conversation length
- AI cost
- Prompt injection rate
- Provider distribution
- AI SLO compliance

### 7. Marketplace Dashboard (sporekart-marketplace)
**Audience**: Marketplace Team, Plugin Developers
**Purpose**: Plugin performance, copilot usage, revenue
**Refresh**: 1 minute
**Panels**: 8

Key Metrics:
- Plugin request/error rate
- Plugin latency
- Top plugins by usage
- Plugin violations
- Copilot queries
- Plugin health
- Marketplace revenue

### 8. Security Dashboard (sporekart-security)
**Audience**: Security Team, Compliance
**Purpose**: Security events, abuse detection, threat monitoring
**Refresh**: 30 seconds
**Panels**: 14

Key Metrics:
- Failed logins
- Permission denied
- JWT errors
- Rate limit triggers
- Suspicious activity
- Unauthorized access
- AI security events (prompt injections, abuse)
- Secret access events
- Plugin violations
- Failed logins by user
- Blocked IPs
- Security score

### 9. Database Dashboard (sporekart-db)
**Audience**: DBAs, Backend Engineers
**Purpose**: Database performance, queries, connections, indexes
**Refresh**: 30 seconds
**Panels**: 14

Key Metrics:
- Active/idle connections
- Queries per second
- Cache hit ratio
- Slow queries
- Dead tuples
- Index usage
- Transaction rate (commits/rollbacks)
- Read/write ratio
- Sequential scans
- Connection pool utilization
- Table and index sizes
- Lock contention

### 10. Deployment Dashboard (sporekart-deploy)
**Audience**: DevOps, Release Managers
**Purpose**: Deployment pipeline, rollbacks, release cadence
**Refresh**: 1 minute
**Panels**: 14

Key Metrics:
- Deployment frequency by service
- Deployment duration
- Deployment success rate
- Current versions
- Rollback events
- Time to deploy
- Post-deploy error rate
- Pipeline status
- Changes by author
- Deploy by environment
- GitHub Actions queue
- Release cadence
- MTTR by deploy

## Access Control

| Dashboard | Viewers | Editors |
|-----------|---------|---------|
| Executive | Leadership | Platform Team |
| Business | All | Product Team |
| Engineering | All Engineers | Engineering Lead |
| SRE | All Engineers | SRE Team |
| Infrastructure | Platform Team | Platform Team |
| AI | AI Team | AI Team |
| Marketplace | Marketplace Team | Marketplace Team |
| Security | Security Team | Security Lead |
| Database | All Engineers | DBA Team |
| Deployment | All Engineers | DevOps Team |

## Data Sources

| Dashboard | Primary Data Source |
|-----------|-------------------|
| All | Prometheus (sporekart-services) |
| Infrastructure | Prometheus (kube-state-metrics, node-exporter) |
| Database | Prometheus (postgres-exporter) |
| AI | Micrometer custom metrics (AIMetrics) |
| Security | Micrometer custom metrics (SecurityMetrics) |
| Business | Micrometer custom metrics (BusinessMetrics) |
| Events | Micrometer custom metrics (EventMetrics) |
| Deployments | GitHub API + Prometheus |

## Creating Custom Dashboards

1. Duplicate an existing dashboard as a starting point
2. Use PromQL queries from the Metrics Catalog
3. Follow naming convention: `SporeKart {Team} {Purpose}`
4. Set appropriate refresh interval for the data type
5. Add documentation panel with purpose and contact info
6. Test with production data before publishing
7. Request review from Platform Team

## Dashboard Maintenance

- Review quarterly for stale panels
- Update when new metrics are added
- Archive unused dashboards
- Document changes in changelog
- Test alert integration monthly
