# Knowledge Source Registry

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Owner:** Enterprise AI Platform Engineering Team
**Module:** `knowledge-registry`

---

## Purpose

The Knowledge Source Registry is the authoritative catalog of every knowledge source the platform retrieves from. It abstracts *where* knowledge comes from behind a uniform source-type contract, enabling the Knowledge Platform (Sprint 17 Part 5) and Semantic Platform to ingest, refresh, and query without coupling to any single storage backend.

---

## Source Types

| Source Type | Description | Status |
|-------------|-------------|--------|
| `DATABASE` | Internal relational/NoSQL stores (PostgreSQL/Supabase) | Supported |
| `PDF` | Uploaded PDF documents, chunked on ingest | Supported |
| `MARKDOWN` | Markdown/text documents | Supported |
| `WEBSITE` | Crawled web pages / sitemaps | Supported |
| `FAQ` | Structured Q&A pairs | Supported |
| `TRAINING` | Curated training/enablement material | Supported |
| `CMS` | Content management system exports | Supported |
| `SHAREPOINT` | Microsoft SharePoint | Future |
| `GOOGLE_DRIVE` | Google Drive | Future |
| `CONFLUENCE` | Atlassian Confluence | Future |
| `NOTION` | Notion workspace | Future |

Future source types are reserved in the `KnowledgeSourceType` enum as `UNSUPPORTED`; the registry rejects registration of unsupported types until their adapters land (no business functionality added in this sprint).

---

## Metadata

Each knowledge source entry carries:

- `sourceId` — UUID
- `sourceType` — enum (above)
- `name`, `description`
- `connectionRef` — opaque reference to credentials/connection (masked)
- `visibility` — `PUBLIC`, `INTERNAL`, `RESTRICTED`, `CONFIDENTIAL`
- `category` — one of the 15 seeded knowledge categories
- `language`
- `tags`
- `createdAt` / `updatedAt` / `lastSyncedAt`

---

## Owner

- `owner` — team/service accountable for the source.
- `lastModifiedBy` — actor who registered or modified the source.
- Visibility and ownership are enforced at the service layer (Knowledge RBAC: ADMINISTRATOR, KNOWLEDGE_MANAGER, CONTENT_EDITOR, USER).

---

## Version

- Knowledge sources are versioned at the document level (`knowledge_document_versions`) in the Knowledge Platform.
- The registry tracks a `sourceVersion` (semantic) representing the schema/contract of the source integration, distinct from document versions.
- Version changes are recorded in history and emitted to the Event Catalog.

---

## Refresh Policy

Each source declares a refresh policy:

- `refreshStrategy` — `MANUAL`, `SCHEDULED`, `EVENT_DRIVEN`, `WEBHOOK`
- `schedule` — cron / frequency (`ONCE`, `HOURLY`, `DAILY`, `WEEKLY`, `MONTHLY`, `CRON`)
- `chunkSize` / `chunkOverlap` — defaults 1000 / 100 chars, sentence-boundary aware
- `incremental` — boolean for delta sync vs full re-sync

Refresh is orchestrated by the Automation & Lifecycle Platform where scheduling is required.

---

## Health

- `healthStatus` — `HEALTHY`, `DEGRADED`, `UNHEALTHY`, `UNKNOWN`
- `lastHealthCheckAt`, `message`, `latencyMs`
- Health probes verify connectivity and auth to the source without transferring full content.
- Unhealthy sources are flagged in the registry and excluded from retrieval until recovered.

---

## Sync Status

- `syncStatus` — `PENDING`, `IN_PROGRESS`, `COMPLETED`, `FAILED`, `STALE`
- `lastSyncedAt`, `nextScheduledSyncAt`, `lastSyncError`
- `documentCount`, `chunkCount` — current ingested volume
- A `STALE` status triggers a refresh recommendation surfaced via the discovery API.

---

## Integration Points

- **Knowledge Platform** — ingestion, chunking, retrieval, citation.
- **Semantic Platform** — embedding generation and vector indexing of chunks.
- **Provider Registry / Capability Discovery** — embedding providers required for sync.
- **Event Catalog** — emits `SourceRegistered`, `SourceSynced`, `SourceHealthChanged`, `SourceDeprecated`.
- **Usage Tracking** — logs retrieval counts per source.
- **API Registry** — knowledge endpoints auto-registered.

---

## Testing

- `KnowledgeRegistryServiceTest` — registration, type validation, status filtering
- `KnowledgeHealthServiceTest` — health evaluation
- `KnowledgeSyncServiceTest` — sync status transitions
- `KnowledgeControllerTest` — discovery endpoints + RFC 9457 errors
- Architecture tests — knowledge-registry boundary isolation
