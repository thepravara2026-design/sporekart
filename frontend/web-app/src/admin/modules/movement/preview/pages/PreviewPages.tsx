import { memo } from 'react';
import { MovementDashboard } from '../../dashboard/MovementDashboard';
import { TransactionTable } from '../../components/TransactionTable';
import { useMovementData } from '../../hooks/useMovementData';
import { MovementTimelineComponent } from '../../components/MovementTimeline';

export const PreviewDashboardPage = memo(function PreviewDashboardPage() {
  return <MovementDashboard />;
});

export const PreviewTransactionsPage = memo(function PreviewTransactionsPage() {
  const { transactions, loading } = useMovementData();
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 16px)', fontWeight: 600 }}>Transactions ({transactions.length})</h3>
      <TransactionTable transactions={transactions} loading={loading} />
    </div>
  );
});

export const PreviewTransfersPage = memo(function PreviewTransfersPage() {
  const { transfers, loading } = useMovementData();
  if (loading) return <div>Loading...</div>;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 16px)', fontWeight: 600 }}>Transfers ({transfers.length})</h3>
      <div style={{ display: 'grid', gap: 8 }}>
        {transfers.slice(0, 6).map((t) => (
          <div key={t.id} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 14 }}>
            <div style={{ fontWeight: 700, fontSize: 'var(--text-body)' }}>{t.transferNumber}</div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{t.product} × {t.quantity.toLocaleString()}</div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{t.sourceWarehouse} → {t.destinationWarehouse}</div>
          </div>
        ))}
      </div>
    </div>
  );
});

export const PreviewTimelinePage = memo(function PreviewTimelinePage() {
  const { timeline } = useMovementData();
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 16px)', fontWeight: 600 }}>Movement Timeline</h3>
      <MovementTimelineComponent events={timeline.slice(0, 8)} />
    </div>
  );
});

export const PreviewAuditPage = memo(function PreviewAuditPage() {
  const { auditRecords } = useMovementData();
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 16px)', fontWeight: 600 }}>Audit Trail ({auditRecords.length})</h3>
      <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }}>
          <thead>
            <tr style={{ borderBottom: '1px solid var(--color-border)' }}>
              <th style={{ padding: 10, textAlign: 'left', fontWeight: 600, fontSize: 'var(--text-caption)' }}>Time</th>
              <th style={{ padding: 10, textAlign: 'left', fontWeight: 600, fontSize: 'var(--text-caption)' }}>User</th>
              <th style={{ padding: 10, textAlign: 'left', fontWeight: 600, fontSize: 'var(--text-caption)' }}>Action</th>
              <th style={{ padding: 10, textAlign: 'left', fontWeight: 600, fontSize: 'var(--text-caption)' }}>Field</th>
            </tr>
          </thead>
          <tbody>
            {auditRecords.slice(0, 10).map((r) => (
              <tr key={r.id} style={{ borderBottom: '1px solid var(--color-border)' }}>
                <td style={{ padding: 10, whiteSpace: 'nowrap', fontSize: 'var(--text-caption)' }}>{new Date(r.timestamp).toLocaleString()}</td>
                <td style={{ padding: 10, fontWeight: 500 }}>{r.user}</td>
                <td style={{ padding: 10 }}>{r.action}</td>
                <td style={{ padding: 10, fontSize: 'var(--text-caption)' }}>{r.field}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});

export const PreviewAnalyticsPage = memo(function PreviewAnalyticsPage() {
  const { analytics } = useMovementData();
  const cards = [
    { label: 'Total Transactions', value: analytics.totalTransactions },
    { label: 'Transfers', value: analytics.transfers },
    { label: 'Receipts', value: analytics.receipts },
    { label: 'Issues', value: analytics.issues },
    { label: 'Adjustments', value: analytics.adjustments },
    { label: 'Pending', value: analytics.pending },
    { label: 'Completed', value: analytics.completed },
  ];
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 16px)', fontWeight: 600 }}>Analytics</h3>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 12 }}>
        {cards.map((c) => (
          <div key={c.label} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
            <div style={{ fontSize: 'var(--text-h2, 22px)', fontWeight: 700 }}>{c.value?.toLocaleString() ?? '0'}</div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{c.label}</div>
          </div>
        ))}
      </div>
    </div>
  );
});
