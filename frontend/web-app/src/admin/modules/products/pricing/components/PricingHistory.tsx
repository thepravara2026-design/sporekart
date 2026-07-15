import React from 'react';
import type { PriceHistoryEntry } from '../types';

interface PricingHistoryProps {
  history: PriceHistoryEntry[];
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

const timelineStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 0,
};

const entryStyle: React.CSSProperties = {
  display: 'flex',
  gap: 16,
  padding: '12px 0',
  borderBottom: '1px solid var(--color-border)',
};

const timelineDot: React.CSSProperties = {
  width: 12,
  height: 12,
  borderRadius: '50%',
  marginTop: 4,
  flexShrink: 0,
};

const entryContent: React.CSSProperties = {
  flex: 1,
};

const entryHeader: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  marginBottom: 4,
};

const entryName: React.CSSProperties = {
  fontSize: 'var(--text-body-sm)',
  fontWeight: 600,
  color: 'var(--color-text-primary)',
};

const entryDate: React.CSSProperties = {
  fontSize: 'var(--text-body-xs)',
  color: 'var(--color-text-tertiary)',
};

const entryDetail: React.CSSProperties = {
  fontSize: 'var(--text-body-xs)',
  color: 'var(--color-text-secondary)',
  marginBottom: 2,
};

const priceChange: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 8,
  padding: '4px 8px',
  borderRadius: 'var(--radius-xs)',
  background: 'var(--color-bg-surface-raised)',
  marginTop: 4,
};

const changeBadge: React.CSSProperties = {
  padding: '2px 6px',
  borderRadius: 8,
  fontSize: 'var(--text-body-xs)',
  fontWeight: 600,
};

const typeLabels: Record<string, string> = {
  price_change: 'Price Change',
  discount_applied: 'Discount',
  bulk_update: 'Bulk Update',
  scheduled: 'Scheduled',
  approved: 'Approved',
  reverted: 'Reverted',
  archived: 'Archived',
  published: 'Published',
};

const typeColors: Record<string, string> = {
  price_change: 'var(--color-accent-blue)',
  discount_applied: 'var(--color-accent-green)',
  bulk_update: 'var(--color-accent-purple)',
  scheduled: 'var(--color-accent-orange)',
  approved: 'var(--color-accent-green)',
  reverted: 'var(--color-accent-red)',
  archived: 'var(--color-accent-yellow)',
  published: 'var(--color-accent-cyan)',
};

function formatPrice(amount: number): string {
  return `₹${amount.toFixed(2)}`;
}

export const PricingHistory: React.FC<PricingHistoryProps> = React.memo(({ history }) => {
  return (
    <div style={sectionStyle}>
      <h2 style={h2Style}>Pricing History</h2>
      {history.length === 0 ? (
        <div style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>
          No pricing history available
        </div>
      ) : (
        <div style={timelineStyle}>
          {history.map((h) => {
            const color = typeColors[h.type] ?? 'var(--color-accent-gray)';
            return (
              <div key={h.id} style={entryStyle}>
                <div style={{ ...timelineDot, background: color }} />
                <div style={entryContent}>
                  <div style={entryHeader}>
                    <div style={entryName}>{h.entityName}</div>
                    <div style={entryDate}>{new Date(h.changeDate).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' })}</div>
                  </div>
                  <div style={entryDetail}>
                    <span style={{ ...changeBadge, background: `${color}20`, color }}>{typeLabels[h.type] ?? h.type}</span>
                    {' · '}by {h.changedBy}
                  </div>
                  <div style={entryDetail}>{h.reason}</div>
                  {h.previousPrice !== h.newPrice && (
                    <div style={priceChange}>
                      <span style={{ color: 'var(--color-text-tertiary)', textDecoration: 'line-through' }}>
                        {formatPrice(h.previousPrice)}
                      </span>
                      <span style={{ color: 'var(--color-text-secondary)' }}>→</span>
                      <span style={{ fontWeight: 700, color: h.newPrice < h.previousPrice ? 'var(--color-accent-green)' : 'var(--color-accent-red)' }}>
                        {formatPrice(h.newPrice)}
                      </span>
                      <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>
                        ({h.tier})
                      </span>
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
});
