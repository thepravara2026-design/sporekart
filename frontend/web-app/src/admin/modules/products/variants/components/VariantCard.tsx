import React from 'react';
import type { ProductVariant } from '../types';

interface VariantCardProps {
  variant: ProductVariant;
  isSelected: boolean;
  onSelect: (id: string) => void;
}

const base: React.CSSProperties = {
  padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)', cursor: 'pointer', transition: 'border-color 0.15s, box-shadow 0.15s',
};

export const VariantCard: React.FC<VariantCardProps> = React.memo(({ variant, isSelected, onSelect }) => {
  const weight = variant.attributes.find((a) => a.name === 'Weight' || a.name === 'Kit Type')?.value ?? '';
  const pkg = variant.attributes.find((a) => a.name === 'Package Type')?.value ?? '';
  return (
    <div
      style={isSelected ? { ...base, borderColor: 'var(--color-accent-blue)', boxShadow: '0 0 0 2px var(--color-accent-blue)' } : base}
      onClick={() => onSelect(variant.id)} role="button" tabIndex={0} aria-selected={isSelected}
      onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') onSelect(variant.id); }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 8 }}>
        <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{variant.name}</div>
        <span style={{ fontFamily: 'monospace', fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>{variant.sku}</span>
      </div>
      <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', marginBottom: 6 }}>
        {variant.productName} · {weight}{pkg ? ` · ${pkg}` : ''}
      </div>
      <div style={{ display: 'flex', gap: 12, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>
        <span>₹{variant.price.toFixed(2)}</span>
        <span>Stock: {variant.stock}</span>
        <span style={{ color: variant.isDefault ? 'var(--color-accent-blue)' : 'var(--color-text-tertiary)' }}>
          {variant.isDefault ? 'Default' : ''}
        </span>
      </div>
    </div>
  );
});
