import { memo, useState } from 'react';
import { SectionHeader } from '../../inventory/components';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { Icon } from '../../../../design-system/icons/Icon';
import { getProductMappings } from '../services/inventoryItemMockService';
import { getStatusVariant } from '../utils';

export const ProductsMappingPage = memo(function ProductsMappingPage() {
  const [mappings] = useState(() => getProductMappings());

  if (mappings.length === 0) {
    return <SectionHeader title="Product Mapping" description="Map products from the product catalog to inventory items." />;
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Product Mapping" description="Every product from the catalog is mapped to one or more inventory items. Use this view to review, link, and manage product-to-inventory relationships." />

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {mappings.map((m) => (
          <div key={m.id} style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 20, background: 'var(--color-surface)', display: 'flex', flexDirection: 'column', gap: 12 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
              <div style={{ width: 36, height: 36, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <Icon name="shopping-bag" size={18} />
              </div>
              <div style={{ flex: 1 }}>
                <h4 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{m.productName}</h4>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{m.productCode}</span>
              </div>
              <StatusBadge status={m.status} variant={getStatusVariant(m.status)} />
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
              <span>Type: {m.productType}</span>
              <span>Items: <strong>{m.inventoryCount}</strong></span>
            </div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
              Mapped: {m.mappedAt}
            </div>
            <button style={{ alignSelf: 'flex-start', padding: '6px 14px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-primary)', fontSize: 'var(--text-body)', fontWeight: 500 }}>
              <Icon name="eye" size={14} /> View Items
            </button>
          </div>
        ))}
      </div>
    </div>
  );
});
