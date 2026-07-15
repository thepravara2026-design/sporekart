import { memo } from 'react';
import { useMovementData } from '../hooks/useMovementData';

export const AnalyticsPage = memo(function AnalyticsPage() {
  const { analytics } = useMovementData();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Analytics</h3>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 16 }}>
        {[
          { label: 'Total Transactions', value: analytics.totalTransactions.toLocaleString(), desc: 'All movement transactions' },
          { label: 'Transfers', value: analytics.transfers.toLocaleString(), desc: 'Warehouse transfers' },
          { label: 'Receipts', value: analytics.receipts.toLocaleString(), desc: 'Goods receipts processed' },
          { label: 'Issues', value: analytics.issues.toLocaleString(), desc: 'Goods issues processed' },
          { label: 'Adjustments', value: analytics.adjustments.toLocaleString(), desc: 'Stock corrections' },
          { label: 'Pending', value: analytics.pending.toLocaleString(), desc: 'Awaiting approval' },
          { label: 'Completed', value: analytics.completed.toLocaleString(), desc: 'Successfully completed' },
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
