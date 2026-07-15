import React from 'react';
import type { BrandAnalytic } from '../types';

interface BrandAnalyticsViewProps {
  brands: BrandAnalytic[];
}

export const BrandAnalyticsView: React.FC<BrandAnalyticsViewProps> = React.memo(({ brands }) => {
  const total = brands.length;
  const active = brands.filter((b) => b.active).length;
  const inactive = total - active;
  const avgCoverage = total > 0 ? Math.round(brands.reduce((s, b) => s + b.coverage, 0) / total) : 0;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }} role="region" aria-label="Brand analytics">
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Total Brands</span>
          <p style={{ margin: '4px 0 0', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600 }}>{total}</p>
        </div>
        <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Active</span>
          <p style={{ margin: '4px 0 0', fontSize: 'var(--text-h5)', color: 'var(--color-accent-green)', fontWeight: 600 }}>{active}</p>
        </div>
        <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Inactive</span>
          <p style={{ margin: '4px 0 0', fontSize: 'var(--text-h5)', color: 'var(--color-accent-red)', fontWeight: 600 }}>{inactive}</p>
        </div>
        <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Avg Coverage</span>
          <p style={{ margin: '4px 0 0', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600 }}>{avgCoverage}%</p>
        </div>
      </div>
      <div style={{ overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' }} aria-label="Brand table">
          <thead>
            <tr style={{ borderBottom: '1px solid var(--color-border)', color: 'var(--color-text-secondary)' }}>
              <th style={{ textAlign: 'left', padding: '10px 12px', fontWeight: 500 }}>Brand</th>
              <th style={{ textAlign: 'right', padding: '10px 12px', fontWeight: 500 }}>Products</th>
              <th style={{ textAlign: 'right', padding: '10px 12px', fontWeight: 500 }}>Coverage</th>
              <th style={{ textAlign: 'center', padding: '10px 12px', fontWeight: 500 }}>Status</th>
              <th style={{ textAlign: 'right', padding: '10px 12px', fontWeight: 500 }}>Health</th>
            </tr>
          </thead>
          <tbody>
            {brands.map((b) => (
              <tr key={b.id} style={{ borderBottom: '1px solid var(--color-border)' }}>
                <td style={{ padding: '10px 12px', color: 'var(--color-text-primary)', fontWeight: 500 }}>{b.name}</td>
                <td style={{ padding: '10px 12px', textAlign: 'right', color: 'var(--color-text-primary)' }}>{b.productCount}</td>
                <td style={{ padding: '10px 12px', textAlign: 'right', color: 'var(--color-text-primary)' }}>{b.coverage}%</td>
                <td style={{ padding: '10px 12px', textAlign: 'center' }}>
                  <span style={{ display: 'inline-block', padding: '2px 10px', borderRadius: 12, fontSize: 'var(--text-caption)', fontWeight: 500, color: b.active ? 'var(--color-accent-green)' : 'var(--color-accent-red)', background: b.active ? 'color-mix(in srgb, var(--color-accent-green) 15%, transparent)' : 'color-mix(in srgb, var(--color-accent-red) 15%, transparent)' }}>{b.active ? 'Active' : 'Inactive'}</span>
                </td>
                <td style={{ padding: '10px 12px', textAlign: 'right', color: b.health >= 80 ? 'var(--color-accent-green)' : b.health >= 60 ? 'var(--color-accent-yellow)' : 'var(--color-accent-red)' }}>{b.health}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});

export default BrandAnalyticsView;
