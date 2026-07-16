import { memo, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Badge } from '../../../../design-system/components/display/Badge';
import { Icon } from '../../../../design-system/icons/Icon';
import type { Course } from '../data/courseMockData';
import {
  LIFECYCLE_LABELS, LIFECYCLE_VARIANTS, CATEGORY_LABELS, LEVEL_LABELS, DELIVERY_LABELS,
} from '../data/courseMockData';

interface CourseListViewProps {
  courses: Course[];
  selectedIds: Set<string>;
  onToggleSelect: (id: string) => void;
  onTogglePin: (id: string) => void;
  onToggleFavorite: (id: string) => void;
}

export const CourseListView = memo(function CourseListView({
  courses, selectedIds, onToggleSelect, onTogglePin, onToggleFavorite,
}: CourseListViewProps) {
  const navigate = useNavigate();

  const handleRowClick = useCallback((courseId: string) => {
    navigate(`/admin/training/courses/${courseId}`);
  }, [navigate]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }} role="list" aria-label="Courses list view">
      {courses.map((course) => (
        <div
          key={course.id}
          role="listitem"
          style={{
            display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)',
            padding: 'var(--space-2) var(--space-3)',
            background: selectedIds.has(course.id) ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-surface-default)',
            border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-sm)',
            cursor: 'pointer', transition: 'background var(--duration-fast) var(--easing-standard)',
          }}
          onClick={() => handleRowClick(course.id)}
          onKeyDown={(e) => { if (e.key === 'Enter') handleRowClick(course.id); }}
          tabIndex={0}
          aria-label={`${course.name} — ${LIFECYCLE_LABELS[course.lifecycle]}`}
        >
          <input
            type="checkbox"
            checked={selectedIds.has(course.id)}
            onChange={() => onToggleSelect(course.id)}
            onClick={(e) => e.stopPropagation()}
            aria-label={`Select ${course.name}`}
          />
          <div style={{
            width: 40, height: 40, borderRadius: 'var(--radius-sm)',
            background: 'var(--color-bg-primary-subtle)',
            display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
          }}>
            <Icon name="book-open" size={18} color="var(--color-primary)" />
          </div>
          <div style={{ flex: 1, minWidth: 0, display: 'flex', flexDirection: 'column', gap: 2 }}>
            <div style={{
              fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)',
              color: 'var(--color-text-primary)',
              whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis',
            }}>
              {course.name}
            </div>
            <div style={{
              fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
              display: 'flex', alignItems: 'center', gap: 8, flexWrap: 'wrap',
            }}>
              <span>{course.code}</span>
              <span>{CATEGORY_LABELS[course.category]}</span>
              <span>{LEVEL_LABELS[course.level]}</span>
              <span>{DELIVERY_LABELS[course.deliveryMode]}</span>
            </div>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 4, flexShrink: 0 }}>
            {course.pinned && <Icon name="star" size={12} color="var(--color-warning)" />}
            {course.favorite && <Icon name="heart" size={12} color="var(--color-danger)" />}
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 4, flexShrink: 0 }}>
            <Badge size="sm" variant={LIFECYCLE_VARIANTS[course.lifecycle]}>
              {LIFECYCLE_LABELS[course.lifecycle]}
            </Badge>
          </div>
          <div style={{
            fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)',
            flexShrink: 0, textAlign: 'right', minWidth: 80,
          }}>
            {course.duration}
          </div>
          <div style={{ display: 'flex', gap: 2, flexShrink: 0 }}>
            <button
              type="button"
              onClick={(e) => { e.stopPropagation(); onTogglePin(course.id); }}
              style={{
                padding: 4, background: 'none', border: 'none', cursor: 'pointer',
                color: course.pinned ? 'var(--color-warning)' : 'var(--color-text-tertiary)',
              }}
              aria-label={course.pinned ? 'Unpin' : 'Pin'}
            >
              <Icon name="star" size={14} color="currentColor" />
            </button>
            <button
              type="button"
              onClick={(e) => { e.stopPropagation(); onToggleFavorite(course.id); }}
              style={{
                padding: 4, background: 'none', border: 'none', cursor: 'pointer',
                color: course.favorite ? 'var(--color-danger)' : 'var(--color-text-tertiary)',
              }}
              aria-label={course.favorite ? 'Unsave' : 'Save'}
            >
              <Icon name="heart" size={14} color="currentColor" />
            </button>
          </div>
        </div>
      ))}
    </div>
  );
});
