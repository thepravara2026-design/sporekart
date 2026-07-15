import React, { useState, useMemo } from 'react';
import type { InsightRecommendation } from '../types';

interface InsightsPanelProps {
  insights: InsightRecommendation[];
}

const insightTypes = ['All', 'SEO', 'Images', 'Publishing', 'Marketplace', 'Quality'] as const;
type InsightFilter = (typeof insightTypes)[number];

const typeMap: Record<string, string> = {
  seo: 'SEO', images: 'Images', duplicate: 'Duplicates', category: 'Category', publishing: 'Publishing', marketplace: 'Marketplace', quality: 'Quality',
};

const severityIcon: Record<string, string> = { critical: '🔴', warning: '🟠', info: '🔵' };
const severityOrder: Record<string, number> = { critical: 0, warning: 1, info: 2 };

export const InsightsPanel: React.FC<InsightsPanelProps> = React.memo(({ insights }) => {
  const [filter, setFilter] = useState<InsightFilter>('All');

  const filtered = useMemo(() => {
    const f = filter === 'All' ? insights : insights.filter((i) => typeMap[i.type] === filter);
    return [...f].sort((a, b) => (severityOrder[a.severity] ?? 9) - (severityOrder[b.severity] ?? 9));
  }, [insights, filter]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }} role="region" aria-label="Insights panel">
      <div role="tablist" aria-label="Filter by type" style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
        {insightTypes.map((t) => (
          <button key={t} role="tab" aria-selected={filter === t} onClick={() => setFilter(t)}
            style={{ padding: '6px 14px', borderRadius: 20, border: filter === t ? '2px solid var(--color-accent-blue)' : '1px solid var(--color-border)', background: filter === t ? 'color-mix(in srgb, var(--color-accent-blue) 10%, transparent)' : 'var(--color-bg-surface-default)', color: filter === t ? 'var(--color-accent-blue)' : 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)', cursor: 'pointer', fontWeight: filter === t ? 600 : 400 }}>
            {t}
          </button>
        ))}
      </div>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }} role="list" aria-label="Insight recommendations">
        {filtered.length === 0 && (
          <div style={{ padding: 32, textAlign: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-sm)' }}>No insights found for this filter.</div>
        )}
        {filtered.map((insight) => (
          <div key={insight.id} style={{ display: 'flex', alignItems: 'flex-start', gap: 12, padding: 14, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }} role="listitem">
            <span style={{ fontSize: 18, lineHeight: '22px' }} aria-hidden="true">{severityIcon[insight.severity] ?? '⚪'}</span>
            <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 4 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8, flexWrap: 'wrap' }}>
                <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{insight.message}</span>
                {insight.productCount > 0 && (
                  <span style={{ fontSize: 'var(--text-caption)', padding: '2px 8px', borderRadius: 12, background: 'color-mix(in srgb, var(--color-accent-red) 15%, transparent)', color: 'var(--color-accent-red)', fontWeight: 500 }}>{insight.productCount} products</span>
                )}
              </div>
              <button style={{ alignSelf: 'flex-start', padding: '4px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-raised)', color: 'var(--color-accent-blue)', fontSize: 'var(--text-caption)', cursor: 'pointer' }}>{insight.action}</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});

export default InsightsPanel;
