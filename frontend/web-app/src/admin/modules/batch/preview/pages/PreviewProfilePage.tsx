import { memo, useState } from 'react';
import type { BatchRecord } from '../../types';
import { useBatchData } from '../../hooks/useBatchData';
import { BatchStatusBadge } from '../../components/BatchStatusBadge';
import { ExpiryStatusBadge } from '../../components/ExpiryStatusBadge';
import { QualityStatusBadge } from '../../components/QualityStatusBadge';
import { BatchLifecycleTimeline } from '../../components/BatchLifecycleTimeline';
import { BatchTimelineComponent } from '../../components/BatchTimeline';

export const PreviewProfilePage = memo(function PreviewProfilePage() {
  const { batches, timeline } = useBatchData();
  const [selectedBatch, setSelectedBatch] = useState(batches[0]?.id ?? '');

  const batch = batches.find((b: BatchRecord) => b.id === selectedBatch);
  const batchTimeline = timeline.filter((e) => e.batchId === selectedBatch);

  if (!batch) return <div style={{ padding: 24, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No batch selected.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
        {batches.slice(0, 10).map((b: BatchRecord) => (
          <button key={b.id} onClick={() => setSelectedBatch(b.id)} style={{ padding: '6px 12px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: selectedBatch === b.id ? 'var(--color-primary)' : 'var(--color-surface)', color: selectedBatch === b.id ? '#fff' : 'var(--color-text-primary)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>{b.batchCode}</button>
        ))}
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>{batch.batchCode}</h3>
          <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
            <BatchStatusBadge status={batch.status} />
            <QualityStatusBadge status={batch.qualityStatus} />
            <ExpiryStatusBadge status={batch.expiryStatus} />
          </div>
          <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', display: 'flex', flexDirection: 'column', gap: 6 }}>
            <span>Product: {batch.product} ({batch.variant})</span>
            <span>SKU: {batch.sku}</span>
            <span>Warehouse: {batch.warehouse}</span>
            <span>Production: {new Date(batch.productionDate).toLocaleDateString()}</span>
            <span>Expiry: {new Date(batch.expiryDate).toLocaleDateString()}</span>
            <span>Shelf Life: {batch.shelfLife} {batch.shelfLifeUnit}</span>
            <span>Quantity: {batch.quantity.toLocaleString()}</span>
            <span>Lots: {batch.lotCount}</span>
            <span>Storage: {batch.storageConditions.temperature} / {batch.storageConditions.humidity}</span>
          </div>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Lifecycle Timeline</h3>
          <BatchLifecycleTimeline currentStatus={batch.status} />
        </div>
      </div>
      <div>
        <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Event Timeline</h3>
        <BatchTimelineComponent events={batchTimeline} />
      </div>
    </div>
  );
});
