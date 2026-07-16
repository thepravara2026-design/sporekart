# Enterprise Course Discovery Platform — Architecture

*Phase 11 — Sprint 26 — Part 8*

## 1. Context

The Enterprise Course Discovery Platform is the **public-facing acquisition channel** for the
SporeKart Enterprise Learning Management System. It sits on top of the certified LMS foundation
(Sprints 26 Parts 1–7: Workspace, Registry, Builder, Taxonomy, Curriculum, Resource Library,
Pricing/Enrollment/Capacity) and exposes a premium, SEO-optimized, high-performance browsing
experience for prospective learners — entirely in **Mock Mode** (no backend, API, DB, payments,
or real enrollments).

## 2. Folder Structure

```
src/public-website/course-discovery/
├── data/
│   ├── discoveryMockData.ts        # Public enrichment layer over Course model
│   └── catalogOptions.ts           # Filter/sort/view option sets + difficulty rank
├── state/
│   ├── useCatalogState.ts          # Catalog: search, filter, sort, paginate, view
│   └── useCourseSelection.ts       # Comparison set + bookmark/wishlist placeholders
├── components/
│   ├── CourseCard.tsx              # grid / list / compact / carousel variants
│   ├── CatalogToolbar.tsx          # Reused Search + Filter + Sort + View framework
│   ├── CatalogView.tsx            # View router + skeleton + empty state + carousel
│   ├── CatalogPagination.tsx      # Accessible, truncated pagination
│   ├── DiscoverySections.tsx      # Featured/Trending/Recommended/Recent/Carousel/Category
│   └── CourseComparisonTable.tsx  # Side-by-side comparison architecture
└── pages/
    ├── CourseCatalogPage.tsx      # Main catalog + discovery sections + comparison
    ├── CourseDetailsPage.tsx      # Premium detail experience + sticky panel + SEO
    ├── CourseCategoryPage.tsx     # Category-filtered catalog
    ├── LearningPathsPage.tsx      # Guided learning journeys
    ├── CourseComparisonPage.tsx   # Dedicated comparison route
    └── MarketingLandingPage.tsx   # Reusable topic landing template (10 topics)
```

The public layer **reuses** the single source of truth `Course` model and constants from
`src/admin/training-workspace/courses/data/courseMockData.ts`. No duplicate mock data exists.

## 3. Static SEO / Routes

| Route | Component | Purpose |
|-------|-----------|---------|
| `/training/courses` | CourseCatalogPage | Catalog + discovery + comparison |
| `/training/courses/:slug` | CourseDetailsPage | Premium course detail |
| `/training/courses/category/:slug` | CourseCategoryPage | Category page |
| `/training/courses/compare` | CourseComparisonPage | Comparison route |
| `/training/learning-paths` | LearningPathsPage | Learning paths |
| `/training/learn/:slug` | MarketingLandingPage | Marketing landing (10 topics) |

All routes are registered in `App.tsx` (lazy + `Suspense`) and declared in `public-website/config.ts`
(`PUBLIC_WEBSITE_ROUTES`, `isPublicWebsiteRoute`, primary nav, footer).

## 4. State Management

| State | Hook / Location |
|-------|-----------------|
| Catalog (search, filter, sort, pagination, view) | `useCatalogState` |
| Discovery sections (featured/trending/...) | Derived `useMemo` over catalog |
| Search state | `CatalogFilters.search` |
| Filter state | `CatalogFilters` (11 dimensions) |
| Sorting state | `CatalogSortField` (9 options) |
| Course detail state | `useParams` + `buildDiscoveryCourse` |
| Comparison state | `useCourseComparison` (max 3) |
| Bookmark / Wishlist placeholders | `useDiscoverySelection` (Set-based) |
| Mock data state | `buildDiscoveryCatalog(MOCK_COURSES)` |

## 5. Reuse (Zero Duplicate Components)

- **Search** — native `<input type="search" role="searchbox">` mirroring the enterprise search pattern.
- **Filter** — dropdown set reusing the enterprise filter shape (`CourseFilters`-compatible).
- **Pagination** — accessible truncated pager, same pattern as the admin registry.
- **Design System** — `Card`, `Badge`, `Icon`, `Seo`, `BreadcrumbFoundation`, `MediaPlaceholder`,
  `CardSkeleton` from `design-system` / `public-website`.
- **Course model** — imported, never duplicated.

## 6. Performance

- `React.memo` on `CourseCard`, `CatalogView`, `CatalogToolbar`, `DiscoverySection`, pages.
- `useMemo` for filtered/sorted/paginated data and derived sections.
- `useCallback` for all event handlers and filter updates.
- `React.lazy` route-level code splitting in `App.tsx`.
- Skeleton loaders (`CardSkeleton`) for loading states.
- Auto-fit CSS grid + scroll-snap carousels (no layout thrash).
- Virtualization-ready pagination (9/page) for thousands of courses.

## 7. Accessibility (WCAG 2.2 AA)

- Semantic landmarks (`nav`, `section`, `aside`, `table`).
- `aria-label` / `aria-pressed` / `aria-current` on interactive controls.
- Keyboard-operable carousels (`tabIndex`, focusable region).
- Visible focus styles via design tokens; reduced-motion respected by token transitions.
- Color-contrast-safe tokens; non-color-only state (icons + text).

## 8. Responsive

- Desktop / laptop / tablet / mobile via `auto-fill minmax(280px, 1fr)` grids.
- Carousels use horizontal scroll-snap (app-like on mobile, no fixed widths).
- Sticky enrollment panel collapses to stacked flow on narrow viewports.
- No horizontal page scrolling; `clamp`-free token spacing.

## 9. Future Integration Interfaces (interfaces only)

`Enrollment Engine`, `Payment Gateway`, `CRM`, `Recommendation Engine`, `AI Search`,
`Learning Analytics`, `Certificates`, `Student Reviews`, `Trainer Profiles`, `WhatsApp`,
`Email Marketing`, `Marketing Automation`, `Google Analytics`, `Google Search Console`.
Wired as CTA placeholders (`/training/enroll`), structured-data `Course` schema, and the
`recommended`/`trending` derivation hooks — no implementation.
