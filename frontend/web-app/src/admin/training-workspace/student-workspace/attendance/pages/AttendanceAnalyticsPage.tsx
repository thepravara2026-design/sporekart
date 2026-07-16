import { useAttendance } from '../state/AttendanceContext';
import { AnalyticsPanel } from '../components/AnalyticsPanel';
import { AttendanceDashboardSkeleton } from '../components/Skeletons';

export function AttendanceAnalyticsPage() {
  const { summary, dashboardStats } = useAttendance();

  if (!summary) return <AttendanceDashboardSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Attendance Analytics</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Comprehensive attendance metrics and trends
        </p>
      </div>

      {dashboardStats.lowAttendanceAlerts.length > 0 && (
        <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0', color: '#dc2626' }}>Low Attendance Monitoring</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 8 }}>
            {dashboardStats.lowAttendanceAlerts.map((alert) => (
              <div key={alert.studentId} style={{ padding: 8, borderRadius: 'var(--radius-sm)', border: '1px solid #fecaca', background: '#fef2f2' }}>
                <div style={{ fontWeight: 'var(--weight-medium)', fontSize: 'var(--text-body-sm)' }}>{alert.studentName}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{alert.courseName} — {alert.batchName}</div>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 4, fontSize: 'var(--text-caption)' }}>
                  <span>Attendance: <strong style={{ color: '#dc2626' }}>{alert.attendancePercent}%</strong></span>
                  <span>Threshold: {alert.threshold}%</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      <AnalyticsPanel summary={summary} />
    </div>
  );
}
