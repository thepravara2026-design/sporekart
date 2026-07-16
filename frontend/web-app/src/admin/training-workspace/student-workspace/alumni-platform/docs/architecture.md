# Alumni Platform — Architecture

## Overview
The Alumni Platform is Sprint 27 Part 11 of the SporeKart Enterprise LMS. It provides the centralized career services layer responsible for placement drives, internship management, career development, alumni networking, mentorship, and employer partnerships.

## Platform Scope
- Enterprise Placement Hub
- Internship Management Center
- Career Development Center
- Alumni Network & Directory
- Job Opportunity Repository
- Company Partnership Management
- Mentorship Program
- Contribution Tracking
- Event Management
- Placement & Alumni Analytics

## Architecture Principles
1. **Mock Mode** — No backend, APIs, database, AI, or external integrations
2. **Domain Driven Design** — Each capability is a bounded context
3. **Feature-first** — Organized by feature, not by layer
4. **Composition** — Reusable components composed into pages
5. **Separation of Concerns** — Types, data, state, UI, docs are separate

## Directory Structure
```
alumni-platform/
  components/     — 20+ reusable UI components
  data/           — Mock data generators (13 generators)
  docs/           — 16 documentation files
  pages/          — 12 platform pages
  state/          — AlumniContext (state management)
  types.ts        — Domain interfaces & constants
```

## Integration Points
- **navigation.ts** — Alumni is a STUDENT_NAV_ITEM under 'Career & Alumni' group
- **App.tsx** — Routes `/admin/training/student-workspace/alumni` and `alumni/*`
- **types.ts** (student-workspace) — `'alumni'` is a valid StudentStatus
- **Profile types** — `'placement'` and `'alumni'` are valid TimelineEvent types
