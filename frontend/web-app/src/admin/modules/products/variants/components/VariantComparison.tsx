import React from 'react';
import type { ProductVariant } from '../types';

interface VariantComparisonProps {
  variants: ProductVariant[];
  selectedIds: string[];
  onToggleSelect: (id: string) => void;
}

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const table: React.CSSProperties = { width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' };
const th: React.CSSProperties = { textAlign: 'left', padding: '10px 12px', borderBottom: '2px solid var(--color-border)', color: 'var(--color-text-tertiary)', fontWeight: 600, textTransform: 'uppercase', fontSize: 'var(--text-body-xs)', letterSpacing: '0.5px', whiteSpace: 'nowrap', background: 'var(--color-bg-surface-raised)' };
const td: React.CSSProperties = { padding: '10px 12px', borderBottom: '1px solid var(--color-border)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body-xs)' };
const label: React.CSSProperties = { ...td, fontWeight: 600, color: 'var(--color-text-tertiary)', whiteSpace: 'nowrap' };

export const VariantComparison: React.FC<VariantComparisonProps> = React.memo(({ variants, selectedIds, onToggleSelect }) => {
  const selected = variants.filter((v) => selectedIds.includes(v.id));
  const allAttrNames = [...new Set(selected.flatMap((v) => v.attributes.map((a) => a.name)))];

  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Variant Comparison</h2>
      {selected.length < 2 ? (
        <div style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>
          Select at least 2 variants to compare (click rows below)
        </div>
      ) : (
        <div style={{ overflowX: 'auto', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)' }}>
          <table style={table}>
            <thead>
              <tr>
                <th style={th}>Property</th>
                {selected.map((v) => (
                  <th key={v.id} style={{ ...th, textAlign: 'center' }}>
                    <div style={{ fontWeight: 600 }}>{v.name}</div>
                    <div style={{ fontSize: 10, fontWeight: 400 }}>{v.sku}</div>
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              <tr>
                <td style={label}>Status</td>
                {selected.map((v) => <td key={v.id} style={{ ...td, textAlign: 'center' }}><span style={{
                  padding: '2px 8px', borderRadius: 10, fontSize: 'var(--text-body-xs)', fontWeight: 600,
                  background: v.status === 'active' ? 'var(--color-accent-green)20' : 'var(--color-bg-surface-raised)',
                  color: v.status === 'active' ? 'var(--color-accent-green)' : 'var(--color-text-secondary)',
                }}>{v.status}</span></td>)}
              </tr>
              <tr>
                <td style={label}>Price</td>
                {selected.map((v) => <td key={v.id} style={{ ...td, textAlign: 'center', fontWeight: 600 }}>₹{v.price.toFixed(2)}</td>)}
              </tr>
              <tr>
                <td style={label}>Stock</td>
                {selected.map((v) => <td key={v.id} style={{ ...td, textAlign: 'center' }}>{v.stock}</td>)}
              </tr>
              <tr>
                <td style={label}>Default</td>
                {selected.map((v) => <td key={v.id} style={{ ...td, textAlign: 'center' }}>{v.isDefault ? '✓' : '-'}</td>)}
              </tr>
              {allAttrNames.map((name) => (
                <tr key={name}>
                  <td style={label}>{name}</td>
                  {selected.map((v) => {
                    const attr = v.attributes.find((a) => a.name === name);
                    return <td key={v.id} style={{ ...td, textAlign: 'center' }}>{attr?.value ?? '-'}</td>;
                  })}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <div style={{ marginTop: 8 }}>
        <h3 style={{ fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600, marginBottom: 8 }}>Select Variants to Compare</h3>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
          {variants.slice(0, 10).map((v) => {
            const isSel = selectedIds.includes(v.id);
            return (
              <button key={v.id} onClick={() => onToggleSelect(v.id)} style={{
                padding: '6px 12px', borderRadius: 'var(--radius-sm)', cursor: 'pointer', fontSize: 'var(--text-body-xs)',
                border: isSel ? '2px solid var(--color-accent-blue)' : '1px solid var(--color-border)',
                background: isSel ? 'var(--color-accent-blue)15' : 'var(--color-bg-surface-default)',
                color: 'var(--color-text-primary)', fontWeight: isSel ? 600 : 400,
                transition: 'all 0.15s',
              }}>
                {v.name} ({v.sku})
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
});
