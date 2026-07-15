import { memo } from 'react';
import { useMovementData } from '../hooks/useMovementData';
import { StatusBadge } from '../../../components/status/StatusBadge';

export const MovementsPage = memo(function MovementsPage() {
  const { transactions, loading } = useMovementData();
  const grouped = transactions.reduce<Record<string, typeof transactions>>((acc, t) => {
    const key = t.movementType.replace(/_/g, ' ').replace(/\b\w/g, (c) => c.toUpperCase());
    (acc[key] ??= []).push(t);
    return acc;
  }, {});

  if (loading) return <div>Loading...</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Movements by Type</h3>
      {Object.entries(grouped).map(([type, items]) => (
        <section key={type} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
          <div style={{ padding: '12px 16px', borderBottom: '1px solid var(--color-border)', fontWeight: 600, fontSize: 'var(--text-body)' }}>{type} ({items.length})</div>
          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }}>
              <thead>
                <tr style={{ borderBottom: '1px solid var(--color-border)' }}>
                  <th style={{ padding: 10, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>Reference</th>
                  <th style={{ padding: 10, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>Product</th>
                  <th style={{ padding: 10, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>Warehouse</th>
                  <th style={{ padding: 10, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>Status</th>
                  <th style={{ padding: 10, textAlign: 'right', fontWeight: 600, color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>Qty</th>
                  <th style={{ padding: 10, textAlign: 'left', fontWeight: 600, color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>Date</th>
                </tr>
              </thead>
              <tbody>
                {items.slice(0, 10).map((t) => (
                  <tr key={t.id} style={{ borderBottom: '1px solid var(--color-border)' }}>
                    <td style={{ padding: 10, fontWeight: 500 }}>{t.referenceNumber}</td>
                    <td style={{ padding: 10 }}>{t.product}</td>
                    <td style={{ padding: 10 }}>{t.warehouse}</td>
                    <td style={{ padding: 10 }}><StatusBadge status={t.status.replace(/_/g, ' ')} variant={t.status === 'completed' ? 'success' : t.status === 'pending' ? 'warning' : 'info'} /></td>
                    <td style={{ padding: 10, textAlign: 'right' }}>{t.quantity.toLocaleString()}</td>
                    <td style={{ padding: 10, whiteSpace: 'nowrap' }}>{new Date(t.createdAt).toLocaleDateString()}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          {items.length > 10 && <div style={{ padding: '8px 16px', borderTop: '1px solid var(--color-border)', fontSize: 'var(--text-caption)', color: 'var(--color-primary)', cursor: 'pointer' }}>View all {items.length} records →</div>}
        </section>
      ))}
    </div>
  );
});
