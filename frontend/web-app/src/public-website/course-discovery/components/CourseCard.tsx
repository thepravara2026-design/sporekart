import { memo, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { Card } from '../../../design-system/components/composite/Card';
import { Badge } from '../../../design-system/components/display/Badge';
import { Icon } from '../../../design-system/icons/Icon';
import { MediaPlaceholder } from '../../home/MediaPlaceholder';
import {
  categoryColor,
  categoryLabel,
  deliveryLabel,
  levelLabel,
  type DiscoveryCourse,
} from '../data/discoveryMockData';
import type { CatalogViewMode } from '../data/catalogOptions';

interface CourseCardProps {
  dc: DiscoveryCourse;
  view?: Extract<CatalogViewMode, 'grid' | 'list' | 'compact' | 'carousel'>;
  compareSelected?: boolean;
  onToggleCompare?: (id: string) => void;
  bookmarked?: boolean;
  onToggleBookmark?: (id: string) => void;
  wishlisted?: boolean;
  onToggleWishlist?: (id: string) => void;
}

function formatPrice(price: number, currency: string): string {
  if (price === 0) return 'Free';
  const symbol = currency === 'INR' ? '₹' : '$';
  return `${symbol}${price.toLocaleString('en-IN')}`;
}

const BADGE_VARIANT: Record<string, 'primary' | 'success' | 'warning' | 'info' | 'danger' | 'neutral'> = {
  featured: 'primary',
  new: 'success',
  trending: 'warning',
  corporate: 'info',
  government: 'danger',
  popular: 'neutral',
};

export const CourseCard = memo(function CourseCard({
  dc,
  view = 'grid',
  compareSelected = false,
  onToggleCompare,
  bookmarked = false,
  onToggleBookmark,
  wishlisted = false,
  onToggleWishlist,
}: CourseCardProps) {
  const { course, price, originalPrice, availableSeats, totalSeats, badges, currency } = dc;
  const accent = categoryColor(course.category);
  const detailPath = `/training/courses/${course.slug}`;
  const seatRatio = availableSeats / totalSeats;

  const handleCompare = useCallback(
    (e: React.MouseEvent) => {
      e.preventDefault();
      e.stopPropagation();
      onToggleCompare?.(course.id);
    },
    [course.id, onToggleCompare],
  );

  const handleBookmark = useCallback(
    (e: React.MouseEvent) => {
      e.preventDefault();
      e.stopPropagation();
      onToggleBookmark?.(course.id);
    },
    [course.id, onToggleBookmark],
  );

  const handleWishlist = useCallback(
    (e: React.MouseEvent) => {
      e.preventDefault();
      e.stopPropagation();
      onToggleWishlist?.(course.id);
    },
    [course.id, onToggleWishlist],
  );

  const badgesRow = (
    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
      {badges.map((b) => (
        <Badge key={b.kind} size="sm" variant={BADGE_VARIANT[b.kind]}>
          {b.label}
        </Badge>
      ))}
    </div>
  );

  const actionRow = (
    <div style={{ display: 'flex', alignItems: 'center', gap: 6, flexWrap: 'wrap' }}>
      <Link
        to={detailPath}
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          gap: 4,
          padding: '6px 12px',
          borderRadius: 'var(--radius-pill, 999px)',
          backgroundColor: 'var(--color-bg-accent-default, #2F6F4F)',
          color: '#fff',
          fontWeight: 700,
          fontSize: 'var(--text-body-sm, 14px)',
          textDecoration: 'none',
        }}
        aria-label={`View ${course.name}`}
      >
        View Course <Icon name="arrow-right" size={14} aria-label="View" />
      </Link>
      <button
        type="button"
        onClick={handleCompare}
        aria-pressed={compareSelected}
        aria-label={compareSelected ? 'Remove from comparison' : 'Add to comparison'}
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          gap: 4,
          padding: '6px 10px',
          borderRadius: 'var(--radius-pill, 999px)',
          border: `1px solid ${compareSelected ? 'var(--color-bg-accent-default, #2F6F4F)' : 'var(--color-border-default)'}`,
          background: 'transparent',
          color: compareSelected ? 'var(--color-bg-accent-default, #2F6F4F)' : 'var(--color-text-secondary, #4b5563)',
          cursor: 'pointer',
          fontSize: 'var(--text-body-xs, 12px)',
          fontWeight: 600,
        }}
      >
        <Icon name="sliders" size={13} aria-label="Compare" />
        Compare
      </button>
    </div>
  );

  const metaRow = (
    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2, 8px)', fontSize: 'var(--text-body-xs, 12px)', color: 'var(--color-text-secondary, #4b5563)' }}>
      <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4 }}>
        <Icon name="bar-chart" size={13} aria-label="Level" /> {levelLabel(course.level)}
      </span>
      <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4 }}>
        <Icon name="clock" size={13} aria-label="Duration" /> {course.duration}
      </span>
      <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4 }}>
        <Icon name="monitor" size={13} aria-label="Delivery" /> {deliveryLabel(course.deliveryMode)}
      </span>
      <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4 }}>
        <Icon name="globe" size={13} aria-label="Language" /> {course.language}
      </span>
    </div>
  );

  const seatRow = (
    <div style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-body-xs, 12px)' }}>
      <span
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          gap: 4,
          fontWeight: 600,
          color: seatRatio <= 0 ? 'var(--color-danger)' : seatRatio <= 0.2 ? 'var(--color-warning)' : 'var(--color-success)',
        }}
      >
        <Icon name={seatRatio <= 0 ? 'x-circle' : 'users'} size={13} aria-label="Seats" />
        {seatRatio <= 0 ? 'Waitlist' : `${availableSeats} seats left`}
      </span>
    </div>
  );

  if (view === 'compact') {
    return (
      <Card variant="default" padding="sm" as="article" aria-label={course.name}>
        <Link to={detailPath} style={{ display: 'flex', gap: 'var(--space-3, 12px)', textDecoration: 'none', alignItems: 'center' }} aria-label={course.name}>
          <div style={{ width: 64, height: 64, flexShrink: 0, borderRadius: 'var(--radius-md, 8px)', background: `${accent}1f`, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Icon name="book-open" size={24} color={accent} />
          </div>
          <div style={{ minWidth: 0, flex: 1 }}>
            <div style={{ fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, color: accent, textTransform: 'uppercase', letterSpacing: '0.04em' }}>{categoryLabel(course.category)}</div>
            <h3 style={{ margin: '2px 0 0', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 700, color: 'var(--color-text-primary)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{course.name}</h3>
            <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
              <span>{course.duration}</span><span>{levelLabel(course.level)}</span><span>{formatPrice(price, currency)}</span>
            </div>
          </div>
        </Link>
      </Card>
    );
  }

  if (view === 'list') {
    return (
      <Card variant="default" padding="none" as="article" aria-label={course.name} hoverable>
        <div style={{ display: 'flex', gap: 0, alignItems: 'stretch', flexWrap: 'wrap' }}>
          <div style={{ width: 200, flexShrink: 0, background: `${accent}14`, display: 'flex', alignItems: 'center', justifyContent: 'center', position: 'relative' }}>
            <Icon name="book-open" size={32} color={accent} />
            <div style={{ position: 'absolute', top: 8, left: 8 }}>{badgesRow}</div>
            <div style={{ position: 'absolute', top: 8, right: 8, display: 'flex', gap: 4 }}>
              <button type="button" onClick={handleBookmark} aria-label={bookmarked ? 'Remove bookmark' : 'Bookmark'} style={iconBtnStyle(bookmarked ? 'var(--color-warning)' : 'var(--color-text-tertiary)')}>
                <Icon name="book" size={14} color="currentColor" />
              </button>
              <button type="button" onClick={handleWishlist} aria-label={wishlisted ? 'Remove from wishlist' : 'Add to wishlist'} style={iconBtnStyle(wishlisted ? 'var(--color-danger)' : 'var(--color-text-tertiary)')}>
                <Icon name="heart" size={14} color="currentColor" />
              </button>
            </div>
          </div>
          <div style={{ flex: 1, minWidth: 240, padding: 'var(--space-4, 16px)', display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', gap: 8 }}>
              <span style={{ fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, color: accent, textTransform: 'uppercase' }}>{categoryLabel(course.category)}</span>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{course.code}</span>
            </div>
            <h3 style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary)', lineHeight: 1.3 }}>
              <Link to={detailPath} style={{ color: 'inherit', textDecoration: 'none' }}>{course.name}</Link>
            </h3>
            <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary)', lineHeight: 1.5 }}>{course.shortDescription}</p>
            {metaRow}
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 8, flexWrap: 'wrap', marginTop: 'auto' }}>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <span style={{ fontSize: 'var(--text-body-lg, 18px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>{formatPrice(price, currency)}</span>
                {originalPrice && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textDecoration: 'line-through' }}>{formatPrice(originalPrice, currency)}</span>}
                {seatRow}
              </div>
              {actionRow}
            </div>
          </div>
        </div>
      </Card>
    );
  }

  // grid + carousel share the same rich card body
  return (
    <Card variant="default" padding="none" as="article" aria-label={course.name} hoverable>
      <div style={{ position: 'relative' }}>
        <Link to={detailPath} aria-label={course.name} style={{ display: 'block' }}>
          <MediaPlaceholder label={course.name} icon="book-open" aspectRatio="16 / 9" caption={categoryLabel(course.category)} />
        </Link>
        <div style={{ position: 'absolute', top: 10, left: 10 }}>{badgesRow}</div>
        <div style={{ position: 'absolute', top: 10, right: 10, display: 'flex', gap: 4 }}>
          <button type="button" onClick={handleBookmark} aria-label={bookmarked ? 'Remove bookmark' : 'Bookmark'} style={iconBtnStyle(bookmarked ? 'var(--color-warning)' : 'var(--color-text-tertiary)')}>
            <Icon name="book" size={14} color="currentColor" />
          </button>
          <button type="button" onClick={handleWishlist} aria-label={wishlisted ? 'Remove from wishlist' : 'Add to wishlist'} style={iconBtnStyle(wishlisted ? 'var(--color-danger)' : 'var(--color-text-tertiary)')}>
            <Icon name="heart" size={14} color="currentColor" />
          </button>
          <button type="button" onClick={handleCompare} aria-pressed={compareSelected} aria-label={compareSelected ? 'Remove from comparison' : 'Add to comparison'} style={iconBtnStyle(compareSelected ? 'var(--color-bg-accent-default, #2F6F4F)' : 'var(--color-text-tertiary)')}>
            <Icon name="sliders" size={14} color="currentColor" />
          </button>
        </div>
      </div>
      <div style={{ padding: 'var(--space-4, 16px)', display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)', flex: 1 }}>
        <span style={{ fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, color: accent, textTransform: 'uppercase', letterSpacing: '0.04em' }}>
          {categoryLabel(course.category)} · {course.code}
        </span>
        <h3 style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary)', lineHeight: 1.3 }}>
          <Link to={detailPath} style={{ color: 'inherit', textDecoration: 'none' }}>{course.name}</Link>
        </h3>
        <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary)', lineHeight: 1.5, display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden', flex: 1 }}>
          {course.shortDescription}
        </p>
        {metaRow}
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 8, flexWrap: 'wrap' }}>
          <span style={{ fontSize: 'var(--text-body-lg, 18px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>{formatPrice(price, currency)}</span>
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4, fontSize: 'var(--text-body-xs, 12px)', color: 'var(--color-text-tertiary)' }}>
            <Icon name="star" size={13} color="var(--color-warning)" aria-label="Rating" /> {course.rating.toFixed(1)}
            <span style={{ color: 'var(--color-text-tertiary)' }}>· {dc.enrollmentCountPlaceholder} enrolled</span>
          </span>
        </div>
        {seatRow}
        <div style={{ marginTop: 'var(--space-1, 4px)' }}>{actionRow}</div>
      </div>
    </Card>
  );
});

function iconBtnStyle(color: string): React.CSSProperties {
  return {
    width: 28,
    height: 28,
    borderRadius: 'var(--radius-full, 999px)',
    border: 'none',
    background: 'rgba(255,255,255,0.92)',
    color,
    cursor: 'pointer',
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    boxShadow: '0 1px 3px rgba(0,0,0,0.12)',
  };
}

export default CourseCard;
