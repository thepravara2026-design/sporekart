import React from 'react';
import type { MarketplaceAnalytic } from '../types';
import { BarChart } from './charts/BarChart';
import { ScoreCard } from './charts/ScoreCard';

interface MarketplaceAnalyticsViewProps {
  marketplace: MarketplaceAnalytic;
}

export const MarketplaceAnalyticsView: React.FC<MarketplaceAnalyticsViewProps> = React.memo(({ marketplace }) => {
  const channelData = {
    labels: ['Amazon', 'Flipkart', 'Google Shopping', 'AgriBegri', 'IndiaMART'],
    series: [{ name: 'Readiness %', data: [marketplace.amazon, marketplace.flipkart, marketplace.googleShopping, marketplace.agriBegri, marketplace.indiaMART], color: 'var(--color-accent-orange)' }],
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }} role="region" aria-label="Marketplace analytics">
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-component-gap)' }}>
        <ScoreCard label="Avg Readiness" value={marketplace.avgScore} unit="%" icon="🛒" color="var(--color-accent-orange)" />
      </div>
      <BarChart config={channelData} />
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          <h3 style={{ margin: 0, fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600 }}>Missing Requirements</h3>
          <ul style={{ margin: 0, padding: 0, listStyle: 'none', display: 'flex', flexDirection: 'column', gap: 6 }}>
            {marketplace.missingRequirements.map((r, i) => (
              <li key={i} style={{ padding: '10px 12px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{r}</li>
            ))}
          </ul>
        </div>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          <h3 style={{ margin: 0, fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600 }}>Warnings</h3>
          <ul style={{ margin: 0, padding: 0, listStyle: 'none', display: 'flex', flexDirection: 'column', gap: 6 }}>
            {marketplace.warnings.map((w, i) => (
              <li key={i} style={{ padding: '10px 12px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{w}</li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
});

export default MarketplaceAnalyticsView;
