import React from 'react';
import type { PricingEntity } from '../types';

interface PricingTableProps {
  entities: PricingEntity[];
  selectedId: string | null;
  onSelect: (id: string) => void;
}

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

function StatusBadge({ status }: { status: string }) {
  const colors: Record<string, string> = {
    active: 'var(--color-accent-green)',
    draft: 'var(--color-accent-yellow)',
    pending: 'var(--color-accent-orange)',
    archived: 'var(--color-accent-red)',
  };
  return (
    <span
      style={{
        padding: '2px 8px',
        borderRadius: 10,
        fontSize: 'var(--text-body-xs)',
        fontWeight: 600,
        background: `${colors[status] ?? 'var(--color-accent-gray)'}20`,
        color: colors[status] ?? 'var(--color-text-secondary)',
      }}
    >
      {status}
    </span>
  );
}

export const PricingTable: React.FC<PricingTableProps> = React.memo(({ entities, selectedId, onSelect }) => {
  return (
    <div style={{ overflowX: 'auto', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)' }}>
      <table style={tableStyle}>
        <thead>
          <tr>
            <th style={thStyle}>Product</th>
            <th style={thStyle}>SKU</th>
            <th style={thStyle}>MRP</th>
            <th style={thStyle}>Selling</th>
            <th style={thStyle}>Wholesale</th>
            <th style={thStyle}>GST</th>
            <th style={thStyle}>Discount</th>
            <th style={thStyle}>Status</th>
          </tr>
        </thead>
        <tbody>
          {entities.length === 0 && (
            <tr>
              <td colSpan={8} style={{ ...tdStyle, textAlign: 'center', padding: 32, color: 'var(--color-text-tertiary)' }}>
                No pricing data found
              </td>
            </tr>
          )}
          {entities.map((entity) => {
            const mrp = entity.prices.find((p) => p.tier === 'mrp')?.amount ?? 0;
            const selling = entity.prices.find((p) => p.tier === 'selling')?.amount ?? 0;
            const wholesale = entity.prices.find((p) => p.tier === 'wholesale')?.amount ?? 0;
            const discount = mrp > 0 ? Math.round(((mrp - selling) / mrp) * 100) : 0;
            const isSelected = entity.id === selectedId;

            return (
              <tr
                key={entity.id}
                onClick={() => onSelect(entity.id)}
                style={{
                  cursor: 'pointer',
                  background: isSelected ? 'var(--color-bg-surface-raised)' : undefined,
                  transition: 'background 0.15s',
                }}
                onMouseEnter={(e) => { if (!isSelected) e.currentTarget.style.background = 'var(--color-bg-surface-hover)'; }}
                onMouseLeave={(e) => { if (!isSelected) e.currentTarget.style.background = 'none'; }}
                role="row"
                aria-selected={isSelected}
              >
                <td style={tdStyle}>
                  <div style={{ fontWeight: 500 }}>{entity.productName}</div>
                  <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>
                    {entity.category} · {entity.brand}
                  </div>
                </td>
                <td style={tdStyle}>{entity.sku}</td>
                <td style={tdStyle}>{formatPrice(mrp)}</td>
                <td style={tdStyle}>{formatPrice(selling)}</td>
                <td style={tdStyle}>{formatPrice(wholesale)}</td>
                <td style={tdStyle}>{entity.gstPercentage}%</td>
                <td style={tdStyle}>{discount > 0 ? `${discount}%` : '-'}</td>
                <td style={tdStyle}><StatusBadge status={entity.status} /></td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
});
