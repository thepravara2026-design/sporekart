import React from 'react';
import type { SeoAnalytic } from '../types';
import { ProgressChart } from './charts/ProgressChart';
import { ScoreCard } from './charts/ScoreCard';

interface SeoAnalyticsViewProps {
  seo: SeoAnalytic;
}

const coverageItems: { key: keyof SeoAnalytic; label: string }[] = [
  { key: 'metaCoverage', label: 'Meta Coverage' },
  { key: 'schemaCoverage', label: 'Schema Coverage' },
  { key: 'slugCoverage', label: 'Slug Coverage' },
  { key: 'canonicalCoverage', label: 'Canonical Coverage' },
  { key: 'ogCoverage', label: 'OG Coverage' },
  { key: 'aiReadiness', label: 'AI Readiness' },
];

export const SeoAnalyticsView: React.FC<SeoAnalyticsViewProps> = React.memo(({ seo }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }} role="region" aria-label="SEO analytics">
    <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-component-gap)' }}>
      <ScoreCard label="Avg SEO Score" value={seo.avgScore} unit="%" icon="🔍" color="var(--color-accent-blue)" />
      <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', flex: 1 }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Products Missing SEO</span>
        <p style={{ margin: '4px 0 0', fontSize: 'var(--text-h5)', color: seo.missingSeo > 0 ? 'var(--color-accent-red)' : 'var(--color-accent-green)', fontWeight: 600 }}>{seo.missingSeo}<span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', fontWeight: 400 }}> / {seo.totalProducts}</span></p>
      </div>
    </div>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: 'var(--space-component-gap)' }} role="list" aria-label="SEO coverage scores">
      {coverageItems.map(({ key, label }) => (
        <ProgressChart key={key} label={label} value={seo[key] as number} max={100} color={(seo[key] as number) >= 80 ? 'var(--color-accent-green)' : (seo[key] as number) >= 60 ? 'var(--color-accent-yellow)' : 'var(--color-accent-red)'} />
      ))}
    </div>
  </div>
));

export default SeoAnalyticsView;
