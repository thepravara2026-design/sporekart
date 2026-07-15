import React from 'react';
import type { ProductKpi } from '../types';
import { ScoreCard } from './charts/ScoreCard';

interface KpiCenterProps {
  kpi: ProductKpi;
}

const kpiDefinitions: { key: keyof ProductKpi; label: string; icon: string }[] = [
  { key: 'catalogGrowth', label: 'Catalog Growth', icon: '📦' },
  { key: 'productCompletion', label: 'Product Completion', icon: '✅' },
  { key: 'avgSeo', label: 'Avg SEO Score', icon: '🔍' },
  { key: 'avgValidation', label: 'Avg Validation', icon: '🛡️' },
  { key: 'avgCompliance', label: 'Avg Compliance', icon: '📋' },
  { key: 'avgMarketplace', label: 'Avg Marketplace', icon: '🛒' },
  { key: 'avgAccessibility', label: 'Avg Accessibility', icon: '♿' },
  { key: 'avgQuality', label: 'Avg Quality', icon: '⭐' },
];

function kpiColor(value: number): string {
  if (value >= 80) return 'var(--color-accent-green)';
  if (value >= 60) return 'var(--color-accent-yellow)';
  return 'var(--color-accent-red)';
}

export const KpiCenter: React.FC<KpiCenterProps> = React.memo(({ kpi }) => (
  <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 'var(--space-component-gap)' }} role="list" aria-label="KPI center scores">
    {kpiDefinitions.map(({ key, label, icon }) => (
      <ScoreCard key={key} label={label} value={kpi[key]} unit={key === 'catalogGrowth' ? undefined : '%'} icon={icon} color={kpiColor(kpi[key])} />
    ))}
  </div>
));

export default KpiCenter;
