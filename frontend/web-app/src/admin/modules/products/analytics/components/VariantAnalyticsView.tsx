import React from 'react';
import type { VariantAnalytic, ChartConfig } from '../types';
import { DonutChart } from './charts/DonutChart';
import { BarChart } from './charts/BarChart';
import { ProgressChart } from './charts/ProgressChart';

interface VariantAnalyticsViewProps {
  variant: VariantAnalytic;
  chartVariants: ChartConfig;
  chartConfig: any;
}

const statStyle: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' };

export const VariantAnalyticsView: React.FC<VariantAnalyticsViewProps> = React.memo(({ variant, chartVariants, chartConfig }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }} role="region" aria-label="Variant analytics">
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 'var(--space-component-gap)' }}>
      <div style={statStyle}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Products with Variants</span>
        <p style={{ margin: '4px 0 0', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600 }}>{variant.productsWithVariants}<span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', fontWeight: 400 }}> / {variant.totalProducts}</span></p>
      </div>
      <div style={statStyle}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Avg Variants per Product</span>
        <p style={{ margin: '4px 0 0', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600 }}>{variant.avgVariants}</p>
      </div>
      <div style={statStyle}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Missing Variants</span>
        <p style={{ margin: '4px 0 0', fontSize: 'var(--text-h5)', color: variant.missingVariants > 0 ? 'var(--color-accent-red)' : 'var(--color-accent-green)', fontWeight: 600 }}>{variant.missingVariants}</p>
      </div>
      <div style={statStyle}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>SKU Coverage</span>
        <ProgressChart label="SKU Coverage" value={variant.skuCoverage} max={100} color={variant.skuCoverage >= 80 ? 'var(--color-accent-green)' : variant.skuCoverage >= 60 ? 'var(--color-accent-yellow)' : 'var(--color-accent-red)'} />
      </div>
    </div>
    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600 }}>Variant Distribution</h3>
        <DonutChart config={chartVariants} />
      </div>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600 }}>Packaging Distribution</h3>
        <BarChart config={chartConfig} />
      </div>
    </div>
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600 }}>Attribute Usage</h3>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 8 }}>
        {variant.attributeUsage.map((a, i) => (
          <div key={i} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 12px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
            <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{a.label}</span>
            <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', fontWeight: 600 }}>{a.count}</span>
          </div>
        ))}
      </div>
    </div>
  </div>
));

export default VariantAnalyticsView;
