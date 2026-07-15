import React from 'react';
import type { TaxRule } from '../types';

interface TaxManagerProps {
  rules: TaxRule[];
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
};

const tdStyle: React.CSSProperties = {
  padding: '10px 12px',
  borderBottom: '1px solid var(--color-border)',
  color: 'var(--color-text-primary)',
};

const taxTypes: Record<string, string> = {
  gst: 'GST',
  vat: 'VAT',
  sales_tax: 'Sales Tax',
  service_tax: 'Service Tax',
  cess: 'Cess',
};

export const TaxManager: React.FC<TaxManagerProps> = React.memo(({ rules }) => {
  return (
    <div style={sectionStyle}>
      <h2 style={h2Style}>Tax Rules</h2>
      <div style={{ overflowX: 'auto', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)' }}>
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={thStyle}>Name</th>
              <th style={thStyle}>Type</th>
              <th style={thStyle}>Percentage</th>
              <th style={thStyle}>Category</th>
              <th style={thStyle}>Applies To</th>
              <th style={thStyle}>Status</th>
            </tr>
          </thead>
          <tbody>
            {rules.length === 0 && (
              <tr>
                <td colSpan={6} style={{ ...tdStyle, textAlign: 'center', padding: 32, color: 'var(--color-text-tertiary)' }}>
                  No tax rules configured
                </td>
              </tr>
            )}
            {rules.map((r) => (
              <tr key={r.id}>
                <td style={tdStyle}>
                  <div style={{ fontWeight: 500 }}>{r.name}</div>
                  {r.description && <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>{r.description}</div>}
                </td>
                <td style={tdStyle}>{taxTypes[r.type] ?? r.type}</td>
                <td style={tdStyle}><strong>{r.percentage}%</strong></td>
                <td style={tdStyle}>{r.category}</td>
                <td style={tdStyle}>{r.applicableTo}</td>
                <td style={tdStyle}>
                  <span style={{ color: r.status === 'active' ? 'var(--color-accent-green)' : 'var(--color-accent-red)', fontWeight: 600 }}>
                    {r.status}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});
