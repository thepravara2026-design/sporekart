import { memo } from 'react';
import { Link } from 'react-router-dom';
import { CourseCard } from './CourseCard';
import { Icon } from '../../../design-system/icons/Icon';
import type { DiscoveryCourse } from '../data/discoveryMockData';

export interface DiscoverySectionProps {
  title: string;
  eyebrow?: string;
  description?: string;
  courses: DiscoveryCourse[];
  view?: 'grid' | 'carousel';
  linkTo?: string;
  linkLabel?: string;
  compareIds?: string[];
  onToggleCompare?: (id: string) => void;
  bookmarks?: Set<string>;
  onToggleBookmark?: (id: string) => void;
  wishlist?: Set<string>;
  onToggleWishlist?: (id: string) => void;
}

export const DiscoverySection = memo(function DiscoverySection({
  title,
  eyebrow,
  description,
  courses,
  view = 'carousel',
  linkTo,
  linkLabel,
  compareIds = [],
  onToggleCompare,
  bookmarks,
  onToggleBookmark,
  wishlist,
  onToggleWishlist,
}: DiscoverySectionProps) {
  if (!courses.length) return null;

  return (
    <section aria-label={title} style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4, 16px)' }}>
      <div style={{ display: 'flex', alignItems: 'flex-end', justifyContent: 'space-between', gap: 'var(--space-3, 12px)', flexWrap: 'wrap' }}>
        <div>
          {eyebrow && (
            <span style={{ fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', color: 'var(--color-bg-accent-default, #2F6F4F)' }}>{eyebrow}</span>
          )}
          <h2 style={{ margin: '4px 0 0', fontSize: 'var(--text-h3, 22px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>{title}</h2>
          {description && <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary)' }}>{description}</p>}
        </div>
        {linkTo && linkLabel && (
          <Link to={linkTo} style={{ display: 'inline-flex', alignItems: 'center', gap: 4, color: 'var(--color-bg-accent-default, #2F6F4F)', fontWeight: 700, fontSize: 'var(--text-body-sm, 14px)', textDecoration: 'none', whiteSpace: 'nowrap' }}>
            {linkLabel} <Icon name="arrow-right" size={16} aria-label="View all" />
          </Link>
        )}
      </div>

      {view === 'carousel' ? (
        <div
          role="region"
          aria-label={`${title} carousel`}
          tabIndex={0}
          style={{ display: 'flex', gap: 'var(--space-4, 16px)', overflowX: 'auto', paddingBottom: 'var(--space-2, 8px)', scrollSnapType: 'x mandatory', WebkitOverflowScrolling: 'touch' }}
        >
          {courses.map((dc) => (
            <div key={dc.course.id} style={{ flex: '0 0 300px', scrollSnapAlign: 'start' }}>
              <CourseCard
                dc={dc}
                view="grid"
                compareSelected={compareIds.includes(dc.course.id)}
                onToggleCompare={onToggleCompare}
                bookmarked={bookmarks?.has(dc.course.id)}
                onToggleBookmark={onToggleBookmark}
                wishlisted={wishlist?.has(dc.course.id)}
                onToggleWishlist={onToggleWishlist}
              />
            </div>
          ))}
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-4, 16px)' }}>
          {courses.map((dc) => (
            <CourseCard
              key={dc.course.id}
              dc={dc}
              view="grid"
              compareSelected={compareIds.includes(dc.course.id)}
              onToggleCompare={onToggleCompare}
              bookmarked={bookmarks?.has(dc.course.id)}
              onToggleBookmark={onToggleBookmark}
              wishlisted={wishlist?.has(dc.course.id)}
              onToggleWishlist={onToggleWishlist}
            />
          ))}
        </div>
      )}
    </section>
  );
});

// Category highlight strip linking to category pages.
export const CategoryHighlights = memo(function CategoryHighlights({
  categories,
}: {
  categories: { slug: string; label: string; color: string; count: number }[];
}) {
  return (
    <section aria-label="Browse by category" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3, 12px)' }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h3, 22px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>Browse by category</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 'var(--space-3, 12px)' }}>
        {categories.map((c) => (
          <Link
            key={c.slug}
            to={`/training/courses/category/${c.slug}`}
            style={{
              display: 'flex', alignItems: 'center', gap: 'var(--space-3, 12px)',
              padding: 'var(--space-4, 16px)', borderRadius: 'var(--radius-lg, 12px)',
              border: '1px solid var(--color-border-default)', textDecoration: 'none',
              background: `${c.color}0f`,
            }}
            aria-label={`${c.label} — ${c.count} courses`}
          >
            <span style={{ width: 40, height: 40, borderRadius: 'var(--radius-md, 8px)', background: `${c.color}26`, display: 'inline-flex', alignItems: 'center', justifyContent: 'center', color: c.color, fontWeight: 800 }}>
              <Icon name="book-open" size={20} color={c.color} />
            </span>
            <span style={{ display: 'flex', flexDirection: 'column' }}>
              <span style={{ fontWeight: 700, color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm, 14px)' }}>{c.label}</span>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{c.count} courses</span>
            </span>
          </Link>
        ))}
      </div>
    </section>
  );
});
