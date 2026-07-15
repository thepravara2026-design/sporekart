import React from 'react';
import type { ProductQualityMetric } from '../types';

interface ProductQualityProps {
  metrics: ProductQualityMetric[];
}

const severityColor: Record<string, string> = {
  critical: 'var(--color-accent-red)',
  warning: 'var(--color-accent-orange)',
  info: 'var(--color-accent-blue)',
};

const severityBg: Record<string, string> = {
  critical: 'color-mix(in srgb, var(--color-accent-red) 15%, transparent)',
  warning: 'color-mix(in srgb, var(--color-accent-orange) 15%, transparent)',
  info: 'color-mix(in srgb, var(--color-accent-blue) 15%, transparent)',
};

export const ProductQuality: React.FC<ProductQualityProps> = React.memo(({ metrics }) => (
  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 'var(--space-component-gap)' }} role="list" aria-label="Product quality metrics">
    {metrics.map((m, i) => (
      <div key={`${m.label}-${i}`} style={{ display: 'flex', alignItems: 'center', gap: 12, padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }} role="listitem">
        <div style={{ width: 40, height: 40, display: 'flex', alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-sm)', background: severityBg[m.severity], fontSize: 20 }} aria-hidden="true">{m.icon}</div>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 4, flex: 1 }}>
          <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', fontWeight: 500 }}>{m.label}</span>
          <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
            <span style={{ fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600 }}>{m.count}</span>
            <span style={{ fontSize: 'var(--text-caption)', color: severityColor[m.severity], padding: '2px 8px', borderRadius: 12, background: severityBg[m.severity] }}>{m.severity}</span>
          </div>
        </div>
      </div>
    ))}
  </div>
));

export default ProductQuality;
