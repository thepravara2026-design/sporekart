import React from 'react';
import type { DiscountRule } from '../types';

interface DiscountManagerProps {
  discounts: DiscountRule[];
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

function StatusBadge({ status }: { status: string }) {
  const colors: Record<string, string> = {
    active: 'var(--color-accent-green)',
    inactive: 'var(--color-accent-red)',
    scheduled: 'var(--color-accent-blue)',
    expired: 'var(--color-accent-gray)',
    archived: 'var(--color-accent-yellow)',
  };
  return (
    <span style={{ padding: '2px 8px', borderRadius: 10, fontSize: 'var(--text-body-xs)', fontWeight: 600, background: `${colors[status] ?? '#888'}20`, color: colors[status] ?? '#888' }}>
      {status}
    </span>
  );
}

const discTypes: Record<string, string> = {
  percentage: 'Percentage',
  fixed: 'Fixed Amount',
  volume: 'Volume Based',
  wholesale: 'Wholesale',
  coupon: 'Coupon',
};

export const DiscountManager: React.FC<DiscountManagerProps> = React.memo(({ discounts }) => {
  return (
    <div style={sectionStyle}>
      <h2 style={h2Style}>Discount Rules</h2>
      <div style={{ overflowX: 'auto', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)' }}>
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={thStyle}>Name</th>
              <th style={thStyle}>Type</th>
              <th style={thStyle}>Value</th>
              <th style={thStyle}>Applies To</th>
              <th style={thStyle}>Period</th>
              <th style={thStyle}>Priority</th>
              <th style={thStyle}>Stackable</th>
              <th style={thStyle}>Status</th>
            </tr>
          </thead>
          <tbody>
            {discounts.length === 0 && (
              <tr>
                <td colSpan={8} style={{ ...tdStyle, textAlign: 'center', padding: 32, color: 'var(--color-text-tertiary)' }}>
                  No discount rules configured
                </td>
              </tr>
            )}
            {discounts.map((d) => (
              <tr key={d.id}>
                <td style={tdStyle}>
                  <div style={{ fontWeight: 500 }}>{d.name}</div>
                  {d.description && <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>{d.description}</div>}
                </td>
                <td style={tdStyle}>{discTypes[d.type] ?? d.type}</td>
                <td style={tdStyle}>{d.type === 'fixed' ? `₹${d.value}` : `${d.value}%`}</td>
                <td style={tdStyle}>{d.applicableTo}</td>
                <td style={tdStyle}>
                  <div style={{ fontSize: 'var(--text-body-xs)' }}>{d.startDate} → {d.endDate}</div>
                </td>
                <td style={tdStyle}>{d.priority}</td>
                <td style={tdStyle}>{d.stackable ? 'Yes' : 'No'}</td>
                <td style={tdStyle}><StatusBadge status={d.status} /></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});
