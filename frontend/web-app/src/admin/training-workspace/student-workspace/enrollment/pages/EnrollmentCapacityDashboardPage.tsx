import { useMemo } from 'react';
import { useEnrollment } from '../state/EnrollmentContext';
import { CapacityGauge } from '../components/CapacityGauge';
import { DashboardWidget } from '../components/DashboardWidget';
import { EmptyState } from '../components/EmptyStates';
import { EnrollmentDashboardSkeleton } from '../components/Skeletons';

export function EnrollmentCapacityDashboardPage() {
  const { capacityInfo, dashboardStats, batches, isLoading } = useEnrollment();

  const sortedByUtilization = useMemo(() => {
    return [...capacityInfo].sort((a, b) => b.utilizationPercent - a.utilizationPercent);
  }, [capacityInfo]);

  const batchUtilization = useMemo(() => {
    if (batches.length === 0) return { low: 0, medium: 0, high: 0, full: 0 };
    return batches.reduce(
      (acc, b) => {
        const pct = b.filledSeats / b.capacity;
        if (pct >= 1) acc.full++;
        else if (pct >= 0.75) acc.high++;
        else if (pct >= 0.5) acc.medium++;
        else acc.low++;
        return acc;
      },
      { low: 0, medium: 0, high: 0, full: 0 },
    );
  }, [batches]);

  if (isLoading) return <EnrollmentDashboardSkeleton />;

  if (capacityInfo.length === 0) return <EmptyState type="noCapacity" />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Capacity Dashboard</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Batch capacity and seat utilization overview
        </p>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget label="Total Capacity" value={capacityInfo.reduce((s, c) => s + c.maxCapacity, 0)} icon="📊" variant="info" />
        <DashboardWidget label="Utilization" value={`${dashboardStats.capacityUtilization}%`} icon="📈" variant={dashboardStats.capacityUtilization > 85 ? 'warning' : 'success'} />
        <DashboardWidget label="Available Seats" value={capacityInfo.reduce((s, c) => s + c.availableSeats, 0)} icon="💺" variant="success" />
        <DashboardWidget label="Occupied Seats" value={capacityInfo.reduce((s, c) => s + c.occupiedSeats, 0)} icon="👥" variant="default" />
      </div>

      <div style={{
        padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
        border: '1px solid var(--color-border-default)',
        background: 'var(--color-bg-surface-default)',
      }}>
        <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>
          Batch Utilization Summary
        </h3>
        <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap' }}>
          {[
            { label: 'Low (<50%)', count: batchUtilization.low, color: '#16a34a' },
            { label: 'Medium (50-74%)', count: batchUtilization.medium, color: '#2563eb' },
            { label: 'High (75-99%)', count: batchUtilization.high, color: '#ca8a04' },
            { label: 'Full (100%)', count: batchUtilization.full, color: '#dc2626' },
          ].map((item) => (
            <div key={item.label} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <div style={{ width: 12, height: 12, borderRadius: '50%', background: item.color }} />
              <span style={{ fontSize: 'var(--text-body-sm)' }}>{item.label}: <strong>{item.count}</strong></span>
            </div>
          ))}
        </div>
      </div>

      <div style={{
        display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))',
        gap: 'var(--space-component-gap)',
      }}>
        {sortedByUtilization.map((c) => (
          <CapacityGauge key={c.batchId} capacity={c} />
        ))}
      </div>
    </div>
  );
}
