import React from 'react';
import type { ProductVariant } from '../types';

interface VariantMatrixProps {
  variants: ProductVariant[];
  selectedId: string | null;
  onSelect: (id: string) => void;
}

const headerCell: React.CSSProperties = {
  padding: '10px 12px', borderBottom: '2px solid var(--color-border)',
  color: 'var(--color-text-tertiary)', fontWeight: 600, textTransform: 'uppercase',
  fontSize: 'var(--text-body-xs)', letterSpacing: '0.5px', whiteSpace: 'nowrap',
};

const cell: React.CSSProperties = {
  padding: '10px 12px', borderBottom: '1px solid var(--color-border)',
  color: 'var(--color-text-primary)', fontSize: 'var(--text-body-xs)',
};

export const VariantMatrix: React.FC<VariantMatrixProps> = React.memo(({ variants, selectedId, onSelect }) => {
  const allAttrNames = [...new Set(variants.flatMap((v) => v.attributes.map((a) => a.name)))];

  return (
    <div style={{ padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Variant Matrix</h2>
      {variants.length === 0 ? (
        <div style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>No variants to display</div>
      ) : (
        <div style={{ overflowX: 'auto', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' }}>
            <thead>
              <tr>
                <th style={headerCell}>Variant</th>
                <th style={headerCell}>SKU</th>
                {allAttrNames.map((name) => <th key={name} style={headerCell}>{name}</th>)}
                <th style={headerCell}>Price</th>
                <th style={headerCell}>Stock</th>
                <th style={headerCell}>Status</th>
              </tr>
            </thead>
            <tbody>
              {variants.map((v) => (
                <tr key={v.id} onClick={() => onSelect(v.id)}
                  style={{ cursor: 'pointer', background: v.id === selectedId ? 'var(--color-bg-surface-raised)' : undefined }}
                  onMouseEnter={(e) => { if (v.id !== selectedId) e.currentTarget.style.background = 'var(--color-bg-surface-hover)'; }}
                  onMouseLeave={(e) => { if (v.id !== selectedId) e.currentTarget.style.background = 'none'; }}>
                  <td style={{ ...cell, fontWeight: 500 }}>{v.name}</td>
                  <td style={{ ...cell, fontFamily: 'monospace', fontWeight: 600 }}>{v.sku}</td>
                  {allAttrNames.map((name) => {
                    const attr = v.attributes.find((a) => a.name === name);
                    return <td key={name} style={cell}>{attr?.value ?? '-'}</td>;
                  })}
                  <td style={cell}>₹{v.price.toFixed(2)}</td>
                  <td style={cell}>{v.stock}</td>
                  <td style={cell}>
                    <span style={{
                      padding: '2px 8px', borderRadius: 10, fontSize: 'var(--text-body-xs)', fontWeight: 600,
                      background: v.status === 'active' ? 'var(--color-accent-green)20' : 'var(--color-bg-surface-raised)',
                      color: v.status === 'active' ? 'var(--color-accent-green)' : 'var(--color-text-secondary)',
                    }}>{v.status}</span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
});
