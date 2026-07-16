import { memo, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCourseState, type ViewMode } from '../state/courseState';
import { CourseExplorerToolbar } from '../components/CourseExplorerToolbar';
import { CourseGridView } from '../components/CourseGridView';
import { CourseListView } from '../components/CourseListView';
import { CourseTableView } from '../components/CourseTableView';
import { CourseDashboardWidgets } from '../components/CourseDashboardWidgets';
import { CourseEmptyState, CourseSkeletonGrid } from '../components/CourseEmptyStates';
import { Card } from '../../../../design-system/components/composite/Card';
import { Icon } from '../../../../design-system/icons/Icon';

const CourseRegistryPage = memo(function CourseRegistryPage() {
  const state = useCourseState();
  const navigate = useNavigate();

  const handleCreate = useCallback(() => {
    // Placeholder
  }, []);

  const renderView = useCallback(() => {
    if (state.loading) return <CourseSkeletonGrid />;

    if (state.paginatedCourses.length === 0) {
      const hasFilters = state.activeFilterCount > 0;
      if (hasFilters) {
        return <CourseEmptyState type="no-search" onClearFilters={state.clearFilters} />;
      }
      return <CourseEmptyState type="no-courses" />;
    }

    switch (state.viewMode) {
      case 'list':
        return (
          <CourseListView
            courses={state.paginatedCourses}
            selectedIds={state.selection.selectedIds}
            onToggleSelect={state.toggleSelect}
            onTogglePin={state.togglePinned}
            onToggleFavorite={state.toggleFavorite}
          />
        );
      case 'table':
        return (
          <CourseTableView
            courses={state.paginatedCourses}
            selectedIds={state.selection.selectedIds}
            onToggleSelect={state.toggleSelect}
            onTogglePin={state.togglePinned}
            onToggleFavorite={state.toggleFavorite}
          />
        );
      default:
        return (
          <CourseGridView
            courses={state.paginatedCourses}
            selectedIds={state.selection.selectedIds}
            onToggleSelect={state.toggleSelect}
            onTogglePin={state.togglePinned}
            onToggleFavorite={state.toggleFavorite}
          />
        );
    }
  }, [state]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{
        display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between',
        flexWrap: 'wrap', gap: 'var(--space-inline-md)',
      }}>
        <div>
          <h2 style={{
            margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h2)',
            color: 'var(--color-text-primary)',
          }}>
            Course Registry
          </h2>
          <p style={{
            margin: 0, fontSize: 'var(--text-body)',
            color: 'var(--color-text-secondary)',
          }}>
            Central registry for all training programs offered by SporeKart.
          </p>
        </div>
        <div style={{ display: 'flex', gap: 'var(--space-inline-sm)', flexShrink: 0 }}>
          <button
            type="button"
            onClick={() => navigate('/admin/training/courses/drafts')}
            style={{
              display: 'flex', alignItems: 'center', gap: 4,
              padding: '6px 12px', borderRadius: 'var(--radius-input)',
              background: 'transparent', border: '1px solid var(--color-border-default)',
              cursor: 'pointer', color: 'var(--color-text-secondary)',
              fontSize: 'var(--text-body-sm)',
            }}
          >
            <Icon name="edit" size={14} color="currentColor" />
            Drafts
          </button>
          <button
            type="button"
            onClick={() => navigate('/admin/training/courses/archived')}
            style={{
              display: 'flex', alignItems: 'center', gap: 4,
              padding: '6px 12px', borderRadius: 'var(--radius-input)',
              background: 'transparent', border: '1px solid var(--color-border-default)',
              cursor: 'pointer', color: 'var(--color-text-secondary)',
              fontSize: 'var(--text-body-sm)',
            }}
          >
            <Icon name="archive" size={14} color="currentColor" />
            Archived
          </button>
          <button
            type="button"
            onClick={handleCreate}
            style={{
              display: 'flex', alignItems: 'center', gap: 4,
              padding: '6px 14px', borderRadius: 'var(--radius-input)',
              background: 'var(--color-bg-primary-default)',
              color: 'var(--color-text-on-primary)',
              border: 'none', cursor: 'pointer', fontSize: 'var(--text-body-sm)',
            }}
          >
            <Icon name="plus" size={14} color="currentColor" />
            Create Course
          </button>
        </div>
      </div>

      <CourseDashboardWidgets stats={state.stats} />

      <Card variant="default" padding="md" as="div">
        <CourseExplorerToolbar
          viewMode={state.viewMode}
          onViewModeChange={(mode: ViewMode) => state.setViewMode(mode)}
          filters={state.filters}
          onFilterChange={state.updateFilter}
          onClearFilters={state.clearFilters}
          activeFilterCount={state.activeFilterCount}
          totalItems={state.totalItems}
          pinnedOnly={state.pinnedOnly}
          onPinnedOnlyChange={state.setPinnedOnly}
          favoritesOnly={state.favoritesOnly}
          onFavoritesOnlyChange={state.setFavoritesOnly}
          sortBy={state.sortBy}
          sortOrder={state.sortOrder}
          onSort={state.setSort}
          onClearSelection={state.clearSelection}
          selectionCount={state.selection.selectedIds.size}
        />
        <div style={{ marginTop: 'var(--space-stack-sm)' }}>
          {renderView()}
        </div>

        {state.totalPages > 1 && (
          <div style={{
            display: 'flex', justifyContent: 'center', alignItems: 'center',
            gap: 'var(--space-inline-sm)', marginTop: 'var(--space-stack-md)',
            paddingTop: 'var(--space-stack-sm)',
            borderTop: '1px solid var(--color-border-default)',
          }}>
            <button
              type="button"
              onClick={() => state.setPage(state.currentPage - 1)}
              disabled={state.currentPage <= 1}
              style={{
                padding: '4px 10px', borderRadius: 'var(--radius-input)',
                background: 'var(--color-bg-background)',
                border: '1px solid var(--color-border-default)',
                cursor: state.currentPage <= 1 ? 'default' : 'pointer',
                color: state.currentPage <= 1 ? 'var(--color-text-tertiary)' : 'var(--color-text-secondary)',
                fontSize: 'var(--text-caption)',
              }}
              aria-label="Previous page"
            >
              <Icon name="chevron-left" size={14} color="currentColor" />
            </button>
            <span style={{
              fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
            }}>
              Page {state.currentPage} of {state.totalPages}
            </span>
            <button
              type="button"
              onClick={() => state.setPage(state.currentPage + 1)}
              disabled={state.currentPage >= state.totalPages}
              style={{
                padding: '4px 10px', borderRadius: 'var(--radius-input)',
                background: 'var(--color-bg-background)',
                border: '1px solid var(--color-border-default)',
                cursor: state.currentPage >= state.totalPages ? 'default' : 'pointer',
                color: state.currentPage >= state.totalPages ? 'var(--color-text-tertiary)' : 'var(--color-text-secondary)',
                fontSize: 'var(--text-caption)',
              }}
              aria-label="Next page"
            >
              <Icon name="chevron-right" size={14} color="currentColor" />
            </button>
          </div>
        )}
      </Card>
    </div>
  );
});

export default CourseRegistryPage;
