import { memo, useMemo } from 'react';
import { useParams } from 'react-router-dom';
import { Seo } from '../../Seo';
import { BreadcrumbFoundation } from '../../BreadcrumbFoundation';
import { CatalogToolbar } from '../components/CatalogToolbar';
import { CatalogView, CatalogEmptyState } from '../components/CatalogView';
import { CatalogPagination } from '../components/CatalogPagination';
import { useCatalogState } from '../state/useCatalogState';
import { useCourseComparison, useDiscoverySelection } from '../state/useCourseSelection';
import { buildDiscoveryCatalog, categoryLabel, categoryColor } from '../data/discoveryMockData';
import { MOCK_COURSES, type TrainingCategory } from '../../../admin/training-workspace/courses/data/courseMockData';
import type { CatalogViewMode } from '../data/catalogOptions';

const ALL = buildDiscoveryCatalog(MOCK_COURSES);

export const CourseCategoryPage = memo(function CourseCategoryPage() {
  const { slug } = useParams<{ slug: string }>();
  const category = slug as TrainingCategory;

  const filteredAll = useMemo(
    () => (category ? ALL.filter((c) => c.course.category === category) : ALL),
    [category],
  );
  const state = useCatalogState(filteredAll);
  const selection = useDiscoverySelection();
  const comparison = useCourseComparison(ALL);

  const accent = category ? categoryColor(category) : '#2F6F4F';
  const label = category ? categoryLabel(category) : 'All Categories';

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
        title={`${label} Courses — SporeKart Training`}
        description={`Browse ${label} training courses from SporeKart. Expert-led, hands-on programs with certification.`}
        canonical={`https://sporekart.example.com/training/courses/category/${slug}`}
      />
      <BreadcrumbFoundation items={[{ label: 'Training', href: '/training' }, { label: 'Courses', href: '/training/courses' }, { label }]} />

      <header style={{ marginBottom: 'var(--space-5, 24px)', borderLeft: `4px solid ${accent}`, paddingLeft: 'var(--space-4, 16px)' }}>
        <h1 style={{ margin: 0, fontSize: 'var(--text-h1, 32px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>{label} Courses</h1>
        <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary)' }}>
          Explore {filteredAll.length} program{filteredAll.length !== 1 ? 's' : ''} in {label.toLowerCase()}.
        </p>
      </header>

      <CatalogToolbar
        filters={state.filters}
        onFilterChange={state.updateFilter}
        onClearFilters={state.clearFilters}
        activeFilterCount={state.activeFilterCount}
        sort={state.sort}
        onSortChange={state.setSortField}
        viewMode={state.viewMode}
        onViewModeChange={state.setViewMode}
        totalItems={state.totalItems}
      />

      <div style={{ marginTop: 'var(--space-4, 16px)' }}>
        {state.paginated.length === 0 ? (
          <CatalogEmptyState onClear={state.clearFilters} />
        ) : (
          <CatalogView courses={state.paginated} view={state.viewMode as CatalogViewMode} {...viewProps} />
        )}
      </div>

      <CatalogPagination currentPage={state.currentPage} totalPages={state.totalPages} onPageChange={state.setPage} />
    </>
  );
});

export default CourseCategoryPage;
