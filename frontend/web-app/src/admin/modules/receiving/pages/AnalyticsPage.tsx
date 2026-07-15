import { memo } from 'react';
import { useReceivingData } from '../hooks/useReceivingData';

export const AnalyticsPage = memo(function AnalyticsPage() {
  const { analytics } = useReceivingData();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Receiving Analytics</h3>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 16 }}>
        {[
          { label: 'Total Receipts', value: analytics.totalReceipts.toLocaleString(), desc: 'All goods receipts' },
          { label: 'Pending', value: analytics.pendingReceipts.toLocaleString(), desc: 'Awaiting processing' },
          { label: 'Completed', value: analytics.completedReceipts.toLocaleString(), desc: 'Successfully completed' },
          { label: 'Rejected', value: analytics.rejectedReceipts.toLocaleString(), desc: 'Rejected receipts' },
          { label: 'Under Inspection', value: analytics.underInspection.toLocaleString(), desc: 'In quality inspection' },
          { label: 'Acceptance Rate', value: `${analytics.acceptanceRate}%`, desc: 'Percentage accepted' },
          { label: 'Rejection Rate', value: `${analytics.rejectionRate}%`, desc: 'Percentage rejected' },
          { label: 'Allocation Pending', value: analytics.warehousePending.toLocaleString(), desc: 'Awaiting warehouse assignment' },
        ].map((card) => (
          <div key={card.label} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 20, display: 'flex', flexDirection: 'column', gap: 4 }}>
            <span style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700, color: 'var(--color-text-primary)' }}>{card.value}</span>
            <span style={{ fontWeight: 600, fontSize: 'var(--text-body)' }}>{card.label}</span>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{card.desc}</span>
          </div>
        ))}
      </div>
    </div>
  );
});
