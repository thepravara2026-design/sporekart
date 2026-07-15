import React from 'react';
import type { PublishingAnalytic, ChartConfig } from '../types';
import { DonutChart } from './charts/DonutChart';
import { ScoreCard } from './charts/ScoreCard';

interface PublishingAnalyticsViewProps {
  publishing: PublishingAnalytic;
  chartConfig: ChartConfig;
}

const statusItems: { key: keyof PublishingAnalytic; label: string; color: string }[] = [
  { key: 'published', label: 'Published', color: 'var(--color-accent-green)' },
  { key: 'draft', label: 'Draft', color: 'var(--color-accent-yellow)' },
  { key: 'review', label: 'Review', color: 'var(--color-accent-blue)' },
  { key: 'scheduled', label: 'Scheduled', color: 'var(--color-accent-purple)' },
  { key: 'archived', label: 'Archived', color: 'var(--color-accent-red)' },
  { key: 'rejected', label: 'Rejected', color: 'var(--color-accent-orange)' },
  { key: 'pending', label: 'Pending', color: 'var(--color-accent-cyan)' },
];

export const PublishingAnalyticsView: React.FC<PublishingAnalyticsViewProps> = React.memo(({ publishing, chartConfig }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }} role="region" aria-label="Publishing analytics">
    <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-component-gap)' }}>
      <ScoreCard label="Publishing Health" value={publishing.health} unit="%" icon="📤" color={publishing.health >= 80 ? 'var(--color-accent-green)' : publishing.health >= 60 ? 'var(--color-accent-yellow)' : 'var(--color-accent-red)'} />
    </div>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(120px, 1fr))', gap: 'var(--space-component-gap)' }}>
      {statusItems.filter(({ key }) => key !== 'health').map(({ key, label, color }) => (
        <div key={key} style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', textAlign: 'center' }}>
          <span style={{ fontSize: 'var(--text-h5)', color, fontWeight: 600 }}>{publishing[key] as number}</span>
          <p style={{ margin: '4px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</p>
        </div>
      ))}
    </div>
    <DonutChart config={chartConfig} />
  </div>
));

export default PublishingAnalyticsView;
