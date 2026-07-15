import { memo, useMemo } from 'react';
import type { BatchRecord } from '../types';
import { useBatchData } from '../hooks/useBatchData';
import { BatchStatusBadge } from '../components/BatchStatusBadge';
import { ExpiryStatusBadge } from '../components/ExpiryStatusBadge';
import { QualityStatusBadge } from '../components/QualityStatusBadge';
import { BatchLifecycleTimeline } from '../components/BatchLifecycleTimeline';
import { BatchTimelineComponent } from '../components/BatchTimeline';
import { useBatchWorkspace } from '../contexts/BatchWorkspaceContext';

export const BatchProfilePage = memo(function BatchProfilePage() {
  const { batches, timeline } = useBatchData();
  const { profileBatchId, setActiveSection } = useBatchWorkspace();
  const batch = batches.find((b: BatchRecord) => b.id === profileBatchId) ?? batches[0];
  const batchTimeline = useMemo(() => timeline.filter((e) => e.batchId === batch?.id), [timeline, batch?.id]);

  if (!batch) {
    return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No batch selected. <button onClick={() => setActiveSection('registry')} style={{ padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)' }}>Back to Registry</button></div>;
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
        <button onClick={() => setActiveSection('registry')} style={{ padding: '6px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)', fontSize: 'var(--text-caption)' }}>&larr; Back to Registry</button>
        <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Batch Profile: {batch.batchCode}</h2>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>General Information</h3>
          <div style={{ display: 'flex', gap: 8, marginBottom: 12, flexWrap: 'wrap' }}>
            <BatchStatusBadge status={batch.status} />
            <QualityStatusBadge status={batch.qualityStatus} />
            <ExpiryStatusBadge status={batch.expiryStatus} />
          </div>
          <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', display: 'flex', flexDirection: 'column', gap: 6 }}>
            <span><strong>Batch Code:</strong> {batch.batchCode}</span>
            <span><strong>Inventory Item:</strong> {batch.inventoryItemId}</span>
            <span><strong>Product:</strong> {batch.product} ({batch.variant})</span>
            <span><strong>SKU:</strong> {batch.sku}</span>
            <span><strong>Warehouse:</strong> {batch.warehouse}</span>
            <span><strong>Quantity:</strong> {batch.quantity.toLocaleString()}</span>
            <span><strong>Lots:</strong> {batch.lotCount}</span>
            <span><strong>Created By:</strong> {batch.createdBy}</span>
            <span><strong>Created:</strong> {new Date(batch.createdAt).toLocaleString()}</span>
            <span><strong>Updated:</strong> {new Date(batch.updatedAt).toLocaleString()}</span>
          </div>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Production & Expiry</h3>
          <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', display: 'flex', flexDirection: 'column', gap: 6 }}>
            <span><strong>Production Date:</strong> {new Date(batch.productionDate).toLocaleDateString()}</span>
            <span><strong>Expiry Date:</strong> {new Date(batch.expiryDate).toLocaleDateString()}</span>
            {batch.bestBeforeDate && <span><strong>Best Before:</strong> {new Date(batch.bestBeforeDate).toLocaleDateString()}</span>}
            {batch.manufacturingDate && <span><strong>Manufacturing Date:</strong> {new Date(batch.manufacturingDate).toLocaleDateString()}</span>}
            {batch.receivedDate && <span><strong>Received Date:</strong> {new Date(batch.receivedDate).toLocaleDateString()}</span>}
            <span><strong>Shelf Life:</strong> {batch.shelfLife} {batch.shelfLifeUnit}</span>
            <span><strong>Storage:</strong> {batch.storageConditions.temperature} / {batch.storageConditions.humidity}</span>
            <span><strong>Expiry Status:</strong> {batch.expiryStatus.replace(/_/g, ' ')}</span>
          </div>
        </div>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Quality Information</h3>
          <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', display: 'flex', flexDirection: 'column', gap: 6 }}>
            <span><strong>Quality Status:</strong> {batch.qualityStatus.replace(/_/g, ' ')}</span>
            <span><strong>Lifecycle Status:</strong> {batch.status.replace(/_/g, ' ')}</span>
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
