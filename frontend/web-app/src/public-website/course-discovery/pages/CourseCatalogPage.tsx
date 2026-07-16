import { memo, useMemo, useState } from 'react';
import { Seo } from '../../Seo';
import { BreadcrumbFoundation } from '../../BreadcrumbFoundation';
import { CatalogToolbar } from '../components/CatalogToolbar';
import { CatalogView, CatalogSkeletonGrid, CatalogEmptyState } from '../components/CatalogView';
import { CatalogPagination } from '../components/CatalogPagination';
import { DiscoverySection, CategoryHighlights } from '../components/DiscoverySections';
import { CourseComparisonTable } from '../components/CourseComparisonTable';
import { useCatalogState } from '../state/useCatalogState';
import { useCourseComparison, useDiscoverySelection } from '../state/useCourseSelection';
import { buildDiscoveryCatalog, categoryLabel, categoryColor } from '../data/discoveryMockData';
import { MOCK_COURSES } from '../../../admin/training-workspace/courses/data/courseMockData';
import type { CatalogViewMode } from '../data/catalogOptions';

const ALL = buildDiscoveryCatalog(MOCK_COURSES);

export const CourseCatalogPage = memo(function CourseCatalogPage() {
  const [loading] = useState(false);
  const state = useCatalogState(ALL);
  const selection = useDiscoverySelection();
  const comparison = useCourseComparison(ALL);
  const [view, setView] = useState<CatalogViewMode>(state.viewMode);

  const onViewChange = (mode: CatalogViewMode) => {
    setView(mode);
    state.setViewMode(mode);
  };

  const featured = useMemo(() => ALL.filter((c) => c.featured).slice(0, 8), []);
  const trending = useMemo(() => ALL.filter((c) => c.trending).slice(0, 8), []);
  const recommended = useMemo(() => ALL.filter((c) => c.recommended).slice(0, 8), []);
  const recentlyAdded = useMemo(() => [...ALL].sort((a, b) => b.course.createdAt.localeCompare(a.course.createdAt)).slice(0, 8), []);
  const upcoming = useMemo(() => ALL.filter((c) => c.upcoming).slice(0, 8), []);

  const categoryHighlights = useMemo(() => {
    const counts = new Map<string, number>();
    MOCK_COURSES.forEach((c) => counts.set(c.category, (counts.get(c.category) ?? 0) + 1));
    return Array.from(counts.entries()).map(([cat, count]) => ({
      slug: cat,
      label: categoryLabel(cat as never),
      color: categoryColor(cat as never),
      count,
    }));
  }, []);

  const viewProps = {
    compareIds: comparison.compareIds,
    onToggleCompare: comparison.toggleCompare,
    bookmarks: selection.bookmarks,
    onToggleBookmark: selection.toggleBookmark,
    wishlist: selection.wishlist,
    onToggleWishlist: selection.toggleWishlist,
  };

  return (
    <>
      <Seo
        title="Course Catalog — SporeKart Training"
        description="Explore SporeKart's enterprise training catalog: mushroom cultivation, spawn production, commercial farming, and more. Browse, compare, and enroll in expert-led courses."
        canonical="https://sporekart.example.com/training/courses"
      />
      <BreadcrumbFoundation items={[{ label: 'Training', href: '/training' }, { label: 'Course Catalog' }]} />

      <header style={{ marginBottom: 'var(--space-5, 24px)' }}>
        <span style={{ fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', color: 'var(--color-bg-accent-default, #2F6F4F)' }}>Enterprise Learning</span>
        <h1 style={{ margin: '4px 0 0', fontSize: 'var(--text-h1, 32px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>Course Catalog</h1>
        <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary)', maxWidth: 720 }}>
          Discover expert-led training programs across cultivation, spawn production, commercial farming, and entrepreneurship.
        </p>
      </header>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-6, 32px)' }}>
        <CategoryHighlights categories={categoryHighlights} />

        <DiscoverySection title="Featured Courses" eyebrow="Handpicked" courses={featured} view="carousel" linkTo="/training/courses?view=featured" linkLabel="View all" {...viewProps} />
        <DiscoverySection title="Trending Courses" eyebrow="Popular now" courses={trending} view="carousel" linkTo="/training/courses?sort=trending" linkLabel="View all" {...viewProps} />
        <DiscoverySection title="Recommended for you" eyebrow="Curated" courses={recommended} view="carousel" {...viewProps} />
        <DiscoverySection title="Recently Added" eyebrow="New" courses={recentlyAdded} view="carousel" {...viewProps} />
        {upcoming.length > 0 && <DiscoverySection title="Upcoming Batches" eyebrow="Enrolling soon" courses={upcoming} view="carousel" {...viewProps} />}

        {/* Filterable explorer */}
        <section aria-label="Browse all courses" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4, 16px)' }}>
          <h2 style={{ margin: 0, fontSize: 'var(--text-h3, 22px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>Browse all courses</h2>
          <CatalogToolbar
            filters={state.filters}
            onFilterChange={state.updateFilter}
            onClearFilters={state.clearFilters}
            activeFilterCount={state.activeFilterCount}
            sort={state.sort}
            onSortChange={state.setSortField}
            viewMode={view}
            onViewModeChange={onViewChange}
            totalItems={state.totalItems}
          />

          {loading ? (
            <CatalogSkeletonGrid count={state.totalItems > 0 ? Math.min(state.totalItems, 9) : 9} />
          ) : state.paginated.length === 0 ? (
            <CatalogEmptyState onClear={state.clearFilters} />
          ) : (
            <CatalogView courses={state.paginated} view={view} {...viewProps} />
          )}

          <CatalogPagination currentPage={state.currentPage} totalPages={state.totalPages} onPageChange={state.setPage} />
        </section>

        {comparison.compareCourses.length > 0 && (
          <section aria-label="Course comparison" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3, 12px)' }}>
            <CourseComparisonTable courses={comparison.compareCourses} onRemove={comparison.toggleCompare} onClear={comparison.clearCompare} />
          </section>
        )}
      </div>
    </>
  );
});

export default CourseCatalogPage;
