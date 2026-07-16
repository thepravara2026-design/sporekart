# Component Inventory — Sprint 26 Part 8

## New Components (course-discovery)
| Component | File | Reuses |
|-----------|------|--------|
| CourseCard (grid/list/compact/carousel) | `components/CourseCard.tsx` | Card, Badge, Icon, MediaPlaceholder |
| CatalogToolbar | `components/CatalogToolbar.tsx` | Icon, design tokens |
| CatalogView | `components/CatalogView.tsx` | CourseCard, CardSkeleton |
| CatalogPagination | `components/CatalogPagination.tsx` | Icon |
| DiscoverySection / CategoryHighlights | `components/DiscoverySections.tsx` | CourseCard, Icon |
| CourseComparisonTable | `components/CourseComparisonTable.tsx` | Badge, Icon |

## New Pages
| Page | File |
|------|------|
| CourseCatalogPage | `pages/CourseCatalogPage.tsx` |
| CourseDetailsPage | `pages/CourseDetailsPage.tsx` |
| CourseCategoryPage | `pages/CourseCategoryPage.tsx` |
| LearningPathsPage | `pages/LearningPathsPage.tsx` |
| CourseComparisonPage | `pages/CourseComparisonPage.tsx` |
| MarketingLandingPage | `pages/MarketingLandingPage.tsx` |

## New State / Data
| Module | File |
|--------|------|
| useCatalogState | `state/useCatalogState.ts` |
| useCourseSelection (compare + bookmark/wishlist) | `state/useCourseSelection.ts` |
| discoveryMockData | `data/discoveryMockData.ts` |
| catalogOptions | `data/catalogOptions.ts` |

## Reused (no duplication)
`Card`, `Badge`, `Icon`, `Seo`, `BreadcrumbFoundation`, `MediaPlaceholder`, `CardSkeleton`,
`Container`, plus the `Course` model from the admin registry. Zero duplicate components created.
