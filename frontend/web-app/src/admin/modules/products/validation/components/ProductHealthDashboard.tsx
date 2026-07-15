import React from 'react';
import type { ProductHealthSummary } from '../types';

interface ProductHealthDashboardProps {
  health: ProductHealthSummary;
}

const categories: { key: keyof ProductHealthSummary; label: string }[] = [
  { key: 'validationScore', label: 'Validation' },
  { key: 'seoScore', label: 'SEO' },
  { key: 'complianceScore', label: 'Compliance' },
  { key: 'marketplaceScore', label: 'Marketplace' },
  { key: 'accessibilityScore', label: 'Accessibility' },
  { key: 'aiReadinessScore', label: 'AI Readiness' },
  { key: 'publishingScore', label: 'Publishing' },
];

function scoreColor(s: number): string {
  if (s >= 80) return 'var(--color-success)';
  if (s >= 50) return 'var(--color-warning)';
  return 'var(--color-danger)';
}

function ringColor(s: number): string {
  if (s >= 80) return 'var(--color-success)';
  if (s >= 60) return 'var(--color-warning)';
  return 'var(--color-danger)';
}

const riskColors: Record<string, { bg: string; color: string }> = {
  low: { bg: '#dcfce7', color: '#166534' },
  medium: { bg: '#fef9c3', color: '#854d0e' },
  high: { bg: '#fee2e2', color: '#991b1b' },
  critical: { bg: '#fce7f3', color: '#9d174d' },
};

const certLabels: Record<string, string> = {
  none: 'Not Certified',
  bronze: 'Bronze',
  silver: 'Silver',
  gold: 'Gold',
  enterprise: 'Enterprise',
  marketplace_ready: 'Marketplace Ready',
  export_ready: 'Export Ready',
};

const circumference = 2 * Math.PI * 58;

export const ProductHealthDashboard = React.memo(function ProductHealthDashboard({ health }: ProductHealthDashboardProps) {
  const offset = circumference - (health.overallHealth / 100) * circumference;
  const riskStyle = riskColors[health.riskLevel] ?? { bg: '#f1f5f9', color: '#475569' };

  return (
    <div style={{ padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 24 }} aria-label="Product health dashboard">
      <div style={{ display: 'flex', alignItems: 'center', gap: 32, flexWrap: 'wrap' }}>
        <div style={{ position: 'relative', width: 140, height: 140 }} aria-label={`Overall health score: ${health.overallHealth}%`}>
          <svg width="140" height="140" viewBox="0 0 140 140" aria-hidden="true">
            <circle cx="70" cy="70" r="58" fill="none" stroke="var(--color-bg-surface-raised)" strokeWidth="10" />
            <circle cx="70" cy="70" r="58" fill="none" stroke={ringColor(health.overallHealth)} strokeWidth="10"
              strokeDasharray={circumference} strokeDashoffset={offset} transform="rotate(-90 70 70)" strokeLinecap="round" />
          </svg>
          <div style={{ position: 'absolute', inset: 0, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
            <span style={{ fontSize: 'var(--text-h1)', fontWeight: 700, color: 'var(--color-text-primary)', lineHeight: 1 }}>{health.overallHealth}</span>
            <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>Health</span>
          </div>
        </div>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Product Health</h2>
          <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, padding: '4px 12px', borderRadius: 'var(--radius-sm)', fontSize: 'var(--text-body-xs)', fontWeight: 600, background: riskStyle.bg, color: riskStyle.color }}>
              <span style={{ width: 8, height: 8, borderRadius: '50%', background: riskStyle.color }} aria-hidden="true" />
              {health.riskLevel.charAt(0).toUpperCase() + health.riskLevel.slice(1)} Risk
            </span>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, padding: '4px 12px', borderRadius: 'var(--radius-sm)', fontSize: 'var(--text-body-xs)', fontWeight: 600, background: 'var(--color-bg-surface-raised)', color: 'var(--color-text-secondary)' }}>
              {certLabels[health.certificationStatus] ?? health.certificationStatus}
            </span>
          </div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 12 }}>
        {categories.map((cat) => {
          const val = health[cat.key] as number;
          return (
            <div key={cat.key} style={{ padding: 14, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 6 }}>
                <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>{cat.label}</span>
                <span style={{ fontSize: 'var(--text-body-xs)', fontWeight: 700, color: scoreColor(val) }}>{val}%</span>
              </div>
              <div style={{ height: 8, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', overflow: 'hidden' }}>
                <div style={{ height: '100%', width: `${val}%`, borderRadius: 'var(--radius-xs)', background: scoreColor(val), transition: 'width 0.4s' }} />
              </div>
            </div>
          );
        })}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 12 }}>
        <div style={{ padding: 14, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 2 }}>
          <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>Total Products</span>
          <span style={{ fontSize: 'var(--text-h4)', fontWeight: 700, color: 'var(--color-text-primary)' }}>{health.totalProducts}</span>
        </div>
        <div style={{ padding: 14, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 2 }}>
          <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>Validated</span>
          <span style={{ fontSize: 'var(--text-h4)', fontWeight: 700, color: 'var(--color-success)' }}>{health.validatedProducts}</span>
        </div>
        <div style={{ padding: 14, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 2 }}>
          <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>Failed</span>
          <span style={{ fontSize: 'var(--text-h4)', fontWeight: 700, color: 'var(--color-danger)' }}>{health.failedProducts}</span>
        </div>
      </div>
    </div>
  );
});
