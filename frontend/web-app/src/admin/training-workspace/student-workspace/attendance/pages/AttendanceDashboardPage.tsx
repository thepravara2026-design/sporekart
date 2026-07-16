import { useAttendance } from '../state/AttendanceContext';
import { DashboardWidget } from '../components/DashboardWidget';
import { AttendanceDashboardSkeleton } from '../components/Skeletons';
import { AnalyticsPanel } from '../components/AnalyticsPanel';

export function AttendanceDashboardPage() {
  const { dashboardStats, summary } = useAttendance();

  if (!dashboardStats) return <AttendanceDashboardSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Attendance Dashboard</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Enterprise attendance & learning presence overview
        </p>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget label="Today's Sessions" value={dashboardStats.todaySessions} icon="📅" variant="info" subtitle="Scheduled today" />
        <DashboardWidget label="Students Present" value={dashboardStats.studentsPresent} icon="✅" variant="success" subtitle="Marked present" />
        <DashboardWidget label="Students Absent" value={dashboardStats.studentsAbsent} icon="❌" variant="danger" subtitle="Marked absent" />
        <DashboardWidget label="Late Arrivals" value={dashboardStats.lateArrivals} icon="⏰" variant="warning" subtitle="Arrived late" />
        <DashboardWidget label="Half Day" value={dashboardStats.halfDay} icon="🌗" variant="warning" subtitle="Half-day marked" />
        <DashboardWidget label="Overall Attendance" value={`${dashboardStats.overallPercent}%`} icon="📊" variant={dashboardStats.overallPercent >= 75 ? 'success' : 'danger'} subtitle="Across all batches" />
      </div>

      {dashboardStats.lowAttendanceAlerts.length > 0 && (
        <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid #dc2626', background: '#fef2f2' }}>
          <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0', color: '#dc2626' }}>
            Low Attendance Alerts
          </h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
            {dashboardStats.lowAttendanceAlerts.map((alert) => (
              <div key={alert.studentId} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: 'var(--text-body-sm)' }}>
                <span>{alert.studentName} — {alert.batchName}</span>
                <span style={{ fontWeight: 'var(--weight-semibold)', color: '#dc2626' }}>{alert.attendancePercent}% (threshold: {alert.threshold}%)</span>
              </div>
            ))}
          </div>
        </div>
      )}

      <AnalyticsPanel summary={summary} />
    </div>
  );
}
