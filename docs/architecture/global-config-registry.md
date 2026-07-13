# Global Configuration Registry

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Owner:** Enterprise AI Platform Engineering Team
**Module:** `global-config-registry`

---

## Purpose

The Global Configuration Registry is the single, centralized, versioned, and validated source of all platform configuration. It consolidates settings that were previously scattered across `application.yml`, module configs, and feature-flag properties into one introspectable, auditable registry — extending the Governance Administration & Control Plane (Sprint 18 Part 8).

---

## Centralized Config

Configuration is grouped by domain:

| Group | Scope |
|-------|-------|
| `platform` | Core platform settings, module enablement, environment profiles |
| `ai` | Provider defaults, model selection, timeout/retry strategies |
| `governance` | Policy, decision, approval, compliance, risk, trust defaults |
| `security` | RBAC roles, auth, encryption, rate limits |
| `workflow` | Automation, lifecycle, scheduling defaults |
| `provider` | Per-provider configuration and priority |
| `search` | Semantic/search/vector configuration |
| `notification` | Notification channels and templates |

Each entry is addressed by `key` + `module` + `environment` (matching the Administration Platform's central configuration model) and supports `GLOBAL`, `MODULE`, `ENVIRONMENT`, `PROVIDER`, `TENANT`, `USER` scopes.

---

## Versioning

- Every configuration change creates a new `ConfigurationVersion` (monotonic per key+module+environment).
- Versions are immutable; rollback creates a new version with the prior content.
- `ConfigurationVersionManager` tracks the full history.
- Version metadata includes actor, timestamp, and change reason.

---

## Validation

`ConfigurationValidationService` enforces:

- Schema/type validation per config key
- Allowed-value / range constraints
- Environment-specific rules (e.g., production stricter than development)
- Referential integrity (e.g., a referenced provider must exist in the Provider Registry)
- Import/export supports **dry-run validation** before applying

Invalid configuration is rejected with RFC 9457 and a stable error code; no partial apply.

---

## Snapshots

- A **configuration snapshot** captures the full effective configuration for a module/environment at a point in time.
- Snapshots are taken automatically before bulk changes and on demand.
- `ConfigurationSnapshotService` supports create, list, compare, and **restore**.
- Restore applies the snapshot as a new set of versions (auditable, non-destructive).

---

## Rollback

- Rollback targets either a single key version or a full snapshot.
- Both reuse the versioning machinery — no in-place mutation.
- Rollback is audit-logged and emits an event to the Event Catalog.
- The Administration Platform's maintenance-mode integration can gate rollback during change windows.

---

## Integration Points

- **Governance Administration & Control Plane** — owns the persistence and UI; the registry provides the discovery/validation contract on top.
- **All modules** — read effective config via the registry rather than raw `application.yml`.
- **Feature Flags** — global/environment/module-scoped flags are a first-class config group.
- **Event Catalog** — emits `ConfigurationCreated`, `ConfigurationUpdated`, `ConfigurationDeleted`, `ConfigurationRolledBack`, `SnapshotCreated`, `SnapshotRestored`.
- **API Registry** — admin config endpoints auto-registered under `/api/v1/admin/*`.

---

## Testing

- `ConfigurationVersionManagerTest` — versioning, immutability, rollback
- `ConfigurationValidationServiceTest` — schema/range/referential rules
- `ConfigurationSnapshotServiceTest` — snapshot create/restore/compare
- `GlobalConfigControllerTest` — admin endpoints + RFC 9457 errors
- Architecture tests — global-config-registry boundary isolation
