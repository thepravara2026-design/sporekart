import { memo } from 'react';
import { useReceivingData } from '../hooks/useReceivingData';
import { StatusBadge } from '../../../components/status/StatusBadge';

export const RejectionPage = memo(function RejectionPage() {
  const { receipts, loading } = useReceivingData();
  const rejected = receipts.filter((r) => r.receiptStatus === 'rejected');

  if (loading) return <div>Loading...</div>;
  if (rejected.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No rejected receipts.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Rejection ({rejected.length})</h3>
      <div style={{ display: 'grid', gap: 12 }}>
        {rejected.map((r) => (
          <div key={r.id} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-danger)', padding: 16, display: 'flex', flexDirection: 'column', gap: 8 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 8 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontWeight: 700 }}>{r.receiptNumber}</span>
                <StatusBadge status="Rejected" variant="danger" />
              </div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{new Date(r.createdAt).toLocaleDateString()}</span>
            </div>
            <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap', fontSize: 'var(--text-body)' }}>
              <span><strong>Product:</strong> {r.product}</span>
              <span><strong>Supplier:</strong> {r.supplier}</span>
              <span><strong>Qty:</strong> {r.quantity.toLocaleString()}</span>
            </div>
            <div style={{ background: 'var(--color-bg)', padding: '8px 12px', borderRadius: 'var(--radius-sm)', fontSize: 'var(--text-caption)', color: 'var(--color-danger)' }}>
              <strong>Reason:</strong> {r.rejectionReason.replace(/_/g, ' ')}
            </div>
            <div style={{ display: 'flex', gap: 6 }}>
              <button style={{ padding: '4px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Return to Supplier</button>
              <button style={{ padding: '4px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Dispose</button>
              <button style={{ padding: '4px 12px', border: 'none', borderRadius: 'var(--radius-sm)', background: 'var(--color-primary)', color: '#fff', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Re-inspect</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});
