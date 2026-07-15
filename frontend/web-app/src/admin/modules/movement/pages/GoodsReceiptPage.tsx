import { memo } from 'react';
import { useMovementData } from '../hooks/useMovementData';
import { StatusBadge } from '../../../components/status/StatusBadge';

export const GoodsReceiptPage = memo(function GoodsReceiptPage() {
  const { goodsReceipts, loading } = useMovementData();

  if (loading) return <div>Loading...</div>;
  if (goodsReceipts.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No goods receipts recorded yet.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Goods Receipt ({goodsReceipts.length})</h3>
      <div style={{ display: 'grid', gap: 12 }}>
        {goodsReceipts.map((gr) => (
          <div key={gr.id} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16, display: 'flex', flexDirection: 'column', gap: 8 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 8 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontWeight: 700 }}>{gr.receiptNumber}</span>
                <StatusBadge status={gr.status?.replace(/_/g, ' ') ?? 'pending'} variant={gr.status === 'completed' ? 'success' : 'warning'} />
              </div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{new Date(gr.createdAt).toLocaleDateString()}</span>
            </div>
            <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap', fontSize: 'var(--text-body)' }}>
              <span><strong>Product:</strong> {gr.product}</span>
              <span><strong>Warehouse:</strong> {gr.warehouse}</span>
              <span><strong>Accepted:</strong> {gr.acceptedQty.toLocaleString()}</span>
              {gr.rejectedQty > 0 && <span><strong>Rejected:</strong> {gr.rejectedQty.toLocaleString()}</span>}
            </div>
            {gr.supplier && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Supplier: {gr.supplier}</span>}
          </div>
        ))}
      </div>
    </div>
  );
});
