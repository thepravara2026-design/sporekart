import { memo } from 'react';
import { useMovementData } from '../hooks/useMovementData';
import { StatusBadge } from '../../../components/status/StatusBadge';

export const GoodsIssuePage = memo(function GoodsIssuePage() {
  const { goodsIssues, loading } = useMovementData();

  if (loading) return <div>Loading...</div>;
  if (goodsIssues.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No goods issues recorded yet.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Goods Issue ({goodsIssues.length})</h3>
      <div style={{ display: 'grid', gap: 12 }}>
        {goodsIssues.map((gi) => (
          <div key={gi.id} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16, display: 'flex', flexDirection: 'column', gap: 8 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 8 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontWeight: 700 }}>{gi.issueNumber}</span>
                <StatusBadge status={gi.status?.replace(/_/g, ' ') ?? 'pending'} variant={gi.status === 'completed' ? 'success' : 'warning'} />
              </div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{new Date(gi.createdAt).toLocaleDateString()}</span>
            </div>
            <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap', fontSize: 'var(--text-body)' }}>
              <span><strong>Product:</strong> {gi.product}</span>
              <span><strong>Warehouse:</strong> {gi.warehouse}</span>
              <span><strong>Qty:</strong> {gi.quantity.toLocaleString()}</span>
              <span><strong>Reason:</strong> {gi.reason}</span>
            </div>
            {gi.destination && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Destination: {gi.destination}</span>}
          </div>
        ))}
      </div>
    </div>
  );
});
