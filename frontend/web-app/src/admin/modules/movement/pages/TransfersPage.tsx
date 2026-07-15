import { memo } from 'react';
import { useMovementData } from '../hooks/useMovementData';
import { StatusBadge } from '../../../components/status/StatusBadge';

export const TransfersPage = memo(function TransfersPage() {
  const { transfers, loading } = useMovementData();

  if (loading) return <div>Loading...</div>;
  if (transfers.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No transfers recorded yet.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Warehouse Transfers ({transfers.length})</h3>
      <div style={{ display: 'grid', gap: 12 }}>
        {transfers.map((transfer) => (
          <div key={transfer.id} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16, display: 'flex', flexDirection: 'column', gap: 8 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 8 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontWeight: 700, fontSize: 'var(--text-body)' }}>{transfer.transferNumber}</span>
                <StatusBadge status={transfer.status?.replace(/_/g, ' ') ?? 'pending'} variant={transfer.status === 'completed' ? 'success' : 'warning'} />
              </div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{new Date(transfer.createdAt).toLocaleDateString()}</span>
            </div>
            <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', alignItems: 'center', fontSize: 'var(--text-body)' }}>
              <span style={{ fontWeight: 500 }}>{transfer.product}</span>
              <span style={{ color: 'var(--color-text-tertiary)' }}>× {transfer.quantity.toLocaleString()}</span>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
              <span>{transfer.sourceWarehouse}</span>
              <span style={{ color: 'var(--color-text-tertiary)' }}>→</span>
              <span>{transfer.destinationWarehouse}</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});
