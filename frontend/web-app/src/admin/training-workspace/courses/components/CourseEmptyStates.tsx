import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';

interface CourseEmptyStateProps {
  type: 'no-courses' | 'no-search' | 'no-drafts' | 'no-published' | 'no-archived';
  onClearFilters?: () => void;
}

const CONFIG = {
  'no-courses': {
    icon: 'book-open',
    title: 'No courses yet',
    description: 'Get started by creating your first training course.',
  },
  'no-search': {
    icon: 'search',
    title: 'No matching courses',
    description: 'Try adjusting your search or filters to find what you\'re looking for.',
  },
  'no-drafts': {
    icon: 'edit',
    title: 'No draft courses',
    description: 'Draft courses will appear here when you create them.',
  },
  'no-published': {
    icon: 'check-circle',
    title: 'No published courses',
    description: 'Published courses will appear here once they are live.',
  },
  'no-archived': {
    icon: 'archive',
    title: 'No archived courses',
    description: 'Archived courses will appear here when you archive them.',
  },
};

export const CourseEmptyState = memo(function CourseEmptyState({
  type,
  onClearFilters,
}: CourseEmptyStateProps) {
  const config = CONFIG[type];

  return (
    <div
      style={{
        display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
        padding: 'var(--space-stack-xl) var(--space-page-x)',
        textAlign: 'center', minHeight: 320,
      }}
      role="status"
    >
      <div style={{
        width: 72, height: 72, borderRadius: 'var(--radius-full)',
        background: 'var(--color-bg-skeleton-base)',
        color: 'var(--color-text-tertiary)',
        display: 'flex', alignItems: 'center', justifyContent: 'center',
        marginBottom: 'var(--space-stack-md)',
      }}>
        <Icon name={config.icon} size={32} color="currentColor" />
      </div>
      <h3 style={{
        margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)',
        fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)',
      }}>
        {config.title}
      </h3>
      <p style={{
        margin: '0 0 var(--space-stack-md)', fontSize: 'var(--text-body)',
        color: 'var(--color-text-secondary)', maxWidth: 400,
      }}>
        {config.description}
      </p>
      {onClearFilters && (
        <button
          type="button"
          onClick={onClearFilters}
          style={{
            display: 'flex', alignItems: 'center', gap: 4,
            padding: '6px 14px', borderRadius: 'var(--radius-input)',
            background: 'var(--color-bg-primary-default)',
            color: 'var(--color-text-on-primary)',
            border: 'none', cursor: 'pointer',
            fontSize: 'var(--text-body-sm)',
          }}
        >
          <Icon name="x" size={14} color="currentColor" />
          Clear filters
        </button>
      )}
    </div>
  );
});

export const CourseSkeletonGrid = memo(function CourseSkeletonGrid() {
  return (
    <div style={{
      display: 'grid',
      gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
      gap: 'var(--space-component-gap)',
    }}>
      {Array.from({ length: 6 }).map((_, i) => (
        <div key={i} style={{
          display: 'flex', flexDirection: 'column',
          borderRadius: 'var(--radius-card)',
          border: '1px solid var(--color-border-default)',
          overflow: 'hidden',
          background: 'var(--color-bg-surface-default)',
        }}>
          <div style={{
            height: 120,
            background: 'var(--color-bg-skeleton-base)',
            animation: 'skeletonPulse 1.5s ease-in-out infinite',
          }} />
          <div style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 8 }}>
            <div style={{
              height: 14, width: '80%', borderRadius: 'var(--radius-sm)',
              background: 'var(--color-bg-skeleton-base)',
              animation: 'skeletonPulse 1.5s ease-in-out infinite',
            }} />
            <div style={{
              height: 12, width: '100%', borderRadius: 'var(--radius-sm)',
              background: 'var(--color-bg-skeleton-base)',
              animation: 'skeletonPulse 1.5s ease-in-out infinite',
            }} />
            <div style={{
              height: 12, width: '60%', borderRadius: 'var(--radius-sm)',
              background: 'var(--color-bg-skeleton-base)',
              animation: 'skeletonPulse 1.5s ease-in-out infinite',
            }} />
            <div style={{
              display: 'flex', gap: 4, marginTop: 4,
            }}>
              <div style={{
                width: 50, height: 18, borderRadius: 'var(--radius-badge)',
                background: 'var(--color-bg-skeleton-base)',
                animation: 'skeletonPulse 1.5s ease-in-out infinite',
              }} />
              <div style={{
                width: 40, height: 18, borderRadius: 'var(--radius-badge)',
                background: 'var(--color-bg-skeleton-base)',
                animation: 'skeletonPulse 1.5s ease-in-out infinite',
              }} />
            </div>
          </div>
        </div>
      ))}
      <style>{`@keyframes skeletonPulse { 0%, 100% { opacity: 0.4; } 50% { opacity: 0.8; } }`}</style>
    </div>
  );
});
