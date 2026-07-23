# Release Notes — SporeKart Enterprise AI Platform RC-3

**Version:** 3.0 RC-3  
**Release Date:** 23-Jul-2026  
**Previous Release:** RC-2 (20-Jul-2026)  
**Classification:** Release Candidate — Enterprise Evaluation

---

## Executive Summary

RC-3 is a major capability release for the SporeKart Enterprise AI Platform, introducing the Marketplace & Plugin SDK ecosystem, full Multi-Copilot operation across 9 enterprise copilots, and a comprehensively certified AI Platform. This release transforms the platform from a single-copilot architecture to a multi-agent, extensible enterprise AI platform.

---

## Version Details

| Component | Version |
|-----------|---------|
| Platform Version | 3.0 RC-3 |
| API Version | v3 |
| Schema Version | 21 |
| SDK Version (Plugin SDK) | 2.1.0 |
| Core Copilot Engine | 1.3.0 |
| AI Gateway | 1.2.0 |
| Release Branch | `rc-3-marketplace` |

---

## Scope

RC-3 encompasses three major capability domains:

1. **Marketplace & Plugin SDK** — Extensibility platform for third-party plugin development, installation, and lifecycle management
2. **Multi-Copilot Operation** — 9 specialized enterprise copilots with intelligent routing, handoff, and shared context
3. **AI Platform Certification** — Production-grade AI infrastructure with gateway, prompts, RAG, streaming, conversation, memory, knowledge, and failover

---

## New Capabilities

### Marketplace & Plugin SDK

| Capability | Description |
|------------|-------------|
| Plugin Lifecycle Management | Install, upgrade, downgrade, remove, reload with dependency resolution |
| Capability Registry | Centralized registry for plugin capabilities with discovery and conflict detection |
| Version Compatibility | Semantic versioning with forward/backward compatibility matrix |
| Sandbox Isolation | Process, filesystem, network, and memory isolation for plugins |
| Permission Enforcement | Granular permission model with audit trail |
| Plugin Health Monitoring | Heartbeat, resource leak detection, deadlock detection |
| Plugin Recovery | Auto-restart, state checkpointing, circuit breaker |
| SDK TypeScript API | Fully typed SDK with 48 methods, comprehensive error types |
| Plugin Store | Registry with install from URL or store |

### Multi-Copilot Operation

| Capability | Description |
|------------|-------------|
| Customer Copilot | Product discovery, orders, support |
| Admin Copilot | User management, system config, audits |
| Trainer Copilot | Content authoring, course management |
| Grower Copilot | Inventory, cultivation, harvest planning |
| BI Copilot | Analytics, dashboards, reporting |
| Marketing Copilot | Campaigns, promotions, segments |
| Operations Copilot | Fulfillment, logistics, supply chain |
| Executive Copilot | Strategic insights, KPIs, forecasts |
| Developer Copilot (Core) | API docs, SDK, integration support |
| Automatic Intent Routing | ML-based routing to correct copilot |
| Copilot Handoff | Context-preserving handoff between copilots |
| Shared Memory | Cross-copilot memory with expiry and conflict resolution |
| Shared Context | User, business entity, and conversation context |
| Conversation Continuity | Cross-copilot threads, session resume, branching |

### AI Platform

| Capability | Description |
|------------|-------------|
| AI Gateway | Request routing, rate limiting, caching, audit logging |
| Prompt Platform | Template rendering, versioning, A/B testing, safety filters |
| Enterprise RAG | Vector + hybrid search, multi-tenant, source citations |
| Streaming Engine | SSE token streaming, backpressure, error recovery |
| Conversation Engine | Multi-turn, state persistence, context management, branching |
| Memory Engine | Short-term, long-term, episodic, semantic memory |
| Knowledge Platform | Knowledge graph, entity resolution, relationship traversal |
| Provider Failover | Automatic failover across GPT-4o, Claude, Gemini |

---

## Improvements from RC-2

| Area | RC-2 | RC-3 | Improvement |
|------|------|------|-------------|
| Architecture | Single-copilot | Multi-copilot mesh (9 copilots) | Major |
| Extensibility | None | Marketplace + Plugin SDK | New |
| AI Platform | Partially certified | Full certification (8 components) | Major |
| Regression Pass Rate | 96.8% | 99.2% | +2.4% |
| Security Score | 80/100 | 88/100 | +8 |
| Performance Score | 90/100 | 94/100 | +4 |
| Release Confidence | 85/100 | 92/100 | +7 |
| Test Coverage | 847 tests | 1,247 tests | +400 tests |
| Open Defects (Medium+) | 4 | 1 | -3 |

