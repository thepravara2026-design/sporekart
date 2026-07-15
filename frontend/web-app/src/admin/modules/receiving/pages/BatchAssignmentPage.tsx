import { memo } from 'react';
import { useReceivingData } from '../hooks/useReceivingData';
import { StatusBadge } from '../../../components/status/StatusBadge';

export const BatchAssignmentPage = memo(function BatchAssignmentPage() {
  const { batchAssignments, loading } = useReceivingData();

  if (loading) return <div>Loading...</div>;
  if (batchAssignments.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No batch assignments found.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Batch Assignment ({batchAssignments.length})</h3>
      <div style={{ display: 'grid', gap: 12 }}>
        {batchAssignments.map((ba) => (
          <div key={ba.id} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16, display: 'flex', flexDirection: 'column', gap: 8 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 8 }}>
              <span style={{ fontWeight: 700, fontSize: 'var(--text-body)' }}>{ba.batchCode}</span>
              <StatusBadge status={ba.qualityStatus.replace(/_/g, ' ')} variant={ba.qualityStatus === 'approved' ? 'success' : 'warning'} />
            </div>
            <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
              <span><strong>Lot:</strong> {ba.lotCode}</span>
              <span><strong>Expiry:</strong> {new Date(ba.expiryDate).toLocaleDateString()}</span>
              <span><strong>Shelf Life:</strong> {ba.shelfLife}</span>
            </div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
              Receipt: {ba.receiptId} · Assigned {new Date(ba.assignedAt).toLocaleDateString()}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});
