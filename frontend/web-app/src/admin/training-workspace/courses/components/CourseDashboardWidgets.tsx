import { memo } from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Icon } from '../../../../design-system/icons/Icon';
import { Badge } from '../../../../design-system/components/display/Badge';
import type { CourseDashboardStats, TrainingCategory } from '../data/courseMockData';
import { CATEGORY_LABELS, CATEGORY_COLORS } from '../data/courseMockData';

interface CourseDashboardWidgetsProps {
  stats: CourseDashboardStats;
}

export const CourseDashboardWidgets = memo(function CourseDashboardWidgets({
  stats,
}: CourseDashboardWidgetsProps) {
  const widgets = [
    { label: 'Total Courses', value: stats.totalCourses, icon: 'book-open', color: '#3b82f6' },
    { label: 'Published', value: stats.publishedCourses, icon: 'check-circle', color: '#22c55e', badge: 'success' as const },
    { label: 'Drafts', value: stats.draftCourses, icon: 'edit', color: '#f59e0b', badge: 'warning' as const },
    { label: 'Archived', value: stats.archivedCourses, icon: 'archive', color: '#6b7280' },
    { label: 'Pending Review', value: stats.pendingReview, icon: 'clock', color: '#a855f7', badge: 'info' as const },
    { label: 'Upcoming Launches', value: stats.upcomingLaunches, icon: 'calendar', color: '#14b8a6' },
  ];

  return (
    <Card variant="default" padding="md" as="div">
      <h3 style={{
        margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h4)',
        fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)',
      }}>
        Course Overview
      </h3>
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))',
        gap: 'var(--space-inline-sm)',
      }}>
        {widgets.map((w) => (
          <div key={w.label} style={{
            padding: 'var(--space-3)',
            background: 'var(--color-bg-background)',
            borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
          }}>
            <div style={{
              display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between',
              marginBottom: 'var(--space-stack-xs)',
            }}>
              <span style={{
                fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
                textTransform: 'uppercase', letterSpacing: 'var(--tracking-wide)',
              }}>
                {w.label}
              </span>
              <Icon name={w.icon} size={18} color={w.color} />
            </div>
            <div style={{
              fontSize: 'var(--text-h4)', fontWeight: 'var(--weight-bold)',
              color: 'var(--color-text-primary)',
            }}>
              {w.value}
            </div>
            {w.badge && w.value > 0 && (
              <div style={{ marginTop: 4 }}>
                <Badge size="sm" variant={w.badge}>{w.value} need attention</Badge>
              </div>
            )}
          </div>
        ))}
      </div>

      {stats.popularCategories.length > 0 && (
        <>
          <h4 style={{
            margin: 'var(--space-stack-sm) 0 var(--space-stack-xs)',
            fontSize: 'var(--text-body-sm)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-primary)',
          }}>
            Popular Categories
          </h4>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
            {stats.popularCategories.map((cat) => (
              <div
                key={cat.category}
                style={{
                  display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)',
                  padding: '2px 0',
                }}
              >
                <span style={{
                  width: 8, height: 8, borderRadius: 'var(--radius-full)',
                  background: CATEGORY_COLORS[cat.category as TrainingCategory] || 'var(--color-text-tertiary)',
                  flexShrink: 0,
                }} />
                <span style={{
                  flex: 1, fontSize: 'var(--text-body-sm)',
                  color: 'var(--color-text-primary)',
                }}>
                  {CATEGORY_LABELS[cat.category as TrainingCategory] || cat.category}
                </span>
                <span style={{
                  fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
                  fontWeight: 'var(--weight-medium)',
                }}>
                  {cat.count}
                </span>
              </div>
            ))}
          </div>
        </>
      )}
    </Card>
  );
});
