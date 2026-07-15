import { memo } from 'react';
import { useReceivingData } from '../hooks/useReceivingData';
import { StatusBadge } from '../../../components/status/StatusBadge';

export const AcceptancePage = memo(function AcceptancePage() {
  const { receipts, loading } = useReceivingData();
  const accepted = receipts.filter((r) => r.receiptStatus === 'approved' || r.receiptStatus === 'completed');

  if (loading) return <div>Loading...</div>;
  if (accepted.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No accepted receipts.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Acceptance ({accepted.length})</h3>
      <div style={{ overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)', background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
          <thead>
            <tr style={{ borderBottom: '1px solid var(--color-border)' }}>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Receipt</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Product</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Status</th>
              <th style={{ padding: 12, textAlign: 'right', fontWeight: 600 }}>Qty</th>
              <th style={{ padding: 12, textAlign: 'right', fontWeight: 600 }}>Accepted</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Acceptance Status</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Allocation</th>
            </tr>
          </thead>
          <tbody>
            {accepted.map((r) => (
              <tr key={r.id} style={{ borderBottom: '1px solid var(--color-border)' }}>
                <td style={{ padding: 12, fontWeight: 500 }}>{r.receiptNumber}</td>
                <td style={{ padding: 12 }}>{r.product}</td>
                <td style={{ padding: 12 }}><StatusBadge status={r.receiptStatus.replace(/_/g, ' ')} variant="success" /></td>
                <td style={{ padding: 12, textAlign: 'right' }}>{r.quantity.toLocaleString()}</td>
                <td style={{ padding: 12, textAlign: 'right', color: 'var(--color-success)', fontWeight: 600 }}>{r.acceptedQty.toLocaleString()}</td>
                <td style={{ padding: 12, fontSize: 'var(--text-caption)' }}>{r.acceptanceStatus.replace(/_/g, ' ')}</td>
                <td style={{ padding: 12, fontSize: 'var(--text-caption)' }}>{r.allocationStatus === 'allocated' ? '\u2713 Allocated' : r.allocationStatus.replace(/_/g, ' ')}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});
