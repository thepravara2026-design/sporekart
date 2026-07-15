import { memo, useState } from 'react';
import { SectionHeader } from '../../inventory/components';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { getVariantMappings } from '../services/inventoryItemMockService';
import { getStatusVariant } from '../utils';

export const VariantsMappingPage = memo(function VariantsMappingPage() {
  const [mappings] = useState(() => getVariantMappings());

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Variant Mapping" description="Product variants mapped to inventory items. Each variant can generate multiple SKU-level inventory records." />

      <div style={{ overflowX: 'auto', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }} aria-label="Variant mappings">
          <thead>
            <tr style={{ background: 'var(--color-surface)', textAlign: 'left' }}>
              <Th>Variant</Th>
              <Th>Product</Th>
              <Th>SKUs</Th>
              <Th>Inventory Items</Th>
              <Th>Status</Th>
              <Th>Mapped</Th>
            </tr>
          </thead>
          <tbody>
            {mappings.map((m) => (
              <tr key={m.id} style={{ borderTop: '1px solid var(--color-border)' }}>
                <Td><span style={{ fontWeight: 500 }}>{m.variantName}</span></Td>
                <Td style={{ color: 'var(--color-text-secondary)' }}>{m.productName}</Td>
                <Td>{m.skuCount}</Td>
                <Td>{m.inventoryCount}</Td>
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
