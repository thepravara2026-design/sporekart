import { memo, useState } from 'react';
import { SectionHeader } from '../../../inventory/components';
import { StatusBadge } from '../../../../components/status/StatusBadge';
import { LifecycleTimeline } from '../../components/LifecycleTimeline';
import { ClassificationBadge } from '../../components/ClassificationBadge';
import { getItems } from '../../services/inventoryItemMockService';
import { getStatusVariant, getLifecycleVariant } from '../../utils';

export const PreviewProfilePage = memo(function PreviewProfilePage() {
  const [items] = useState(() => getItems());
  const item = items[0];

  if (!item) return null;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title={item.name} description={`Item profile — ${item.code}`} />

      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8, alignItems: 'center' }}>
        <StatusBadge status={item.status} variant={getStatusVariant(item.status)} />
        <span style={{ padding: '2px 8px', borderRadius: 'var(--radius-badge)', fontSize: 'var(--text-caption)', background: 'var(--color-surface-hover)', color: 'var(--color-text-secondary)' }}>
          {getLifecycleVariant(item.lifecycle) === 'success' ? '✓' : '○'} {item.lifecycle.replace(/_/g, ' ')}
        </span>
        <ClassificationBadge type={item.classification.type} />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: 12, padding: 20, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)' }}>
        <DetailField label="Item Code" value={item.code} />
        <DetailField label="SKU" value={item.sku} />
        <DetailField label="Product" value={item.productName} />
        <DetailField label="Variant" value={item.variantName} />
        <DetailField label="Category" value={item.category} />
        <DetailField label="Brand" value={item.brand} />
        <DetailField label="Unit" value={item.unit} />
        <DetailField label="Grade" value={item.classification.grade.replace(/_/g, ' ')} />
        <DetailField label="Created" value={item.createdAt} />
        <DetailField label="Updated" value={item.updatedAt} />
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Lifecycle Timeline</h3>
        <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)' }}>
          <LifecycleTimeline events={item.lifecycleEvents} />
        </div>
      </div>
    </div>
  );
});

function DetailField({ label, value }: { label: string; value: string }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.04em', fontWeight: 600 }}>{label}</span>
      <span style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)', fontWeight: 500 }}>{value}</span>
    </div>
  );
}
