import { memo } from 'react';
import { useReceivingData } from '../hooks/useReceivingData';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { getStatusVariant } from '../utils';

export const PendingReceiptsPage = memo(function PendingReceiptsPage() {
  const { receipts, loading } = useReceivingData();
  const pending = receipts.filter((r) =>
    r.receiptStatus === 'pending' || r.receiptStatus === 'draft' ||
    r.receiptStatus === 'batch_pending' || r.receiptStatus === 'warehouse_allocation_pending'
  );

  if (loading) return <div>Loading...</div>;
  if (pending.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No pending receipts.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Pending Receipts ({pending.length})</h3>
      <div style={{ display: 'grid', gap: 12 }}>
        {pending.map((r) => (
          <div key={r.id} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16, display: 'flex', flexDirection: 'column', gap: 8 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 8 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontWeight: 700 }}>{r.receiptNumber}</span>
                <StatusBadge status={r.receiptStatus.replace(/_/g, ' ')} variant={getStatusVariant(r.receiptStatus)} />
              </div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{new Date(r.createdAt).toLocaleDateString()}</span>
            </div>
            <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap', fontSize: 'var(--text-body)' }}>
              <span><strong>Product:</strong> {r.product}</span>
              <span><strong>Supplier:</strong> {r.supplier}</span>
              <span><strong>Qty:</strong> {r.quantity.toLocaleString()}</span>
              <span><strong>Warehouse:</strong> {r.warehouse}</span>
            </div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
              {r.receiptStatus === 'batch_pending' && 'Awaiting batch assignment'}
              {r.receiptStatus === 'warehouse_allocation_pending' && 'Awaiting warehouse allocation'}
              {r.receiptStatus === 'pending' && 'Awaiting processing'}
              {r.receiptStatus === 'draft' && 'Not yet submitted'}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});
