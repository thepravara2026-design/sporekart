import React from 'react';
import type { CurrencySetting } from '../types';

interface CurrencySettingsProps {
  currencies: CurrencySetting[];
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

export const CurrencySettings: React.FC<CurrencySettingsProps> = React.memo(({ currencies }) => {
  return (
    <div style={sectionStyle}>
      <h2 style={h2Style}>Currency Settings</h2>
      <div style={{ overflowX: 'auto', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)' }}>
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={thStyle}>Code</th>
              <th style={thStyle}>Symbol</th>
              <th style={thStyle}>Name</th>
              <th style={thStyle}>Precision</th>
              <th style={thStyle}>Format</th>
              <th style={thStyle}>Exchange Rate</th>
              <th style={thStyle}>Default</th>
              <th style={thStyle}>Status</th>
            </tr>
          </thead>
          <tbody>
            {currencies.map((c) => (
              <tr key={c.code}>
                <td style={{ ...tdStyle, fontWeight: 700 }}>{c.code}</td>
                <td style={{ ...tdStyle, fontSize: 'var(--text-h4)' }}>{c.symbol}</td>
                <td style={tdStyle}>{c.name}</td>
                <td style={tdStyle}>{c.precision} decimals</td>
                <td style={tdStyle}>{c.format}</td>
                <td style={tdStyle}>{c.exchangeRate}</td>
                <td style={tdStyle}>
                  {c.isDefault && (
                    <span style={{ padding: '2px 8px', borderRadius: 10, fontSize: 'var(--text-body-xs)', fontWeight: 600, background: 'var(--color-accent-blue)20', color: 'var(--color-accent-blue)' }}>
                      Default
                    </span>
                  )}
                </td>
                <td style={tdStyle}>
                  <span style={{ color: c.status === 'active' ? 'var(--color-accent-green)' : 'var(--color-accent-red)', fontWeight: 600 }}>
                    {c.status}
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
