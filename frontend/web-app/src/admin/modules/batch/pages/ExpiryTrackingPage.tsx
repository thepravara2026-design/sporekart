import { memo, useState } from 'react';
import { SectionHeader } from '../../inventory/components';
import { useBatchData } from '../hooks/useBatchData';
import { ExpiryStatusBadge } from '../components/ExpiryStatusBadge';

export const ExpiryTrackingPage = memo(function ExpiryTrackingPage() {
  const { batches } = useBatchData();
  const [expiryFilter, setExpiryFilter] = useState('all');
  const filtered = expiryFilter === 'all' ? batches : batches.filter((b) => b.expiryStatus === expiryFilter);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <SectionHeader title="Expiry Tracking" description="Expiry monitoring and near-expiry alerts." />
      <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
        {['all', 'fresh', 'healthy', 'monitor', 'near_expiry', 'critical', 'expired'].map((f) => (
          <button key={f} onClick={() => setExpiryFilter(f)} style={{ padding: '6px 14px', borderRadius: 'var(--radius-badge)', border: 'none', background: expiryFilter === f ? 'var(--color-primary)' : 'var(--color-surface-hover)', color: expiryFilter === f ? '#fff' : 'var(--color-text-secondary)', cursor: 'pointer', fontSize: 'var(--text-caption)', fontWeight: expiryFilter === f ? 600 : 400 }}>{f.replace(/_/g, ' ')}</button>
        ))}
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 12 }}>
        {filtered.map((batch) => {
          const remaining = Math.ceil((new Date(batch.expiryDate).getTime() - Date.now()) / 86400000);
          return (
            <div key={batch.id} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
                <span style={{ fontWeight: 600, color: 'var(--color-text-primary)' }}>{batch.batchCode}</span>
                <ExpiryStatusBadge status={batch.expiryStatus} />
              </div>
              <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', display: 'flex', flexDirection: 'column', gap: 4 }}>
                <span>Product: {batch.product}</span>
                <span>Warehouse: {batch.warehouse}</span>
                <span>Production: {new Date(batch.productionDate).toLocaleDateString()}</span>
                <span>Expiry: {new Date(batch.expiryDate).toLocaleDateString()}</span>
                <span>Remaining: <strong style={{ color: remaining <= 30 ? 'var(--color-danger)' : remaining <= 60 ? 'var(--color-warning)' : 'var(--color-success)' }}>{remaining} days</strong></span>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
});

