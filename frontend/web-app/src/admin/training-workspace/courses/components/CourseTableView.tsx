import { memo, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Badge } from '../../../../design-system/components/display/Badge';
import type { Course } from '../data/courseMockData';
import {
  LIFECYCLE_LABELS, LIFECYCLE_VARIANTS, CATEGORY_LABELS, LEVEL_LABELS, DELIVERY_LABELS,
} from '../data/courseMockData';

interface CourseTableViewProps {
  courses: Course[];
  selectedIds: Set<string>;
  onToggleSelect: (id: string) => void;
  onTogglePin: (id: string) => void;
  onToggleFavorite: (id: string) => void;
}

export const CourseTableView = memo(function CourseTableView({
  courses, selectedIds, onToggleSelect,
}: CourseTableViewProps) {
  const navigate = useNavigate();

  const handleRowClick = useCallback((courseId: string) => {
    navigate(`/admin/training/courses/${courseId}`);
  }, [navigate]);

  return (
    <div style={{ overflowX: 'auto' }}>
      <table
        style={{
          width: '100%', borderCollapse: 'collapse',
          fontSize: 'var(--text-body-sm)',
        }}
        role="grid"
        aria-label="Courses table view"
      >
        <thead>
          <tr style={{
            borderBottom: '2px solid var(--color-border-default)',
            color: 'var(--color-text-tertiary)',
            fontSize: 'var(--text-caption)',
            textTransform: 'uppercase', letterSpacing: 'var(--tracking-wide)',
          }}>
            <th style={{ padding: 'var(--space-2) var(--space-3)', textAlign: 'left', width: 30 }}>
              <input
                type="checkbox"
                checked={courses.length > 0 && selectedIds.size === courses.length}
                onChange={() => {}}
                aria-label="Select all"
              />
            </th>
            <th style={{ padding: 'var(--space-2) var(--space-3)', textAlign: 'left' }}>Course</th>
            <th style={{ padding: 'var(--space-2) var(--space-3)', textAlign: 'left' }}>Code</th>
            <th style={{ padding: 'var(--space-2) var(--space-3)', textAlign: 'left' }}>Status</th>
            <th style={{ padding: 'var(--space-2) var(--space-3)', textAlign: 'left' }}>Category</th>
            <th style={{ padding: 'var(--space-2) var(--space-3)', textAlign: 'left' }}>Level</th>
            <th style={{ padding: 'var(--space-2) var(--space-3)', textAlign: 'left' }}>Mode</th>
            <th style={{ padding: 'var(--space-2) var(--space-3)', textAlign: 'left' }}>Duration</th>
            <th style={{ padding: 'var(--space-2) var(--space-3)', textAlign: 'left' }}>Students</th>
            <th style={{ padding: 'var(--space-2) var(--space-3)', textAlign: 'left' }}>Updated</th>
          </tr>
        </thead>
        <tbody>
          {courses.map((course) => (
            <tr
              key={course.id}
              onClick={() => handleRowClick(course.id)}
              onKeyDown={(e) => { if (e.key === 'Enter') handleRowClick(course.id); }}
              tabIndex={0}
              style={{
                borderBottom: '1px solid var(--color-border-default)',
                background: selectedIds.has(course.id) ? 'var(--color-bg-primary-subtle)' : 'transparent',
                cursor: 'pointer', transition: 'background var(--duration-fast) var(--easing-standard)',
              }}
              aria-label={`${course.name}`}
            >
              <td style={{ padding: 'var(--space-2) var(--space-3)' }}>
                <input
                  type="checkbox"
                  checked={selectedIds.has(course.id)}
                  onChange={() => onToggleSelect(course.id)}
                  onClick={(e) => e.stopPropagation()}
                  aria-label={`Select ${course.name}`}
                />
              </td>
              <td style={{
                padding: 'var(--space-2) var(--space-3)',
                fontWeight: 'var(--weight-semibold)',
                color: 'var(--color-text-primary)',
                maxWidth: 220, overflow: 'hidden', textOverflow: 'ellipsis',
                whiteSpace: 'nowrap',
              }}>
                {course.name}
              </td>
              <td style={{
                padding: 'var(--space-2) var(--space-3)',
                color: 'var(--color-text-secondary)',
                fontFamily: 'var(--font-family-mono)',
                fontSize: 'var(--text-caption)',
              }}>
                {course.code}
              </td>
              <td style={{ padding: 'var(--space-2) var(--space-3)' }}>
                <Badge size="sm" variant={LIFECYCLE_VARIANTS[course.lifecycle]}>
                  {LIFECYCLE_LABELS[course.lifecycle]}
                </Badge>
              </td>
              <td style={{ padding: 'var(--space-2) var(--space-3)', color: 'var(--color-text-secondary)' }}>
                {CATEGORY_LABELS[course.category]}
              </td>
              <td style={{ padding: 'var(--space-2) var(--space-3)', color: 'var(--color-text-secondary)' }}>
                {LEVEL_LABELS[course.level]}
              </td>
              <td style={{ padding: 'var(--space-2) var(--space-3)', color: 'var(--color-text-secondary)' }}>
                {DELIVERY_LABELS[course.deliveryMode]}
              </td>
              <td style={{ padding: 'var(--space-2) var(--space-3)', color: 'var(--color-text-secondary)' }}>
                {course.duration}
              </td>
              <td style={{ padding: 'var(--space-2) var(--space-3)', color: 'var(--color-text-secondary)' }}>
                {course.enrollmentCount || '-'}
              </td>
              <td style={{
                padding: 'var(--space-2) var(--space-3)',
                color: 'var(--color-text-tertiary)',
                fontSize: 'var(--text-caption)',
              }}>
                {course.updatedAt}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
});
