# Sprint 26 Part 11 — End-to-End Data Flow Diagram

> Deliverable 3 of 12. Mock Mode only. No persistence — mock workflow validation only.

## 1. Course Lifecycle Data Flow (Mock)

The full authoring-to-learning lifecycle is represented as a mock workflow. No data is persisted; each stage is backed by its module's `data/*MockData.ts` provider.

```
 Create Course
      │  courses/data/courseMockData.ts (course record, status=draft)
      ▼
 Course Registry ──────────────────────────────► courses/ (registry, drafts, published, archived)
      │
      ▼
 Course Builder ───────────────────────────────► course-builder/ (info, objectives, media, SEO, publishing)
      │  BuilderContext holds working draft
      ▼
 Taxonomy ─────────────────────────────────────► course-taxonomy/ (category, skills, tags, competencies)
      │  taxonomy metadata attached to course (mock)
      ▼
 Curriculum ───────────────────────────────────► course-curriculum/ (modules, lessons, activities, paths)
      │  CurriculumContext holds structure
      ▼
 Resources ────────────────────────────────────► learning-resources/ (files, folders, collections, linking)
      │  resources mapped to curriculum (mock)
      ▼
 Pricing ──────────────────────────────────────► course-enrollment/ (pricing, policies, payment readiness)
      │
      ▼
 Course Catalog ───────────────────────────────► public-website/course-discovery/ (catalog, details, paths)
      │  published course surfaces publicly (mock)
      ▼
 Enrollment ───────────────────────────────────► course-enrollment/ (enrollment, capacity, waitlist, lifecycle)
      │
      ▼
 Analytics ────────────────────────────────────► analytics/ (executive, course, enrollment, curriculum, resource)
      │  KPIs computed from mock data
      ▼
 Notifications ────────────────────────────────► communication/ (announcements, notifications, scheduled)
      │
      ▼
 Future Student Learning ──────────────────────► (placeholder — attendance, assignments, assessments,
                                                   certificates, progress; architecture only, no impl)
```

## 2. Stage-to-Module Mapping

| Lifecycle stage | Module | Mock data source |
| --- | --- | --- |
| Create / Registry | courses | `courses/data/courseMockData.ts` |
| Authoring | course-builder | `course-builder/data/builderMockData.ts` |
| Classification | course-taxonomy | `course-taxonomy/data/taxonomyMockData.ts` |
| Structure | course-curriculum | `course-curriculum/data/curriculumMockData.ts` |
| Materials | learning-resources | `learning-resources/data/resourceMockData.ts` |
| Pricing / Enrollment / Capacity | course-enrollment | `course-enrollment/data/enrollmentMockData.ts` |
| Public discovery | course-discovery | discovery mock data (public-website) |
| Reporting | analytics | `analytics/data/analyticsMockData.ts` |
| Messaging | communication | `communication/data/communicationMockData.ts` |

## 3. Course Status Lifecycle (Validated States)

The following states are represented across the registry and builder modules and are consistent across the platform:

```
Draft ──► Review ──► Approval ──► Published ──► (Visible in Catalog)
                                      │
                                      ├─► Enrollment Ready ─► Capacity Available
                                      ├─► Analytics Ready
                                      ├─► Communication Ready
                                      └─► Archived ──► Future Version
```

- **Draft / Published / Archived** are first-class registry states (`courses/pages/{CourseDraftsPage, CoursePublishedPage, CourseArchivedPage}`).
- **Review / Approval / Publishing** are represented in the builder's publishing panel.
- **Enrollment Ready / Capacity Available** map to enrollment + capacity panels.
- **Analytics Ready / Communication Ready** are downstream-consumption states satisfied by the analytics and communication modules reading published-course mock data.
- **Future Version** is an architectural placeholder (no implementation).

## 4. Data Flow Guarantees

- **No persistence** — all data is in-memory mock; refreshing resets to seeded state.
- **Deterministic mock** — modules use seeded generators (e.g. communication's mulberry32 PRNG with a fixed base time), so the same lifecycle renders identically across sessions.
- **Future API compatibility** — each stage reads from a single per-module provider function, so a future API layer can replace the provider without touching UI (see Mock Data Validation deliverable).
- **One-directional flow** — downstream modules (analytics, communication) read published-course shape; they never mutate upstream authoring state.

## 5. Status

**Validated.** The complete mock lifecycle flows end-to-end across all modules with consistent status semantics and no persistence coupling.