---

## Bug Fixes

| ID | Description | Severity | Fixed In |
|----|-------------|----------|----------|
| RC3-BF-001 | Plugin hot-reload not re-registering capabilities after config change | MEDIUM | RC-3 |
| RC3-BF-002 | Copilot handoff losing context for >5 conversation turns | HIGH | RC-3 |
| RC3-BF-003 | AI Gateway rate limiter not resetting on window boundary | MEDIUM | RC-3 |
| RC3-BF-004 | RAG hybrid search returning duplicate results on page 2+ | MEDIUM | RC-3 |
| RC3-BF-005 | Plugin sandbox CPU quota not enforced for background threads | HIGH | RC-3 |
| RC3-BF-006 | Workspace isolation bypass via shared capability registry | CRITICAL | RC-3 |
| RC3-BF-007 | Memory engine consolidation race condition on concurrent writes | MEDIUM | RC-3 |
| RC3-BF-008 | Streaming engine connection leak on client disconnect | MEDIUM | RC-3 |
| RC3-BF-009 | Prompt template injection via user-controlled variable names | HIGH | RC-3 |
| RC3-BF-010 | Marketplace plugin dependency resolution not handling nested deps | MEDIUM | RC-3 |

---

## Known Issues

| ID | Issue | Severity | Status |
|----|-------|----------|--------|
| KNOWN-01 | Image upload >5MB returns 500 instead of user-friendly error | MEDIUM | Scheduled for RC-4 |
| KNOWN-02 | Secondary provider failover P95 latency 582ms (SLA 500ms) | LOW | Under investigation |
| KNOWN-03 | Long-term memory TTL validation requires 30-day window | LOW | Verified through unit tests |
| KNOWN-04 | Full permission matrix escalation test harness incomplete | LOW | Coverage via unit tests |
| KNOWN-05 | SDK docstring coverage 98.7% (3 internal methods) | LOW | Accepted |

---

## Upgrade Notes

### From RC-2

1. **Database Migration**: Run `infrastructure/database/migrations/002_marketplace_schema.sql` to create Marketplace and Plugin SDK tables
2. **Environment Variables**: Add new required env vars:
   - `VITE_MARKETPLACE_API_URL`
   - `VITE_PLUGIN_SANDBOX_MEMORY_LIMIT`
   - `VITE_COPILOT_ROUTING_ENDPOINT`
3. **Docker Compose**: New services added: `copilot-routing`, `marketplace-service`, `plugin-sandbox`
4. **API Changes**: Copilot routing endpoint moved from `/api/copilot` to `/api/routing`
5. **Plugin SDK**: Plugin developers must target SDK v2.1.0; v1.x plugins continue to work with deprecation warnings

### Breaking Changes

- Copilot routing API path changed: `/api/copilot/routing` → `/api/routing`
- Plugin SDK v1.0.0: Deprecated; will be removed in GA release
- AI Gateway cache key format changed; existing caches invalidated on upgrade

### Deprecations

- Plugin SDK v1.0.0 API (replaced by v2.0.0+)
- Single-copilot mode (replaced by multi-copilot routing)
- Legacy prompt templates (v1 format) — auto-migrated on first use

---

## Deployment Artifacts

| Artifact | Location |
|----------|----------|
| Docker Image | `sporekart/web-app:rc-3` |
| Docker Compose | `docker-compose.yml` |
| Infrastructure | `infrastructure/terraform/` |
| Database Migration | `infrastructure/database/migrations/002_marketplace_schema.sql` |
| Release Audit | `RC3_RELEASE_AUDIT/` |
| Closure Reports | `PRODUCTION_READINESS_CLOSURE/C09-C11/` |

---

## Support

| Channel | Details |
|---------|---------|
| Documentation | `docs/` |
| Runbook | `docs/runbook.md` |
| Monitoring | Grafana: `https://grafana.sporekart.com` |
| Error Tracking | Sentry: configured per environment |
| Incident Response | Email: `ops@sporekart.com` |

---

**Prepared by:** Enterprise Release Governance Board  
**Date:** 23-Jul-2026  
**Certificate ID:** SPK-RC3-20260723-001
