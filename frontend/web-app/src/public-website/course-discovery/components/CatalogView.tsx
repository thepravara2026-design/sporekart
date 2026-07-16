import { memo } from 'react';
import { CourseCard } from './CourseCard';
import type { DiscoveryCourse } from '../data/discoveryMockData';
import type { CatalogViewMode } from '../data/catalogOptions';

export interface CatalogViewProps {
  courses: DiscoveryCourse[];
  view: CatalogViewMode;
  compareIds: string[];
  onToggleCompare: (id: string) => void;
  bookmarks: Set<string>;
  onToggleBookmark: (id: string) => void;
  wishlist: Set<string>;
  onToggleWishlist: (id: string) => void;
}

function GridView({ courses, ...rest }: CatalogViewProps) {
  return (
    <div
      style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
        gap: 'var(--space-4, 16px)',
      }}
    >
      {courses.map((dc) => (
        <CourseCard
          key={dc.course.id}
          dc={dc}
          view="grid"
          compareSelected={rest.compareIds.includes(dc.course.id)}
          onToggleCompare={rest.onToggleCompare}
          bookmarked={rest.bookmarks.has(dc.course.id)}
          onToggleBookmark={rest.onToggleBookmark}
          wishlisted={rest.wishlist.has(dc.course.id)}
          onToggleWishlist={rest.onToggleWishlist}
        />
      ))}
    </div>
  );
}

function ListView({ courses, ...rest }: CatalogViewProps) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3, 12px)' }}>
      {courses.map((dc) => (
        <CourseCard
          key={dc.course.id}
          dc={dc}
          view="list"
          compareSelected={rest.compareIds.includes(dc.course.id)}
          onToggleCompare={rest.onToggleCompare}
          bookmarked={rest.bookmarks.has(dc.course.id)}
          onToggleBookmark={rest.onToggleBookmark}
          wishlisted={rest.wishlist.has(dc.course.id)}
          onToggleWishlist={rest.onToggleWishlist}
        />
      ))}
    </div>
  );
}

function CompactView({ courses }: CatalogViewProps) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
      {courses.map((dc) => (
        <CourseCard key={dc.course.id} dc={dc} view="compact" />
      ))}
    </div>
  );
}

function FeaturedView({ courses, ...rest }: CatalogViewProps) {
  if (courses.length === 0) return null;
  const [hero, ...rest2] = courses;
  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: 'var(--space-4, 16px)' }}>
      {[hero, ...rest2].map((dc) => (
        <CourseCard
          key={dc.course.id}
          dc={dc}
          view="grid"
          compareSelected={rest.compareIds.includes(dc.course.id)}
          onToggleCompare={rest.onToggleCompare}
          bookmarked={rest.bookmarks.has(dc.course.id)}
          onToggleBookmark={rest.onToggleBookmark}
          wishlisted={rest.wishlist.has(dc.course.id)}
          onToggleWishlist={rest.onToggleWishlist}
        />
      ))}
    </div>
  );
}

export const CatalogView = memo(function CatalogView(props: CatalogViewProps) {
  switch (props.view) {
    case 'list':
      return <ListView {...props} />;
    case 'compact':
      return <CompactView {...props} />;
    case 'featured':
      return <FeaturedView {...props} />;
    case 'carousel':
      return <CarouselView {...props} />;
    default:
      return <GridView {...props} />;
  }
});

import { CardSkeleton } from '../../../design-system/components/display/CardSkeleton';

export function CatalogSkeletonGrid({ count = 9 }: { count?: number }) {
  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-4, 16px)' }}>
      {Array.from({ length: count }).map((_, i) => (
        <div key={i} style={{ height: 320 }}>
          <CardSkeleton hasImage lines={3} />
        </div>
      ))}
    </div>
  );
}

export function CatalogEmptyState({ onClear }: { onClear?: () => void }) {
  return (
    <div style={{ textAlign: 'center', padding: 'var(--space-section-gap, 48px) 0' }}>
      <div style={{ width: 64, height: 64, margin: '0 auto var(--space-3)', borderRadius: 'var(--radius-full)', background: 'var(--color-bg-accent-subtle, #eef2ff)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <span style={{ color: 'var(--color-bg-accent-default, #2F6F4F)' }}><CourseCardEmptyIcon /></span>
      </div>
      <h3 style={{ margin: '0 0 var(--space-2)', color: 'var(--color-text-primary)' }}>No courses match your filters</h3>
      <p style={{ margin: '0 0 var(--space-4)', color: 'var(--color-text-secondary)' }}>Try adjusting or clearing your filters to see more programs.</p>
      {onClear && (
        <button type="button" onClick={onClear} style={{ padding: '8px 16px', borderRadius: 'var(--radius-pill)', border: 'none', background: 'var(--color-bg-accent-default, #2F6F4F)', color: '#fff', fontWeight: 700, cursor: 'pointer' }}>
          Clear filters
        </button>
      )}
    </div>
  );
}

function CourseCardEmptyIcon() {
  return <span style={{ fontSize: 28 }}>🔍</span>;
}

// A scrollable, keyboard-accessible carousel of course cards.
export const CarouselView = memo(function CarouselView({ courses, ...rest }: CatalogViewProps) {
  return (
    <div
      role="region"
      aria-label="Course carousel"
      tabIndex={0}
      style={{
        display: 'flex',
        gap: 'var(--space-4, 16px)',
        overflowX: 'auto',
        paddingBottom: 'var(--space-2, 8px)',
        scrollSnapType: 'x mandatory',
        WebkitOverflowScrolling: 'touch',
      }}
    >
      {courses.map((dc) => (
        <div key={dc.course.id} style={{ flex: '0 0 300px', scrollSnapAlign: 'start' }}>
          <CourseCard
            dc={dc}
            view="grid"
            compareSelected={rest.compareIds.includes(dc.course.id)}
            onToggleCompare={rest.onToggleCompare}
            bookmarked={rest.bookmarks.has(dc.course.id)}
            onToggleBookmark={rest.onToggleBookmark}
            wishlisted={rest.wishlist.has(dc.course.id)}
            onToggleWishlist={rest.onToggleWishlist}
          />
        </div>
      ))}
    </div>
  );
});
