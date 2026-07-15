import React from 'react';
import type { PromotionalCampaign } from '../types';

interface PromotionalPricingProps {
  campaigns: PromotionalCampaign[];
}

const sectionStyle: React.CSSProperties = {
  padding: 'var(--space-component-gap)',
  display: 'flex',
  flexDirection: 'column',
  gap: 16,
};

const h2Style: React.CSSProperties = {
  margin: 0,
  fontSize: 'var(--text-h2)',
  color: 'var(--color-text-primary)',
};

const gridStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))',
  gap: 12,
};

const cardStyle: React.CSSProperties = {
  padding: 16,
  borderRadius: 'var(--radius-md)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
};

const typeBadge: React.CSSProperties = {
  padding: '2px 8px',
  borderRadius: 10,
  fontSize: 'var(--text-body-xs)',
  fontWeight: 600,
};

const promoTypes: Record<string, { label: string; color: string }> = {
  campaign: { label: 'Campaign', color: 'var(--color-accent-blue)' },
  limited_time: { label: 'Limited Time', color: 'var(--color-accent-orange)' },
  weekend: { label: 'Weekend', color: 'var(--color-accent-purple)' },
  seasonal: { label: 'Seasonal', color: 'var(--color-accent-green)' },
  featured: { label: 'Featured', color: 'var(--color-accent-yellow)' },
  collection: { label: 'Collection', color: 'var(--color-accent-pink)' },
  category: { label: 'Category', color: 'var(--color-accent-cyan)' },
  brand: { label: 'Brand', color: 'var(--color-accent-indigo)' },
  launch: { label: 'Launch', color: 'var(--color-accent-red)' },
};

const detailRow: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  padding: '4px 0',
  fontSize: 'var(--text-body-xs)',
  borderBottom: '1px solid var(--color-border)',
};

export const PromotionalPricing: React.FC<PromotionalPricingProps> = React.memo(({ campaigns }) => {
  return (
    <div style={sectionStyle}>
      <h2 style={h2Style}>Promotional Pricing</h2>
      <div style={gridStyle}>
        {campaigns.length === 0 && (
          <div style={{ gridColumn: '1 / -1', textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>
            No promotional campaigns configured
          </div>
        )}
        {campaigns.map((c) => {
          const pt = promoTypes[c.type] ?? { label: c.type, color: 'var(--color-accent-gray)' };
          return (
            <div key={c.id} style={cardStyle}>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 8 }}>
                <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
                  {c.name}
                </span>
                <span style={{ ...typeBadge, background: `${pt.color}20`, color: pt.color }}>{pt.label}</span>
              </div>
              <div style={detailRow}>
                <span style={{ color: 'var(--color-text-tertiary)' }}>Discount</span>
                <span style={{ fontWeight: 500 }}>{c.discountType === 'fixed' ? `₹${c.discountValue}` : `${c.discountValue}%`}</span>
              </div>
              <div style={detailRow}>
                <span style={{ color: 'var(--color-text-tertiary)' }}>Applies To</span>
                <span>{c.applicableTo}</span>
              </div>
              <div style={detailRow}>
                <span style={{ color: 'var(--color-text-tertiary)' }}>Period</span>
                <span>{c.startDate} → {c.endDate}</span>
              </div>
              <div style={detailRow}>
                <span style={{ color: 'var(--color-text-tertiary)' }}>Priority</span>
                <span>{c.priority}</span>
              </div>
              <div style={detailRow}>
                <span style={{ color: 'var(--color-text-tertiary)' }}>Status</span>
                <span style={{ color: c.status === 'active' ? 'var(--color-accent-green)' : 'var(--color-accent-orange)' }}>
                  {c.status}
                </span>
              </div>
              {c.description && (
                <div style={{ marginTop: 8, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>
                  {c.description}
                </div>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
});
