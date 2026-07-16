# Sprint 26 Part 11 — Cross-Module Dependency Graph

> Deliverable 2 of 12. Mock Mode only. Read-only validation artifact.

## 1. Method

A read-only import audit was run across every `.ts`/`.tsx` file under `src/admin/training-workspace`, searching for relative imports that cross a module boundary (`../<other-module>/...`). Design-system and shared-shell imports were catalogued separately.

## 2. Module Dependency Graph

```
                        ┌─────────────────────────────┐
                        │   Design System (frozen)     │  ◄── imported by ALL modules
                        │   src/design-system/**       │      (Icon, Card, Button,
                        └─────────────────────────────┘       Dialog, Badge, charts,
                                    ▲                          layout, Select, Input)
                                    │ (inward, one-way)
   ┌────────────────────────────────────────────────────────────────────────┐
   │                    Training Workspace Shell (Part 1)                     │
   │   WorkspaceContext · TrainingWorkspaceLayout · data/navigation.ts        │
   │   data/mockData.ts · components/{dashboard,workspace}                     │
   └────────────────────────────────────────────────────────────────────────┘
        ▲            ▲          ▲          ▲          ▲          ▲
        │ (shell consumed inward by module pages via <Outlet/> + shared constants)
        │
  ┌─────┴─────┬──────────┬───────────┬────────────┬───────────┬─────────────┐
  │ courses   │ builder  │ taxonomy  │ curriculum │ resources │ enrollment  │
  │ (P2)      │ (P3)     │ (P4)      │ (P5)       │ (P6)      │ (P7)        │
  └───────────┴──────────┴───────────┴────────────┴───────────┴─────────────┘

  ┌───────────┐        ┌───────────────┐
  │ analytics │◄───────│ communication │   (single cross-module edge, one-way)
  │ (P9)      │  reuse │ (P10)         │   communication imports KpiCard /
  └───────────┘  KPIs  └───────────────┘   WidgetGrid / WidgetCard from analytics

  ┌────────────────────────────────────────────────┐
  │ course-discovery (P8) — public-website/         │  (isolated public surface;
  │ no import to/from admin modules                 │   no admin coupling)
  └────────────────────────────────────────────────┘
```

## 3. Edge Inventory

| Edge | Type | Detail | Cyclic? |
| --- | --- | --- | --- |
| every module → Design System | inward, one-way | UI primitives | No |
| shell components → `data/navigation.ts`, `data/mockData.ts` | inward, one-way | read-only constants/types | No |
| **communication → analytics** | inward, one-way | `KpiCard`, `WidgetGrid`, `WidgetCard` reused on Communication Overview & Statistics pages | No |
| analytics → communication | **does not exist** | verified absent | — |
| courses / builder / taxonomy / curriculum / resources / enrollment | intra-module only | no sibling imports | No |
| course-discovery (public) ↔ admin modules | **none** | fully isolated | No |

## 4. Circular Dependency Analysis

**Result: NO circular dependencies.**

- The only sibling-to-sibling edge is `communication → analytics`.
- The reverse edge (`analytics → communication`) was explicitly searched and does not exist.
- No pair of modules mutually import each other.
- All other modules import only from within their own folder plus the Design System and shared read-only shell constants.

## 5. Reference Integrity

- **No broken references** — `tsc -b --noEmit` resolves every import (0 errors).
- **No duplicated cross-module state** — each module owns its own state; the shell owns only shell state (see State Management Report).
- **No inconsistent metadata** — module taxonomy/status enums are defined once per module in its `data/*` provider and consumed only within that module.

## 6. Consolidation Candidates (non-blocking)

These are healthy future-refactor notes, not integration defects:

- `communication/components/CommPagination.tsx` duplicates the concept of the shared `admin/components/navigation/Pagination.tsx` used by enrollment panels. Candidate for unification.
- `StatusBadge` exists in both `communication` and `course-enrollment` with domain-specific label/tone maps. Candidate for a generic DS badge wrapper.
- Per-module search inputs and filter selects could be promoted to a shared toolbar primitive in a later sprint.

None of these introduce cycles or broken references; they are documented for Part 12 planning.

## 7. Status

**Validated.** Dependency direction is inward-only, acyclic, and reference-complete.
