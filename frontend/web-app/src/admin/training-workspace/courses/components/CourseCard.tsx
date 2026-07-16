import { memo, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card } from '../../../../design-system/components/composite/Card';
import { Badge } from '../../../../design-system/components/display/Badge';
import { Icon } from '../../../../design-system/icons/Icon';
import type { Course } from '../data/courseMockData';
import {
  LIFECYCLE_LABELS,
  LIFECYCLE_VARIANTS,
  CATEGORY_COLORS,
  DELIVERY_LABELS,
} from '../data/courseMockData';

interface CourseCardProps {
  course: Course;
  selected?: boolean;
  onToggleSelect?: (id: string) => void;
  onTogglePin?: (id: string) => void;
  onToggleFavorite?: (id: string) => void;
  onPreview?: (id: string) => void;
}

export const CourseCard = memo(function CourseCard({
  course,
  selected = false,
  onToggleSelect,
  onTogglePin,
  onToggleFavorite,
}: CourseCardProps) {
  const navigate = useNavigate();

  const handleClick = useCallback(() => {
    navigate(`/admin/training/courses/${course.id}`);
  }, [navigate, course.id]);

  const handleKeyDown = useCallback(
    (e: React.KeyboardEvent) => {
      if (e.key === 'Enter') handleClick();
    },
    [handleClick]
  );

  return (
    <Card
      variant={selected ? 'elevated' : 'default'}
      padding="none"
      hoverable
      as="div"
      aria-label={`${course.name} — ${LIFECYCLE_LABELS[course.lifecycle]}`}
    >
      <div
        role="button"
        tabIndex={0}
        onClick={handleClick}
        onKeyDown={handleKeyDown}
        style={{ cursor: 'pointer', display: 'flex', flexDirection: 'column', height: '100%' }}
      >
        <div
          style={{
            height: 120,
            background: `linear-gradient(135deg, ${CATEGORY_COLORS[course.category]}40, ${CATEGORY_COLORS[course.category]}20)`,
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            position: 'relative', borderBottom: '1px solid var(--color-border-default)',
          }}
        >
          <Icon name="book-open" size={36} color={CATEGORY_COLORS[course.category]} />
          <div style={{
            position: 'absolute', top: 8, right: 8, display: 'flex', gap: 4,
          }}>
            {course.pinned && (
              <span style={{
                width: 24, height: 24, borderRadius: 'var(--radius-full)',
                background: 'var(--color-bg-surface-default)',
                display: 'flex', alignItems: 'center', justifyContent: 'center',
                boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
              }}>
                <Icon name="star" size={12} color="var(--color-warning)" aria-label="Pinned" />
              </span>
            )}
          </div>
          {onToggleSelect && (
            <div style={{ position: 'absolute', top: 8, left: 8 }}>
              <input
                type="checkbox"
                checked={selected}
                onChange={() => onToggleSelect(course.id)}
                onClick={(e) => e.stopPropagation()}
                aria-label={`Select ${course.name}`}
                style={{ width: 16, height: 16, cursor: 'pointer' }}
              />
            </div>
          )}
          <div style={{
            position: 'absolute', bottom: -1, left: 0, right: 0,
            padding: '4px 12px',
            background: 'linear-gradient(transparent, var(--color-bg-surface-default))',
          }}>
            <span style={{
              fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-bold)',
              color: 'var(--color-text-primary)',
            }}>
              {course.code}
            </span>
          </div>
        </div>
        <div style={{
          padding: 'var(--space-3)', flex: 1,
          display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)',
        }}>
          <h3 style={{
            margin: 0, fontSize: 'var(--text-body-sm)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-primary)',
            lineHeight: 'var(--leading-tight)',
            display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical',
            overflow: 'hidden',
          }}>
            {course.name}
          </h3>
          <p style={{
            margin: 0, fontSize: 'var(--text-caption)',
            color: 'var(--color-text-secondary)',
            lineHeight: 'var(--leading-normal)',
            display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical',
            overflow: 'hidden', flex: 1,
          }}>
            {course.shortDescription}
          </p>
          <div style={{
            display: 'flex', flexWrap: 'wrap', gap: 4, marginTop: 'auto',
          }}>
            <Badge size="sm" variant={LIFECYCLE_VARIANTS[course.lifecycle]}>
              {LIFECYCLE_LABELS[course.lifecycle]}
            </Badge>
            <Badge size="sm" variant="default">
              {DELIVERY_LABELS[course.deliveryMode]}
            </Badge>
          </div>
          <div style={{
            display: 'flex', justifyContent: 'space-between', alignItems: 'center',
            fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)',
            paddingTop: 'var(--space-stack-xs)',
            borderTop: '1px solid var(--color-border-default)',
          }}>
            <span>{course.duration}</span>
            <div style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
              {course.enrollmentCount > 0 && (
                <>
                  <Icon name="users" size={10} color="currentColor" />
                  <span>{course.enrollmentCount}</span>
                </>
              )}
            </div>
          </div>
        </div>
      </div>
      <div style={{
        display: 'flex', borderTop: '1px solid var(--color-border-default)',
      }}>
        {onTogglePin && (
          <button
            type="button"
            onClick={(e) => { e.stopPropagation(); onTogglePin(course.id); }}
            style={{
              flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center',
              gap: 4, padding: '4px', background: 'none', border: 'none',
              cursor: 'pointer', fontSize: 'var(--text-caption)',
              color: course.pinned ? 'var(--color-warning)' : 'var(--color-text-tertiary)',
            }}
            aria-label={course.pinned ? 'Unpin course' : 'Pin course'}
          >
            <Icon name="star" size={12} color="currentColor" />
            <span>Pin</span>
          </button>
        )}
        {onToggleFavorite && (
          <button
            type="button"
            onClick={(e) => { e.stopPropagation(); onToggleFavorite(course.id); }}
            style={{
              flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center',
              gap: 4, padding: '4px', background: 'none', border: 'none',
              borderLeft: '1px solid var(--color-border-default)',
              cursor: 'pointer', fontSize: 'var(--text-caption)',
              color: course.favorite ? 'var(--color-danger)' : 'var(--color-text-tertiary)',
            }}
            aria-label={course.favorite ? 'Remove from favorites' : 'Add to favorites'}
          >
            <Icon name="heart" size={12} color="currentColor" />
            <span>Save</span>
          </button>
        )}
      </div>
    </Card>
  );
});
