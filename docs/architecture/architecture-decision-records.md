# Architecture Decision Records (ADR) Repository

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Owner:** Enterprise AI Platform Engineering Team
**Location:** `docs/adr/`

---

## What Are ADRs?

Architecture Decision Records (ADRs) are short, focused documents that capture an important architectural decision made on the SporeKart Enterprise AI Platform: the **problem** it addresses, the **decision** taken, the **alternatives** considered, the **trade-offs**, the **consequences**, and the current **status**.

ADRs are immutable once accepted — a reversed decision is recorded by a *new* ADR that supersedes the old one (status changed to `SUPERSEDED`), never by editing the original. This preserves the historical reasoning behind the system.

---

## ADR Structure

Every ADR follows the same template:

- **Problem** — the forces, context, and issue that required a decision.
- **Decision** — the chosen approach, stated in imperative/declarative form.
- **Alternatives** — other options considered, with brief rationale for rejection.
- **Trade-offs** — what we gain and what we give up.
- **Consequences** — the impact on code, operations, teams, and future decisions.
- **Status** — `PROPOSED`, `ACCEPTED`, `SUPERSEDED`, `DEPRECATED`.

---

## Index (ADR-001 .. ADR-015)

| ID | Title | Status |
|----|-------|--------|
| ADR-001 | Modular Monolith via Spring Modulith as Architectural Baseline | ACCEPTED |
| ADR-002 | Hexagonal Architecture per Module (Package Boundaries) | ACCEPTED |
| ADR-003 | Provider Registry with Priority & Fallback Chain | ACCEPTED |
| ADR-004 | Prompt Version Registry with Immutable Versions & Rollback | ACCEPTED |
| ADR-005 | Knowledge Source Registry with Source-Type Abstraction | ACCEPTED |
| ADR-006 | Global Configuration Registry — Centralized, Versioned, Validated | ACCEPTED |
| ADR-007 | AI Usage & Cost Foundation — Tracking Only, No Billing | ACCEPTED |
| ADR-008 | Event Catalog as Single Source of Truth for Async Contracts | ACCEPTED |
| ADR-009 | API Registry for Auto-Discovered REST Surface | ACCEPTED |
| ADR-010 | AI Capability Discovery for Runtime Feature Negotiation | ACCEPTED |
| ADR-011 | Documentation Policy — DDD-First, Docs as Source of Truth | ACCEPTED |
| ADR-012 | No New Business Functionality Policy for Phase 4 Hardening | ACCEPTED |
| ADR-013 | ADRs Mandatory for Cross-Cutting Architecture Decisions | ACCEPTED |
| ADR-014 | Registry Modules Are Read-Optimized, Cache-First Services | ACCEPTED |
| ADR-015 | Final Architecture Review & Maturity Gate Before Release | ACCEPTED |

Each ADR is a file under `docs/adr/` named `adr-NNN-<slug>.md`.

---

## Summary of Decisions

- **ADR-001 — Modular Monolith via Spring Modulith.** Use a single deployable with enforced module boundaries over early microservices. *Trade-off:* less independent scaling; *gain:* simpler ops, strong boundaries, easy future extraction.
- **ADR-002 — Hexagonal Architecture per Module.** Each module uses `api/ application/ domain/ infrastructure/ interfaces/` packages with dependency rules enforced by ArchUnit. *Trade-off:* more boilerplate; *gain:* testability and swapability.
- **ADR-003 — Provider Registry with Priority & Fallback Chain.** Central provider catalog with ordered priority and declarative fallback. *Trade-off:* config complexity; *gain:* zero-downtime provider swaps.
- **ADR-004 — Prompt Version Registry.** Immutable published prompt versions with rollback-as-new-version. *Trade-off:* history growth; *gain:* full auditability and safe recovery.
- **ADR-005 — Knowledge Source Registry.** Uniform source-type contract abstracting DB/PDF/Markdown/Website/FAQ/Training/CMS and future connectors. *Trade-off:* adapter work for each type; *gain:* backend independence.
- **ADR-006 — Global Configuration Registry.** Centralized, versioned, validated config with snapshots/rollback. *Trade-off:* single point to secure; *gain:* consistency and auditability.
- **ADR-007 — Usage & Cost Foundation, No Billing.** Track usage/cost indicators only. *Trade-off:* no charging now; *gain:* clean future billing extension point.
- **ADR-008 — Event Catalog.** Single registry for all async event contracts. *Trade-off:* maintenance overhead; *gain:* discoverability and drift detection.
- **ADR-009 — API Registry.** Auto-discovered REST surface with ownership/auth/health. *Trade-off:* startup scan cost; *gain:* always-current API inventory.
- **ADR-010 — Capability Discovery.** Runtime negotiation of features/providers. *Trade-off:* cached-staleness risk; *gain:* decoupled, config-driven rollout.
- **ADR-011 — DDD-First Documentation Policy.** Docs are source of truth, DDD-structured. *Trade-off:* doc discipline required; *gain:* consistency and onboarding speed.
- **ADR-012 — No New Business Functionality in Phase 4.** Hardening only. *Trade-off:* deferred features; *gain:* stability and maturity.
- **ADR-013 — ADRs Mandatory for Cross-Cutting Decisions.** No cross-cutting change without an ADR. *Trade-off:* process overhead; *gain:* traceable rationale.
- **ADR-014 — Registries Are Read-Optimized, Cache-First.** Registries optimize reads, cache aggressively. *Trade-off:* eventual consistency on writes; *gain:* low latency at scale.
- **ADR-015 — Final Architecture Review & Maturity Gate.** Release requires a consolidated architecture review. *Trade-off:* release gate; *gain:* certified maturity.

---

## How to Propose a New ADR

1. **Identify the decision.** If a change is cross-cutting (affects multiple modules, the platform baseline, or an existing accepted ADR), it requires an ADR.
2. **Create the file.** Name it `docs/adr/adr-NNN-<kebab-slug>.md` using the next sequential number. Copy the standard template (Problem / Decision / Alternatives / Trade-offs / Consequences / Status) — see `docs/adr/README.md`.
3. **Set status to `PROPOSED`.** Add the entry to the index table above with status `PROPOSED`.
4. **Review.** Share for architecture review; the Enterprise AI Platform Engineering Team reviews against existing ADRs and module boundaries.
5. **Accept or reject.** On acceptance, set status to `ACCEPTED` and link the implementing work. To reverse, write a new ADR that sets the old one to `SUPERSEDED` — never edit an accepted ADR's decision.
6. **Reference.** Implementation specs and architecture docs must cite the relevant ADR id.

---

## Integration Points

- **Final Architecture Review (ADR-015)** — consumes all ADRs as evidence of maturity.
- **All 8 registry modules** — each is backed by one or more of ADR-001..ADR-014.
- **API Registry / Event Catalog** — ADR changes can be emitted as catalog events for traceability.
