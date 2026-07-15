import { memo, useState } from 'react';
import { SectionHeader } from '../../inventory/components';
import { useBatchData } from '../hooks/useBatchData';
import { QualityStatusBadge } from '../components/QualityStatusBadge';
import { QUALITY_STATUSES } from '../constants';

export const QualityStatusPage = memo(function QualityStatusPage() {
  const { batches } = useBatchData();
  const [filter, setFilter] = useState('all');
  const filtered = filter === 'all' ? batches : batches.filter((b) => b.qualityStatus === filter);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <SectionHeader title="Quality Status" description="Quality inspections and status tracking." />
      <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
        {['all', ...QUALITY_STATUSES.map((q) => q.value)].map((f) => (
          <button key={f} onClick={() => setFilter(f)} style={{ padding: '6px 14px', borderRadius: 'var(--radius-badge)', border: 'none', background: filter === f ? 'var(--color-primary)' : 'var(--color-surface-hover)', color: filter === f ? '#fff' : 'var(--color-text-secondary)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>{f === 'all' ? 'All' : f.replace(/_/g, ' ')}</button>
        ))}
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 12 }}>
        {filtered.map((batch) => (
          <div key={batch.id} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
              <span style={{ fontWeight: 600, color: 'var(--color-text-primary)' }}>{batch.batchCode}</span>
              <QualityStatusBadge status={batch.qualityStatus} />
            </div>
            <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', display: 'flex', flexDirection: 'column', gap: 4 }}>
              <span>Product: {batch.product}</span>
              <span>Warehouse: {batch.warehouse}</span>
              <span>Lifecycle: {batch.status.replace(/_/g, ' ')}</span>
              <span>Expiry: {batch.expiryStatus.replace(/_/g, ' ')}</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});

