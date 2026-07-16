import { memo } from 'react';
import { Link } from 'react-router-dom';
import { Icon } from '../../../design-system/icons/Icon';
import { Badge } from '../../../design-system/components/display/Badge';
import {
  categoryLabel,
  deliveryLabel,
  levelLabel,
  type DiscoveryCourse,
} from '../data/discoveryMockData';

export interface CourseComparisonTableProps {
  courses: DiscoveryCourse[];
  onRemove: (id: string) => void;
  onClear: () => void;
}

const ROWS: { key: string; label: string; render: (dc: DiscoveryCourse) => React.ReactNode }[] = [
  { key: 'category', label: 'Category', render: (dc) => categoryLabel(dc.course.category) },
  { key: 'level', label: 'Difficulty', render: (dc) => levelLabel(dc.course.level) },
  { key: 'duration', label: 'Duration', render: (dc) => dc.course.duration },
  { key: 'language', label: 'Language', render: (dc) => dc.course.language },
  { key: 'delivery', label: 'Delivery Mode', render: (dc) => deliveryLabel(dc.course.deliveryMode) },
  { key: 'modules', label: 'Modules', render: (dc) => `${dc.course.moduleCount} modules` },
  { key: 'certification', label: 'Certification', render: () => <Badge size="sm" variant="success">Included</Badge> },
  { key: 'seats', label: 'Seat Availability', render: (dc) => `${dc.availableSeats} / ${dc.totalSeats}` },
  { key: 'rating', label: 'Rating', render: (dc) => `${dc.course.rating.toFixed(1)} ★` },
  { key: 'completion', label: 'Completion Rate', render: (dc) => `${(dc.course.rating * 18).toFixed(0)}%` },
  { key: 'price', label: 'Price', render: (dc) => (dc.price === 0 ? 'Free' : `₹${dc.price.toLocaleString('en-IN')}`) },
];

export const CourseComparisonTable = memo(function CourseComparisonTable({
  courses,
  onRemove,
  onClear,
}: CourseComparisonTableProps) {
  if (courses.length === 0) return null;

  return (
    <div style={{ overflowX: 'auto' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 'var(--space-3, 12px)' }}>
        <h2 style={{ margin: 0, fontSize: 'var(--text-h3, 22px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>Compare courses</h2>
        <button type="button" onClick={onClear} style={{ background: 'none', border: 'none', color: 'var(--color-danger)', cursor: 'pointer', fontWeight: 600, fontSize: 'var(--text-body-sm)' }}>
          Clear all
        </button>
      </div>
      <table style={{ width: '100%', borderCollapse: 'collapse', minWidth: 640 }}>
        <thead>
          <tr>
            <th style={thStyle}>Feature</th>
            {courses.map((dc) => (
              <th key={dc.course.id} style={{ ...thStyle, textAlign: 'left' }}>
                <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 8 }}>
                  <Link to={`/training/courses/${dc.course.slug}`} style={{ color: 'inherit', textDecoration: 'none', fontWeight: 700 }}>{dc.course.name}</Link>
                  <button type="button" onClick={() => onRemove(dc.course.id)} aria-label={`Remove ${dc.course.name} from comparison`} style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--color-text-tertiary)' }}>
                    <Icon name="x" size={14} color="currentColor" />
                  </button>
                </div>
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {ROWS.map((row) => (
            <tr key={row.key} style={{ borderTop: '1px solid var(--color-border-default)' }}>
              <td style={{ ...tdStyle, fontWeight: 600, color: 'var(--color-text-secondary)' }}>{row.label}</td>
              {courses.map((dc) => (
                <td key={dc.course.id} style={{ ...tdStyle, color: 'var(--color-text-primary)' }}>{row.render(dc)}</td>
              ))}
            </tr>
          ))}
          <tr style={{ borderTop: '1px solid var(--color-border-default)' }}>
            <td style={tdStyle} />
            {courses.map((dc) => (
              <td key={dc.course.id} style={tdStyle}>
                <Link to={`/training/courses/${dc.course.slug}`} style={{ display: 'inline-flex', alignItems: 'center', gap: 4, padding: '6px 12px', borderRadius: 'var(--radius-pill)', background: 'var(--color-bg-accent-default, #2F6F4F)', color: '#fff', fontWeight: 700, fontSize: 'var(--text-body-sm)', textDecoration: 'none' }}>
                  View <Icon name="arrow-right" size={14} aria-label="View" />
                </Link>
              </td>
            ))}
          </tr>
        </tbody>
      </table>
    </div>
  );
});

const thStyle: React.CSSProperties = {
  padding: 'var(--space-3, 12px)',
  verticalAlign: 'top',
  borderBottom: '2px solid var(--color-border-default)',
  fontSize: 'var(--text-body-sm)',
};

const tdStyle: React.CSSProperties = { padding: 'var(--space-3, 12px)', fontSize: 'var(--text-body-sm)', verticalAlign: 'top' };
