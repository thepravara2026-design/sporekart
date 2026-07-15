import React from 'react';
import type { PricingEntity, PriceTier } from '../types';
import { PRICE_TIER_LABELS } from '../types';

interface PriceCardProps {
  entity: PricingEntity;
  onSelect: (id: string) => void;
  isSelected: boolean;
}

const cardStyle: React.CSSProperties = {
  padding: 16,
  borderRadius: 'var(--radius-md)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
  cursor: 'pointer',
  transition: 'border-color 0.15s, box-shadow 0.15s',
};

const selectedCard: React.CSSProperties = {
  ...cardStyle,
  borderColor: 'var(--color-accent-blue)',
  boxShadow: '0 0 0 2px var(--color-accent-blue)',
};

const tierGrid: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: '1fr 1fr',
  gap: 4,
  marginTop: 8,
};

const tierItem: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  padding: '2px 0',
  fontSize: 'var(--text-body-xs)',
};

export const PriceCard: React.FC<PriceCardProps> = React.memo(({ entity, onSelect, isSelected }) => {
  const mrp = entity.prices.find((p) => p.tier === 'mrp')?.amount ?? 0;
  const selling = entity.prices.find((p) => p.tier === 'selling')?.amount ?? 0;
  const discount = mrp > 0 ? Math.round(((mrp - selling) / mrp) * 100) : 0;

  return (
    <div
      style={isSelected ? selectedCard : cardStyle}
      onClick={() => onSelect(entity.id)}
      role="button"
      tabIndex={0}
      aria-selected={isSelected}
      onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') onSelect(entity.id); }}
    >
      <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
        {entity.productName}
      </div>
      <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', marginTop: 2 }}>
        {entity.sku} · {entity.category}
      </div>
      {discount > 0 && (
        <div style={{ marginTop: 6, display: 'flex', alignItems: 'center', gap: 6 }}>
          <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', textDecoration: 'line-through' }}>
            ₹{mrp.toFixed(2)}
          </span>
          <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 700, color: 'var(--color-accent-green)' }}>
            ₹{selling.toFixed(2)}
          </span>
          <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-accent-red)', fontWeight: 600 }}>
            {discount}% off
          </span>
        </div>
      )}
      <div style={tierGrid}>
        {entity.prices.slice(0, 6).map((p) => (
          <div key={p.tier} style={tierItem}>
            <span style={{ color: 'var(--color-text-tertiary)' }}>{PRICE_TIER_LABELS[p.tier as PriceTier]}</span>
            <span style={{ color: 'var(--color-text-primary)', fontWeight: 500 }}>₹{p.amount.toFixed(2)}</span>
          </div>
        ))}
      </div>
    </div>
  );
});
