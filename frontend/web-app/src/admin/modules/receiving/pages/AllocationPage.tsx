import { memo } from 'react';
import { useReceivingData } from '../hooks/useReceivingData';
import { StatusBadge } from '../../../components/status/StatusBadge';

export const AllocationPage = memo(function AllocationPage() {
  const { allocations, loading } = useReceivingData();

  if (loading) return <div>Loading...</div>;
  if (allocations.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No warehouse allocations found.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Warehouse Allocation ({allocations.length})</h3>
      <div style={{ overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)', background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
          <thead>
            <tr style={{ borderBottom: '1px solid var(--color-border)' }}>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Receipt</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Warehouse</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Zone</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Rack</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Shelf</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Bin</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Storage Type</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Status</th>
            </tr>
          </thead>
          <tbody>
            {allocations.map((a) => (
              <tr key={a.id} style={{ borderBottom: '1px solid var(--color-border)' }}>
                <td style={{ padding: 12, fontWeight: 500, fontSize: 'var(--text-caption)' }}>{a.receiptId}</td>
                <td style={{ padding: 12 }}>{a.warehouse}</td>
                <td style={{ padding: 12, fontSize: 'var(--text-caption)' }}>{a.zone}</td>
                <td style={{ padding: 12, fontSize: 'var(--text-caption)' }}>{a.rack}</td>
                <td style={{ padding: 12, fontSize: 'var(--text-caption)' }}>{a.shelf}</td>
                <td style={{ padding: 12, fontSize: 'var(--text-caption)' }}>{a.bin}</td>
                <td style={{ padding: 12, fontSize: 'var(--text-caption)' }}>{a.storageType.replace(/_/g, ' ')}</td>
                <td style={{ padding: 12 }}><StatusBadge status={a.status.replace(/_/g, ' ')} variant={a.status === 'allocated' ? 'success' : a.status === 'failed' ? 'danger' : 'warning'} /></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});
