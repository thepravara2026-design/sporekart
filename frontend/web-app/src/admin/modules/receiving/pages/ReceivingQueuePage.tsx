import { memo } from 'react';
import { useReceivingData } from '../hooks/useReceivingData';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { getStatusVariant } from '../utils';

export const ReceivingQueuePage = memo(function ReceivingQueuePage() {
  const { receipts, loading } = useReceivingData();
  const queue = receipts.filter((r) => r.receiptStatus === 'pending' || r.receiptStatus === 'receiving');

  if (loading) return <div>Loading...</div>;
  if (queue.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>Receiving queue is empty.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Receiving Queue ({queue.length})</h3>
      <div style={{ display: 'grid', gap: 12 }}>
        {queue.map((receipt) => (
          <div key={receipt.id} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16, display: 'flex', flexDirection: 'column', gap: 8 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 8 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontWeight: 700 }}>{receipt.receiptNumber}</span>
                <StatusBadge status={receipt.receiptStatus.replace(/_/g, ' ')} variant={getStatusVariant(receipt.receiptStatus)} />
              </div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{new Date(receipt.createdAt).toLocaleDateString()}</span>
            </div>
            <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap', fontSize: 'var(--text-body)' }}>
              <span><strong>Product:</strong> {receipt.product}</span>
              <span><strong>Supplier:</strong> {receipt.supplier}</span>
              <span><strong>Qty:</strong> {receipt.quantity.toLocaleString()}</span>
              <span><strong>Warehouse:</strong> {receipt.warehouse}</span>
            </div>
            <div style={{ display: 'flex', gap: 6 }}>
              <button style={{ padding: '4px 12px', border: 'none', borderRadius: 'var(--radius-sm)', background: 'var(--color-primary)', color: '#fff', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Start Receiving</button>
              <button style={{ padding: '4px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Inspect</button>
              <button style={{ padding: '4px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Skip</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});
