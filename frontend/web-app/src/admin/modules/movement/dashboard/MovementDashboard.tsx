import { useMovementData } from '../hooks/useMovementData';
import { MovementSummaryCards } from '../components/MovementSummaryCards';
import { MovementTimelineComponent } from '../components/MovementTimeline';
import type { MovementMetric } from '../types';

export function MovementDashboard() {
  const { transactions, timeline } = useMovementData();

  const metrics: MovementMetric[] = [
    { label: 'Total Transactions', value: transactions.length, variant: 'info', trend: 'up' },
    { label: 'Pending Actions', value: transactions.filter((t) => t.status === 'pending').length, variant: 'warning', trend: 'neutral' },
    { label: 'Completed', value: transactions.filter((t) => t.status === 'completed').length, variant: 'success', trend: 'up' },
    { label: 'Transfers', value: transactions.filter((t) => t.movementType === 'warehouse_transfer').length, variant: 'neutral', trend: 'up' },
    { label: 'Adjustments', value: transactions.filter((t) => ['stock_adjustment', 'damage', 'cycle_count_adjustment', 'manual_adjustment'].includes(t.movementType)).length, variant: 'neutral', trend: 'neutral' },
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <MovementSummaryCards metrics={metrics} />
      <section>
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h4, 16px)', fontWeight: 600 }}>Recent Activity</h3>
        <MovementTimelineComponent events={timeline.slice(0, 10)} />
      </section>
    </div>
  );
}
