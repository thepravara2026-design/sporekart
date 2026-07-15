import { memo } from 'react';
import { useMovementData } from '../hooks/useMovementData';
import { StatusBadge } from '../../../components/status/StatusBadge';

export const AdjustmentsPage = memo(function AdjustmentsPage() {
  const { adjustments, loading } = useMovementData();

  if (loading) return <div>Loading...</div>;
  if (adjustments.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No stock adjustments recorded yet.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Stock Adjustments ({adjustments.length})</h3>
      <div style={{ overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)', background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
          <thead>
            <tr style={{ borderBottom: '1px solid var(--color-border)' }}>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Reference</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Product</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Warehouse</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Type</th>
              <th style={{ padding: 12, textAlign: 'right', fontWeight: 600 }}>Qty</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Reason</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Status</th>
            </tr>
          </thead>
          <tbody>
            {adjustments.map((a) => (
              <tr key={a.id} style={{ borderBottom: '1px solid var(--color-border)' }}>
                <td style={{ padding: 12, fontWeight: 500 }}>{a.adjustmentNumber}</td>
                <td style={{ padding: 12 }}>{a.product}</td>
                <td style={{ padding: 12 }}>{a.warehouse}</td>
                <td style={{ padding: 12 }}>{a.type}</td>
                <td style={{ padding: 12, textAlign: 'right', fontWeight: 600, color: a.type === 'negative' || a.type === 'damage' ? 'var(--color-danger)' : 'var(--color-success)' }}>
                  {a.type === 'negative' || a.type === 'damage' ? `-${a.quantity}` : `+${a.quantity}`}
                </td>
                <td style={{ padding: 12 }}>{a.reasonCode}</td>
                <td style={{ padding: 12 }}><StatusBadge status={a.status?.replace(/_/g, ' ') ?? 'pending'} variant={a.status === 'approved' ? 'success' : a.status === 'pending' ? 'warning' : 'info'} /></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});
