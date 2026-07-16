import { memo, useMemo } from 'react';
import { useStudentWorkspace } from '../state/WorkspaceContext';
import { Card } from '../../../../design-system/components/composite/Card';
import { Icon } from '../../../../design-system/icons/Icon';
import { DashboardSkeleton } from '../components/StudentSkeletons';
import StudentQuickActions from '../components/StudentQuickActions';
import StudentEmptyState from '../components/StudentEmptyStates';
import { StudentCompactCard } from '../components/StudentCard';
import { STUDENT_CATEGORIES } from '../types';
import { Badge } from '../../../../design-system/components/display/Badge';

interface StatWidgetProps {
  label: string;
  value: number;
  icon: string;
  color: string;
}

const StatWidget = memo(function StatWidget({ label, value, icon: iconName, color }: StatWidgetProps) {
  return (
    <Card padding="md" variant="outlined">
      <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontWeight: 'var(--weight-medium)' }}>
            {label}
          </span>
          <span style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', lineHeight: 1.2 }}>
            {value}
          </span>
        </div>
        <span style={{
          width: 36, height: 36, borderRadius: 'var(--radius-md)',
          background: `${color}15`, color,
          display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
        }}>
          <Icon name={iconName} size={16} color="currentColor" />
        </span>
      </div>
    </Card>
  );
});

const StudentDashboardPage = memo(function StudentDashboardPage() {
  const { stats, students, loading } = useStudentWorkspace();

  const newStudents = useMemo(() => {
    const thirtyDaysAgo = new Date(Date.now() - 30 * 24 * 60 * 60 * 1000);
    return students.filter((s) => new Date(s.registrationDate) >= thirtyDaysAgo);
  }, [students]);

  const overviewStats = useMemo(() => [
    { label: 'Total Students', value: stats.totalStudents, icon: 'users', color: 'var(--color-primary)' },
    { label: 'Active Students', value: stats.activeStudents, icon: 'user-check', color: 'var(--color-success)' },
    { label: 'Pending Approvals', value: stats.pendingApprovals, icon: 'clock', color: 'var(--color-warning)' },
    { label: 'New Registrations', value: stats.newRegistrations, icon: 'user-plus', color: 'var(--color-info)' },
    { label: 'Inactive Students', value: stats.inactiveStudents, icon: 'user-minus', color: 'var(--color-neutral-500)' },
    { label: 'Archived', value: stats.archivedStudents, icon: 'archive', color: 'var(--color-text-tertiary)' },
  ], [stats]);

  const categoryData = useMemo(() =>
    STUDENT_CATEGORIES.map((cat) => ({
      ...cat,
      count: students.filter((s) => s.category === cat.label).length,
    })),
  [students]
  );

  if (loading) {
    return <DashboardSkeleton />;
  }

  if (students.length === 0) {
    return <StudentEmptyState type="noStudents" />;
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>
          Student Dashboard
        </h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Overview of the student registry and recent activity
        </p>
      </div>

      <StudentQuickActions />

      <section aria-label="Student statistics">
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))',
          gap: 'var(--space-component-gap)',
        }}>
          {overviewStats.map((stat) => (
            <StatWidget key={stat.label} {...stat} />
          ))}
        </div>
      </section>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-section-gap)' }}>
        <Card padding="md">
          <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>
            Students by Course
          </h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {stats.studentsByCourse.map((item) => (
              <div key={item.course} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 'var(--text-caption)', flex: 1, color: 'var(--color-text-secondary)', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                  {item.course}
                </span>
                <div style={{
                  flex: 2, height: 8, borderRadius: 'var(--radius-full)',
                  background: 'var(--color-bg-skeleton-base)', overflow: 'hidden',
                }}>
                  <div style={{
                    width: `${stats.totalStudents > 0 ? (item.count / stats.totalStudents) * 100 : 0}%`,
                    height: '100%',
                    background: 'var(--color-primary)',
                    borderRadius: 'var(--radius-full)',
                    transition: 'width var(--duration-normal) var(--easing-standard)',
                  }} />
                </div>
                <span style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', minWidth: 24, textAlign: 'right' }}>
                  {item.count}
                </span>
              </div>
            ))}
          </div>
        </Card>

        <Card padding="md">
          <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>
            Students by Language
          </h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {stats.studentsByLanguage.map((item) => (
              <div key={item.language} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 'var(--text-caption)', flex: 1, color: 'var(--color-text-secondary)' }}>
                  {item.language}
                </span>
                <div style={{
                  flex: 2, height: 8, borderRadius: 'var(--radius-full)',
                  background: 'var(--color-bg-skeleton-base)', overflow: 'hidden',
                }}>
                  <div style={{
                    width: `${stats.totalStudents > 0 ? (item.count / stats.totalStudents) * 100 : 0}%`,
                    height: '100%',
                    background: 'var(--color-success)',
                    borderRadius: 'var(--radius-full)',
                    transition: 'width var(--duration-normal) var(--easing-standard)',
                  }} />
                </div>
                <span style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', minWidth: 24, textAlign: 'right' }}>
                  {item.count}
                </span>
              </div>
            ))}
          </div>
        </Card>
      </div>

      <Card padding="md">
        <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>
          Student Categories
        </h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
          {categoryData.map((cat) => (
            <div
              key={cat.id}
              style={{
                display: 'flex', alignItems: 'center', gap: 10,
                padding: 'var(--space-3)',
                background: 'var(--color-bg-surface-subtle)',
                borderRadius: 'var(--radius-md)',
              }}
            >
              <span style={{
                width: 32, height: 32, borderRadius: 'var(--radius-md)',
                background: `${cat.color}15`, color: cat.color,
                display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
              }}>
                <Icon name={cat.icon} size={14} color="currentColor" />
              </span>
              <div>
                <div style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)' }}>{cat.label}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{cat.count} students</div>
              </div>
            </div>
          ))}
        </div>
      </Card>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-section-gap)' }}>
        <Card padding="md">
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 12 }}>
            <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>
              Recently Registered
            </h3>
            <Badge variant="info" size="sm">{newStudents.length}</Badge>
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
            {newStudents.slice(0, 5).map((student) => (
              <StudentCompactCard key={student.id} student={student} />
            ))}
            {newStudents.length === 0 && (
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', padding: 8 }}>
                No recent registrations
              </span>
            )}
          </div>
        </Card>

        <Card padding="md">
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 12 }}>
            <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>
              Students by Learning Mode
            </h3>
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {stats.studentsByLearningMode.map((item) => (
              <div key={item.mode} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 'var(--text-caption)', flex: 1, color: 'var(--color-text-secondary)', textTransform: 'capitalize' }}>
                  {item.mode}
                </span>
                <div style={{
                  flex: 2, height: 8, borderRadius: 'var(--radius-full)',
                  background: 'var(--color-bg-skeleton-base)', overflow: 'hidden',
                }}>
                  <div style={{
                    width: `${stats.totalStudents > 0 ? (item.count / stats.totalStudents) * 100 : 0}%`,
                    height: '100%',
                    background: 'var(--color-info)',
                    borderRadius: 'var(--radius-full)',
                    transition: 'width var(--duration-normal) var(--easing-standard)',
                  }} />
                </div>
                <span style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', minWidth: 24, textAlign: 'right' }}>
                  {item.count}
                </span>
              </div>
            ))}
          </div>
        </Card>
      </div>
    </div>
  );
});

export default StudentDashboardPage;
