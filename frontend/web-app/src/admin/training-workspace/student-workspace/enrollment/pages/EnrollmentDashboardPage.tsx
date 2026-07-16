import { useMemo } from 'react';
import { useEnrollment } from '../state/EnrollmentContext';
import { DashboardWidget } from '../components/DashboardWidget';
import { EnrollmentTable } from '../components/EnrollmentTable';
import { EnrollmentDashboardSkeleton } from '../components/Skeletons';
import { EmptyState } from '../components/EmptyStates';
import { ADMISSION_PIPELINE } from '../types';

export function EnrollmentDashboardPage() {
  const { dashboardStats, requests, batches, selectRequest, currentRequest, isLoading } = useEnrollment();

  const stats = dashboardStats;

  const recentEnrollments = useMemo(() => {
    return [...requests].sort((a, b) => b.applicationDate.localeCompare(a.applicationDate)).slice(0, 5);
  }, [requests]);

  const activeBatches = useMemo(() => {
    return batches.filter((b) => b.status === 'active' || b.status === 'filling');
  }, [batches]);

  if (isLoading) return <EnrollmentDashboardSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Enrollment Dashboard</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Enterprise admission & enrollment overview
        </p>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget label="Applications Received" value={stats.totalApplications} icon="📋" variant="info" subtitle="Total applications" />
        <DashboardWidget label="Pending Approval" value={stats.pendingApproval} icon="⏳" variant="warning" subtitle="Awaiting review" />
        <DashboardWidget label="Approved" value={stats.approved} icon="✅" variant="success" subtitle="Applications approved" />
        <DashboardWidget label="Rejected" value={stats.rejected} icon="❌" variant="danger" subtitle="Applications rejected" />
        <DashboardWidget label="Available Seats" value={stats.availableSeats} icon="💺" variant={stats.availableSeats > 20 ? 'success' : 'warning'} subtitle="Across all batches" />
        <DashboardWidget label="Active Batches" value={stats.activeBatches} icon="📦" variant="info" subtitle="Currently active" />
        <DashboardWidget label="Capacity Utilization" value={`${stats.capacityUtilization}%`} icon="📊" variant={stats.capacityUtilization > 85 ? 'warning' : 'success'} subtitle="Across all batches" />
        <DashboardWidget label="Enrolled Students" value={stats.enrolledStudents} icon="🎓" variant="success" subtitle="Completed enrollment" />
      </div>

      <div className="admission-pipeline" style={{
        padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
        border: '1px solid var(--color-border-default)',
        background: 'var(--color-bg-surface-default)',
      }}>
        <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>
          Admission Pipeline
        </h3>
        <div style={{ display: 'flex', gap: 4, overflowX: 'auto', paddingBottom: 8 }} role="list" aria-label="Admission pipeline stages">
          {ADMISSION_PIPELINE.map((stage, i) => (
            <div key={stage.id} style={{ display: 'flex', alignItems: 'center', gap: 4, flexShrink: 0 }}>
              <div style={{
                padding: '4px 10px', borderRadius: 'var(--radius-full)',
                background: i < 6 ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-skeleton-base)',
                color: i < 6 ? 'var(--color-primary)' : 'var(--color-text-tertiary)',
                fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
                whiteSpace: 'nowrap',
              }}>
                {stage.label}
              </div>
              {i < ADMISSION_PIPELINE.length - 1 && (
                <span style={{ color: 'var(--color-border-default)', fontSize: 12 }}>→</span>
              )}
            </div>
          ))}
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: 'var(--space-component-gap)', alignItems: 'start' }}>
        <div style={{
          padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
          border: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-surface-default)',
        }}>
          <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>
            Recent Enrollments
          </h3>
          {recentEnrollments.length === 0 ? (
            <EmptyState type="noApplications" />
          ) : (
            <EnrollmentTable requests={recentEnrollments} onSelect={selectRequest} selectedId={currentRequest?.id ?? null} />
          )}
        </div>

        <div style={{
          padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
          border: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-surface-default)',
        }}>
          <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>
            Active Batches
          </h3>
          {activeBatches.length === 0 ? (
            <EmptyState type="noActiveBatches" />
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
              {activeBatches.map((b) => (
                <div key={b.id} style={{
                  display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                  padding: '8px 0', borderBottom: '1px solid var(--color-border-subtle)',
                }}>
                  <div>
                    <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-medium)' }}>{b.batchName}</div>
                    <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{b.filledSeats}/{b.capacity} filled</div>
                  </div>
                  <div style={{
                    padding: '2px 8px', borderRadius: 'var(--radius-full)', fontSize: 'var(--text-caption)',
                    background: b.availableSeats > 0 ? '#f0fdf4' : '#fef2f2',
                    color: b.availableSeats > 0 ? '#16a34a' : '#dc2626',
                  }}>
                    {b.availableSeats} left
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
