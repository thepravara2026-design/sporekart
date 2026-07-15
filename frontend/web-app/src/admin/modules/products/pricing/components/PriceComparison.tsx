import React from 'react';
import type { PricingEntity } from '../types';
import { getPriceForTier, getDiscountPercent } from '../mock/mockPrices';

interface PriceComparisonProps {
  entities: PricingEntity[];
  selectedId: string | null;
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

const tableStyle: React.CSSProperties = {
  width: '100%',
  borderCollapse: 'collapse',
  fontSize: 'var(--text-body-sm)',
};

const thStyle: React.CSSProperties = {
  textAlign: 'left',
  padding: '10px 12px',
  borderBottom: '2px solid var(--color-border)',
  color: 'var(--color-text-tertiary)',
  fontWeight: 600,
  textTransform: 'uppercase',
  fontSize: 'var(--text-body-xs)',
  letterSpacing: '0.5px',
  whiteSpace: 'nowrap',
};

const tdStyle: React.CSSProperties = {
  padding: '10px 12px',
  borderBottom: '1px solid var(--color-border)',
  color: 'var(--color-text-primary)',
};

function formatPrice(amount: number): string {
  return `₹${amount.toFixed(2)}`;
}

function DiffHighlight({ current, previous }: { current: number; previous: number }) {
  if (previous === 0) return <span style={{ color: 'var(--color-text-primary)' }}>{formatPrice(current)}</span>;
  const diff = current - previous;
  const pct = Math.round((diff / previous) * 100);
  return (
    <span>
      <span style={{ color: 'var(--color-text-primary)' }}>{formatPrice(current)}</span>
      {diff !== 0 && (
        <span style={{ marginLeft: 6, fontSize: 'var(--text-body-xs)', color: diff < 0 ? 'var(--color-accent-green)' : 'var(--color-accent-red)', fontWeight: 600 }}>
          {diff < 0 ? '↓' : '↑'} {Math.abs(pct)}%
        </span>
      )}
    </span>
  );
}

export const PriceComparison: React.FC<PriceComparisonProps> = React.memo(({ entities, selectedId }) => {
  const selected = entities.find((e) => e.id === selectedId);
  const others = entities.filter((e) => e.id !== selectedId).slice(0, 4);

  if (!selected) {
    return (
      <div style={sectionStyle}>
        <h2 style={h2Style}>Price Comparison</h2>
        <div style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>
          Select a product to compare prices
        </div>
      </div>
    );
  }

  const tiers = ['mrp', 'selling', 'wholesale', 'distributor', 'dealer', 'retail'] as const;

  return (
    <div style={sectionStyle}>
      <h2 style={h2Style}>Price Comparison</h2>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', marginBottom: 8 }}>
        Comparing <strong>{selected.productName}</strong> with similar products
      </div>
      <div style={{ overflowX: 'auto', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)' }}>
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={thStyle}>Price Tier</th>
              <th style={{ ...thStyle, background: 'var(--color-bg-surface-raised)' }}>{selected.productName}</th>
              {others.map((e) => (
                <th key={e.id} style={thStyle}>{e.productName}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {tiers.map((tier) => (
              <tr key={tier}>
                <td style={{ ...tdStyle, fontWeight: 600, textTransform: 'capitalize' }}>{tier}</td>
                <td style={{ ...tdStyle, background: 'var(--color-bg-surface-raised)', fontWeight: 600 }}>
                  {formatPrice(getPriceForTier(selected, tier))}
                </td>
                {others.map((e) => (
                  <td key={e.id} style={tdStyle}>
                    <DiffHighlight current={getPriceForTier(e, tier)} previous={getPriceForTier(selected, tier)} />
                  </td>
                ))}
              </tr>
            ))}
            <tr>
              <td style={{ ...tdStyle, fontWeight: 600 }}>Discount</td>
              <td style={{ ...tdStyle, background: 'var(--color-bg-surface-raised)', fontWeight: 600 }}>
                {getDiscountPercent(selected)}%
              </td>
              {others.map((e) => (
                <td key={e.id} style={tdStyle}>
                  {getDiscountPercent(e)}%
                </td>
              ))}
            </tr>
            <tr>
              <td style={{ ...tdStyle, fontWeight: 600 }}>GST</td>
              <td style={{ ...tdStyle, background: 'var(--color-bg-surface-raised)', fontWeight: 600 }}>
                {selected.gstPercentage}%
              </td>
              {others.map((e) => (
                <td key={e.id} style={tdStyle}>{e.gstPercentage}%</td>
              ))}
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
});
