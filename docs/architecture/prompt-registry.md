# Prompt Version Registry

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Owner:** Enterprise AI Platform Engineering Team
**Module:** `prompt-registry`

---

## Purpose

The Prompt Version Registry is the authoritative, versioned catalog of every prompt template used by the platform. It guarantees that no AI interaction uses a hardcoded or untracked prompt: every prompt is a managed, versioned, reviewable, and rollback-able asset.

It extends the Prompt Management Platform (Sprint 17 Part 4) by adding registry-level discovery, comparison, and lifecycle governance on top of the existing template/version engine.

---

## Versioning

- Every prompt template has one or more immutable **versions**.
- A version is identified by `templateId` + `versionNumber` (monotonic integer) plus a `versionId` UUID.
- Published versions are **immutable** — content cannot change after `PUBLISHED`.
- A new edit creates a new version; the prior version is retained for history and rollback.
- `PromptVersionService` manages the version lifecycle; `PromptVersionManager` (in Decision/Automation modules) is not used here — versioning is owned by the Prompt domain.

---

## Metadata

Each prompt/version carries:

- `templateId`, `versionId`
- `name`, `description`
- `category` — one of the 13 seeded categories (Customer Support, Grower Assistant, Training, Product Recommendations, Marketplace, SEO, Marketing, Content Generation, Analytics, ERP, Internal Assistant, System Prompt, Developer Prompt)
- `language`
- `variables` — declared `{{variable}}` placeholders with `VariableType`
- `renderingNotes`
- `audit` — 18 tracked audit action types in `ai_prompt_audit`

---

## Owner

- `owner` — team or service accountable for the prompt.
- `lastModifiedBy` — actor who created/edited the version.
- Ownership is enforced at the service layer for edit/approve/deprecate operations (RBAC: ADMINISTRATOR, KNOWLEDGE_MANAGER, CONTENT_EDITOR, USER).

---

## Tags

- Free-form `tags` for search and grouping (e.g., `production`, `experiment`, `compliance`).
- Tags are indexed for the search API.

---

## Status Lifecycle

```
DRAFT → REVIEW → APPROVED → DEPRECATED
              ↘ (reject) → DRAFT
APPROVED → PUBLISHED → DEPRECATED → ARCHIVED
```

- **DRAFT** — editable work-in-progress.
- **REVIEW** — submitted for approval (`PromptLifecycleService.submit`).
- **APPROVED** — passed review, not yet the active published version.
- **PUBLISHED** — the live version used at runtime (only one per template).
- **DEPRECATED** — no longer used for new requests; retained for history.
- **ARCHIVED** — cold storage; excluded from default listings.

Transitions are validated and audit-logged. Rejection returns the prompt to `DRAFT` with a reason.

---

## Rollback

- Rollback creates a **new version** containing the content of a previously published version (no in-place mutation).
- The new version is auto-`APPROVED` and can be `PUBLISHED`, preserving a complete, auditable history.
- This satisfies the immutability rule while allowing safe recovery from a bad publish.

---

## History

- Full version history per template is queryable: content, metadata, actor, timestamp, and transition reason.
- The `ai_prompt_audit` table records every lifecycle and edit action.
- History is append-only and immutable.

---

## Validation

`PromptValidationService` enforces:

- Injection detection — rejects `{{nested}}`, `${}`, `<script>` patterns
- Template length limits and payload size limits
- Regex validation of variable names
- Unresolved variable rejection at render time (safe rendering)

---

## Comparison

- The registry provides a **diff** API comparing two versions of a template:
  - content delta (line/character diff)
  - metadata delta (owner, tags, category changes)
  - variable set delta (added/removed/retyped variables)
- Comparison is read-only and does not alter state.

---

## Search

- Search by name, category, tag, status, owner, language, and free-text.
- Filtering, pagination, and sorting are supported (addresses the prior risk of missing pagination on list endpoints).
- Indexed for low-latency lookup; results cached via the Prompt Redis namespace.

---

## Future A/B Testing Architecture

The registry is designed to support A/B testing of prompts without changing the runtime contract:

- A **Prompt Experiment** entity links two or more `versionId`s to an experiment key, traffic split percentage, and evaluation metric (e.g., completion quality, token cost).
- The AI Gateway resolves the active version via an `ExperimentResolver` keyed on `correlationId` / tenant / user cohort.
- Results flow into Usage Tracking and Governance Analytics for statistical comparison.
- Rollback/winner-promotion reuses the existing version + lifecycle machinery.
- This is a future extension point only; no business functionality is added in this sprint.

---

## Integration Points

- **AI Gateway / Content / Conversation / Assistants** — load prompts exclusively via the Prompt Platform; never hardcode.
- **Provider Registry** — prompts may reference provider-specific hints (capability requirements).
- **Capability Discovery** — prompts declare required capabilities (streaming, tool calling).
- **Event Catalog** — emits `PromptCreated`, `PromptPublished`, `PromptDeprecated`, `PromptRolledBack`, `PromptExecutionStarted`, `PromptExecutionCompleted`.
- **Usage Tracking** — every execution logs the resolved `templateId` + `versionId`.
- **API Registry** — 17 prompt endpoints auto-registered.

---

## Testing

- `PromptVersionServiceTest` — version immutability, rollback
- `PromptLifecycleServiceTest` — status transitions
- `PromptValidationServiceTest` — injection/length/regex
- `PromptSearchServiceTest` — filter/paginate/sort
- `PromptControllerTest` — all 17 endpoints + RFC 9457 errors
- Architecture tests — prompt-registry boundary isolation
