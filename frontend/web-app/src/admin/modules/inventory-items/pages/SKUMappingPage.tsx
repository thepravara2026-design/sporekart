import { memo, useState } from 'react';
import { SectionHeader } from '../../inventory/components';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { getSKUMappings } from '../services/inventoryItemMockService';
import { getStatusVariant } from '../utils';

export const SKUMappingPage = memo(function SKUMappingPage() {
  const [mappings] = useState(() => getSKUMappings());

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="SKU Association" description="Every SKU is linked to an inventory item, variant, and product. This is the foundation for all future stock and transaction tracking." />

      <div style={{ overflowX: 'auto', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }} aria-label="SKU mappings">
          <thead>
            <tr style={{ background: 'var(--color-surface)', textAlign: 'left' }}>
              <Th>SKU</Th>
              <Th>Inventory Item</Th>
              <Th>Variant</Th>
              <Th>Product</Th>
              <Th>Status</Th>
              <Th>Mapped</Th>
            </tr>
          </thead>
          <tbody>
            {mappings.map((m) => (
              <tr key={m.id} style={{ borderTop: '1px solid var(--color-border)' }}>
                <Td><code style={{ fontSize: 'var(--text-caption)', background: 'var(--color-surface-hover)', padding: '2px 6px', borderRadius: 'var(--radius-sm)' }}>{m.sku}</code></Td>
                <Td><span style={{ fontWeight: 500 }}>{m.inventoryItemName}</span></Td>
                <Td style={{ color: 'var(--color-text-secondary)' }}>{m.variantName}</Td>
                <Td style={{ color: 'var(--color-text-secondary)' }}>{m.productName}</Td>
                <Td><StatusBadge status={m.status} variant={getStatusVariant(m.status)} /></Td>
                <Td style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>{m.mappedAt}</Td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});

function Th({ children }: { children: React.ReactNode }) {
  return <th scope="col" style={{ padding: '12px 16px', fontWeight: 600, color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)', textTransform: 'uppercase', letterSpacing: '0.04em', whiteSpace: 'nowrap' }}>{children}</th>;
}

function Td({ children, style }: { children: React.ReactNode; style?: React.CSSProperties }) {
  return <td style={{ padding: '12px 16px', color: 'var(--color-text-primary)', verticalAlign: 'middle', ...style }}>{children}</td>;
}
