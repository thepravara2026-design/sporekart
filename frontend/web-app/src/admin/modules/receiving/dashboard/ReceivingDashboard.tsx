import { useReceivingData } from '../hooks/useReceivingData';
import { ReceivingSummaryCards } from '../components/ReceivingSummaryCards';
import { ReceivingTimeline } from '../components/ReceivingTimeline';
import type { ReceivingMetric } from '../types';

export function ReceivingDashboard() {
  const { receipts, timeline } = useReceivingData();

  const metrics: ReceivingMetric[] = [
    { label: 'Pending Receipts', value: receipts.filter((r) => r.receiptStatus === 'pending' || r.receiptStatus === 'draft').length, variant: 'warning', trend: 'up' },
    { label: 'Completed', value: receipts.filter((r) => r.receiptStatus === 'completed').length, variant: 'success', trend: 'up' },
    { label: 'Under Inspection', value: receipts.filter((r) => r.receiptStatus === 'inspection').length, variant: 'info', trend: 'neutral' },
    { label: 'Rejected', value: receipts.filter((r) => r.receiptStatus === 'rejected').length, variant: 'danger', trend: 'down' },
    { label: 'Allocation Pending', value: receipts.filter((r) => r.allocationStatus === 'pending' || r.allocationStatus === 'allocating').length, variant: 'warning', trend: 'neutral' },
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <ReceivingSummaryCards metrics={metrics} />
      <section>
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h4, 16px)', fontWeight: 600 }}>Recent Activity</h3>
        <ReceivingTimeline events={timeline.slice(0, 10)} />
      </section>
    </div>
  );
}
