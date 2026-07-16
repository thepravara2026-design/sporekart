# Student Profile — Architecture

## Overview

The Student Profile module extends the Student Workspace & Registry (Sprint 27 Part 1) with a comprehensive profile management system. It follows the same mock-first, service-boundary-aware pattern established in Part 1.

## Directory Structure

```
profile/
  types.ts                — Domain model types and constants
  data/mockData.ts        — Mock profile data generator
  state/ProfileContext.tsx — React Context for profile state
  components/
    ProfileHeader.tsx           — Avatar + name + status + quick stats
    ProfileCompletenessCard.tsx — Completeness progress bar per section
    ProfileTimeline.tsx         — Sequential activity timeline
    ProfileInfoCard.tsx         — Reusable label/value info card
    DocumentStatusCard.tsx      — Document upload/verify status grid
    ProfileNavigation.tsx       — Sub-navigation tabs for profile sections
    ProfileSkeleton.tsx         — Loading skeletons for all profile variants
  pages/
    ProfileIndex.tsx        — Wrapper with sub-nav and section switching
    ProfileMainPage.tsx      — Overview dashboard with all sections
    ProfileEditPage.tsx      — Editable form for profile fields
    ProfileTimelinePage.tsx   — Full activity timeline
    ProfileAcademicPage.tsx   — Academic qualifications
    ProfileProfessionalPage.tsx — Professional info
    ProfileLearningPage.tsx    — Learning preferences
    ProfileGuardianPage.tsx    — Guardian + emergency contact
    ProfileDocumentsPage.tsx   — Document metadata grid
  docs/
    architecture.md
    accessibility.md
    responsive.md
    performance.md
```

## State Management

`ProfileContext` provides:
- `currentProfile` — the selected `StudentProfile`
- `profiles` — full list (for future multi-profile views)
- `selectProfile(studentId)` / `selectProfileById(id)`
- `updateProfile(updated)` — mutation for edit page
- `searchTerm` / `filterStatus` — for future filtering

## Navigation

- `navigation.ts` moved `profile` from `STUDENT_FUTURE_ITEMS` to `STUDENT_NAV_ITEMS`
- App.tsx route: `/admin/training/student-workspace/profile` renders `ProfileIndex`
- Profile sub-navigation switches between 8 sections client-side (no route change)

## Mock Data

`generateMockProfiles(10)` produces 10 varied `StudentProfile` objects with:
- Randomized personal, academic, professional, learning, guardian, emergency, documents, and timeline data
- Realistic Indian addresses, names, and domain-specific professions (mushroom farming, agriculture)
- Dynamic `ProfileCompleteness` calculation based on filled fields
- Deterministic generation (same index = same data)

## Future

When real APIs arrive:
1. Replace `data/mockData.ts` with service calls
2. Replace `ProfileContext` fetching logic with async API calls
3. Keep all UI components and pages unchanged
