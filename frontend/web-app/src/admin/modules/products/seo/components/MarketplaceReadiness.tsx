import React from 'react';
import type { SeoEntry } from '../types';
import type { MarketplaceConfig } from '../mock/mockMarketplace';

interface MarketplaceReadinessProps {
  entry: SeoEntry | null;
  configs: MarketplaceConfig[];
}

const sect: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 12 };
const card: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' };

export const MarketplaceReadiness: React.FC<MarketplaceReadinessProps> = React.memo(({ entry, configs }) => {
  return (
    <div style={sect}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Marketplace Readiness</h2>
      {!entry ? (
        <div style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>Select a product to view marketplace readiness</div>
      ) : (
        <>
          <div style={{ display: 'flex', gap: 12, alignItems: 'center', marginBottom: 8 }}>
            <span style={{ fontSize: 'var(--text-h4)', fontWeight: 700, color: entry.marketplaceScore >= 70 ? 'var(--color-accent-green)' : entry.marketplaceScore >= 50 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)' }}>
              {entry.marketplaceScore}%
            </span>
            <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>Overall Marketplace Readiness Score</span>
          </div>
          <div style={grid}>
            {configs.map((cfg) => {
              const mp = entry.marketplaceStatus[cfg.id as keyof typeof entry.marketplaceStatus];
              if (!mp) return null;
              return (
                <div key={cfg.id} style={card}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 8 }}>
                    <span style={{ fontSize: 24 }}>{cfg.icon}</span>
                    <div>
                      <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{cfg.name}</div>
                      <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>{cfg.url}</div>
                    </div>
                    <div style={{ marginLeft: 'auto' }}>
                      <span style={{
                        padding: '2px 10px', borderRadius: 10, fontSize: 'var(--text-body-xs)', fontWeight: 700,
                        background: mp.ready ? 'var(--color-accent-green)20' : 'var(--color-accent-orange)20',
                        color: mp.ready ? 'var(--color-accent-green)' : 'var(--color-accent-orange)',
                      }}>
                        {mp.ready ? 'Ready' : 'Needs Work'}
                      </span>
                    </div>
                  </div>
                  <div style={{ marginBottom: 6 }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-xs)', marginBottom: 2 }}>
                      <span style={{ color: 'var(--color-text-tertiary)' }}>Score</span>
                      <span style={{ fontWeight: 600 }}>{mp.score}%</span>
                    </div>
                    <div style={{ height: 6, borderRadius: 3, background: 'var(--color-bg-surface-raised)', overflow: 'hidden' }}>
                      <div style={{ height: '100%', width: `${mp.score}%`, borderRadius: 3, background: mp.score >= 70 ? 'var(--color-accent-green)' : mp.score >= 50 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)' }} />
                    </div>
                  </div>
                  {mp.missing.length > 0 && (
                    <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-accent-orange)' }}>
                      Missing: {mp.missing.join(', ')}
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        </>
      )}
    </div>
  );
});
