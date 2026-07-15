import React from 'react';
import type { ProductVariant } from '../types';

interface VariantManagerProps {
  variants: ProductVariant[];
  selectedId: string | null;
  onSelect: (id: string) => void;
}

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const table: React.CSSProperties = { width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' };
const th: React.CSSProperties = { textAlign: 'left', padding: '10px 12px', borderBottom: '2px solid var(--color-border)', color: 'var(--color-text-tertiary)', fontWeight: 600, textTransform: 'uppercase', fontSize: 'var(--text-body-xs)', letterSpacing: '0.5px', whiteSpace: 'nowrap' };
const td: React.CSSProperties = { padding: '10px 12px', borderBottom: '1px solid var(--color-border)', color: 'var(--color-text-primary)' };

export const VariantManager: React.FC<VariantManagerProps> = React.memo(({ variants, selectedId, onSelect }) => {
  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        Product Variants ({variants.length})
      </h2>
      <div style={{ overflowX: 'auto', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)' }}>
        <table style={table}>
          <thead>
            <tr>
              <th style={th}>Variant Name</th>
              <th style={th}>SKU</th>
              <th style={th}>Product</th>
              <th style={th}>Key Attributes</th>
              <th style={th}>Price</th>
              <th style={th}>Stock</th>
              <th style={th}>Status</th>
            </tr>
          </thead>
          <tbody>
            {variants.length === 0 && (
              <tr><td colSpan={7} style={{ ...td, textAlign: 'center', padding: 32, color: 'var(--color-text-tertiary)' }}>No variants found</td></tr>
            )}
            {variants.map((v) => {
              const keyAttrs = v.attributes.filter((a) => ['Weight', 'Kit Type', 'Package Type'].includes(a.name)).map((a) => a.value).join(', ');
              return (
                <tr key={v.id} onClick={() => onSelect(v.id)}
                  style={{ cursor: 'pointer', background: v.id === selectedId ? 'var(--color-bg-surface-raised)' : undefined, transition: 'background 0.15s' }}
                  onMouseEnter={(e) => { if (v.id !== selectedId) e.currentTarget.style.background = 'var(--color-bg-surface-hover)'; }}
                  onMouseLeave={(e) => { if (v.id !== selectedId) e.currentTarget.style.background = 'none'; }}
                  role="row" aria-selected={v.id === selectedId}>
                  <td style={td}>
                    <div style={{ fontWeight: 500 }}>{v.name}</div>
                    <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>Default: {v.isDefault ? 'Yes' : 'No'}</div>
                  </td>
                  <td style={{ ...td, fontFamily: 'monospace', fontWeight: 600 }}>{v.sku}</td>
                  <td style={td}>{v.productName}</td>
                  <td style={td}><span style={{ fontSize: 'var(--text-body-xs)' }}>{keyAttrs}</span></td>
                  <td style={td}>₹{v.price.toFixed(2)}</td>
                  <td style={td}>{v.stock}</td>
                  <td style={td}>
                    <span style={{
                      padding: '2px 8px', borderRadius: 10, fontSize: 'var(--text-body-xs)', fontWeight: 600,
                      background: v.status === 'active' ? 'var(--color-accent-green)20' : v.status === 'draft' ? 'var(--color-accent-orange)20' : v.status === 'inactive' ? 'var(--color-accent-yellow)20' : 'var(--color-accent-red)20',
                      color: v.status === 'active' ? 'var(--color-accent-green)' : v.status === 'draft' ? 'var(--color-accent-orange)' : v.status === 'inactive' ? 'var(--color-accent-yellow)' : 'var(--color-accent-red)',
                    }}>{v.status}</span>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
});
