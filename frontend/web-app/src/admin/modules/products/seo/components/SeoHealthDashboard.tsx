import React from 'react';
import type { SeoHealthScore } from '../types';

const sect: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };

function ScoreRing({ score, label }: { score: number; label: string }) {
  const color = score >= 80 ? 'var(--color-accent-green)' : score >= 60 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)';
  return (
    <div style={{ textAlign: 'center', padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{
        width: 80, height: 80, borderRadius: '50%', margin: '0 auto 8px', display: 'flex', alignItems: 'center', justifyContent: 'center',
        border: `6px solid ${color}`, fontSize: 'var(--text-h3)', fontWeight: 700, color,
      }}>{score}</div>
      <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>{label}</div>
    </div>
  );
}

function CategoryBar({ label, score }: { label: string; score: number }) {
  const color = score >= 80 ? 'var(--color-accent-green)' : score >= 60 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)';
  return (
    <div style={{ marginBottom: 8 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-xs)', marginBottom: 2 }}>
        <span style={{ color: 'var(--color-text-secondary)' }}>{label}</span>
        <span style={{ fontWeight: 600, color }}>{score}%</span>
      </div>
      <div style={{ height: 8, borderRadius: 4, background: 'var(--color-bg-surface-raised)', overflow: 'hidden' }}>
        <div style={{ height: '100%', width: `${score}%`, borderRadius: 4, background: color, transition: 'width 0.3s' }} />
      </div>
    </div>
  );
}

export const SeoHealthDashboard: React.FC<{ health: SeoHealthScore }> = React.memo(({ health }) => {
  const categories = [
    { label: 'Title', score: health.title },
    { label: 'Description', score: health.description },
    { label: 'Slug', score: health.slug },
    { label: 'Images', score: health.images },
    { label: 'Keywords', score: health.keywords },
    { label: 'Structured Data', score: health.structuredData },
    { label: 'Schema', score: health.schema },
    { label: 'URL', score: health.url },
    { label: 'Canonical', score: health.canonical },
    { label: 'Accessibility', score: health.accessibility },
    { label: 'Performance', score: health.performance },
    { label: 'Content Quality', score: health.contentQuality },
  ];

  return (
    <div style={sect}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>SEO Health Dashboard</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 12 }}>
        <ScoreRing score={health.overall} label="Overall SEO Health" />
        {categories.slice(0, 5).map((c) => <ScoreRing key={c.label} score={c.score} label={c.label} />)}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 12 }}>Category Scores</h3>
          {categories.map((c) => <CategoryBar key={c.label} label={c.label} score={c.score} />)}
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
          <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
            <h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>Recommendations</h3>
            {health.recommendations.map((r, i) => (
              <div key={i} style={{ padding: '6px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body-xs)', color: 'var(--color-text-primary)', display: 'flex', gap: 6 }}>
                <span style={{ color: 'var(--color-accent-blue)' }}>→</span> {r}
              </div>
            ))}
          </div>

          <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
            <h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>Warnings</h3>
            {health.warnings.map((w, i) => (
              <div key={i} style={{ padding: '6px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body-xs)', color: 'var(--color-accent-orange)', display: 'flex', gap: 6 }}>
                <span>⚠</span> {w}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
});
