# Folder Structure — Sprint 26 Part 8

```
frontend/web-app/src/public-website/
├── config.ts                      # + new routes, nav, isPublicWebsiteRoute (modified)
├── pages/TrainingPage.tsx         # + CTA → /training/courses (modified)
└── course-discovery/              # NEW feature tree
    ├── data/
    │   ├── discoveryMockData.ts   # Public enrichment over Course model
    │   └── catalogOptions.ts      # Filter/sort/view sets, difficulty rank
    ├── state/
    │   ├── useCatalogState.ts     # search/filter/sort/paginate/view
    │   └── useCourseSelection.ts  # compare + bookmark/wishlist placeholders
    ├── components/
    │   ├── CourseCard.tsx
    │   ├── CatalogToolbar.tsx
    │   ├── CatalogView.tsx
    │   ├── CatalogPagination.tsx
    │   ├── DiscoverySections.tsx
    │   └── CourseComparisonTable.tsx
    └── pages/
        ├── CourseCatalogPage.tsx
        ├── CourseDetailsPage.tsx
        ├── CourseCategoryPage.tsx
        ├── LearningPathsPage.tsx
        ├── CourseComparisonPage.tsx
        └── MarketingLandingPage.tsx

frontend/web-app/src/App.tsx       # + 6 lazy routes (modified)
```

## Reused (not duplicated)
- `design-system/components/composite/Card`
- `design-system/components/display/Badge`, `CardSkeleton`
- `design-system/components/display/Icon`
- `public-website/Seo`, `BreadcrumbFoundation`, `MediaPlaceholder`
- `admin/training-workspace/courses/data/courseMockData` (Course model + constants)
