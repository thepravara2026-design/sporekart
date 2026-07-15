import React from 'react';
import type { CatalogHealth } from '../types';
import { ProgressChart } from './charts/ProgressChart';
import { ScoreCard } from './charts/ScoreCard';

interface CatalogHealthDashboardProps {
  health: CatalogHealth;
}

const labels: { key: keyof CatalogHealth; label: string }[] = [
  { key: 'completion', label: 'Completion' },
  { key: 'validation', label: 'Validation' },
  { key: 'publishing', label: 'Publishing' },
  { key: 'seo', label: 'SEO' },
  { key: 'media', label: 'Media' },
  { key: 'compliance', label: 'Compliance' },
  { key: 'marketplace', label: 'Marketplace' },
  { key: 'accessibility', label: 'Accessibility' },
  { key: 'aiReadiness', label: 'AI Readiness' },
];

export const CatalogHealthDashboard: React.FC<CatalogHealthDashboardProps> = React.memo(({ health }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }} role="region" aria-label="Catalog health dashboard">
    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '32px 0' }}>
      <ScoreCard label="Overall Health" value={health.overall} unit="%" icon="📊" color="var(--color-accent-blue)" />
    </div>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: 'var(--space-component-gap)' }} role="list" aria-label="Health category scores">
      {labels.map(({ key, label }) => (
        <ProgressChart key={key} label={label} value={health[key]} max={100} color={health[key] >= 80 ? 'var(--color-accent-green)' : health[key] >= 60 ? 'var(--color-accent-yellow)' : 'var(--color-accent-red)'} />
      ))}
    </div>
  </div>
));

export default CatalogHealthDashboard;
