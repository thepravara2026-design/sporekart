import { useAssignments } from '../state/AssignmentContext';
import { DashboardWidget } from '../components/DashboardWidget';
import { AssignmentDashboardSkeleton } from '../components/Skeletons';

export function AssignmentDashboardPage() {
  const { dashboardStats, assignments } = useAssignments();

  if (!dashboardStats) return <AssignmentDashboardSkeleton />;

  const activeCount = assignments.filter((a) => a.status === 'open' || a.status === 'in-progress').length;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Assignment Dashboard</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget label="Total Assignments" value={dashboardStats.totalAssignments} variant="default" subtitle="All time" />
        <DashboardWidget label="Active Assignments" value={activeCount} variant="info" subtitle="Open & in progress" />
        <DashboardWidget label="Draft" value={dashboardStats.draftAssignments} variant="default" subtitle="Not yet published" />
        <DashboardWidget label="Completed" value={dashboardStats.completedAssignments} variant="success" subtitle="Fully completed" />
        <DashboardWidget label="Pending Submissions" value={dashboardStats.pendingSubmissions} variant="warning" subtitle="Awaiting submission" />
        <DashboardWidget label="Late Submissions" value={dashboardStats.lateSubmissions} variant="danger" subtitle="Past due date" />
        <DashboardWidget label="Projects" value={dashboardStats.totalProjects} variant="info" subtitle="Active projects" />
        <DashboardWidget label="Practical Tasks" value={dashboardStats.practicalTasks} variant="default" subtitle="Lab & field work" />
      </div>

      {/* Course Wise */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Course Wise Assignments</h3>
          {dashboardStats.courseWise.map((c) => (
            <div key={c.course} style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span>{c.course}</span>
              <span style={{ fontWeight: 'var(--weight-medium)' }}>{c.count}</span>
            </div>
          ))}
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Batch Wise Assignments</h3>
          {dashboardStats.batchWise.map((b) => (
            <div key={b.batch} style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span>{b.batch}</span>
              <span style={{ fontWeight: 'var(--weight-medium)' }}>{b.count}</span>
            </div>
          ))}
        </div>
      </div>

      {/* Upcoming Deadlines */}
      <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
        <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Upcoming Deadlines</h3>
        {dashboardStats.upcomingDeadlines.length === 0 ? (
          <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', padding: '12px 0' }}>No upcoming deadlines.</div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
            {dashboardStats.upcomingDeadlines.map((d) => (
              <div key={d.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
                <div>
                  <span style={{ fontWeight: 'var(--weight-medium)' }}>{d.title}</span>
                  <span style={{ color: 'var(--color-text-tertiary)', marginLeft: 8 }}>{d.batchName}</span>
                </div>
                <span style={{ color: new Date(d.dueDate) < new Date() ? '#dc2626' : 'var(--color-text-secondary)' }}>{d.dueDate}</span>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
