import React from 'react';
import type { AiReadinessScore } from '../types';

const sect: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };

function Meter({ label, score }: { label: string; score: number }) {
  const color = score >= 80 ? 'var(--color-accent-green)' : score >= 60 ? 'var(--color-accent-blue)' : score >= 40 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)';
  return (
    <div style={{ marginBottom: 10 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-xs)', marginBottom: 2 }}>
        <span style={{ color: 'var(--color-text-secondary)' }}>{label}</span>
        <span style={{ fontWeight: 600, color }}>{score}%</span>
      </div>
      <div style={{ height: 10, borderRadius: 5, background: 'var(--color-bg-surface-raised)', overflow: 'hidden' }}>
        <div style={{ height: '100%', width: `${score}%`, borderRadius: 5, background: color, transition: 'width 0.3s' }} />
      </div>
    </div>
  );
}

export const AiReadinessDashboard: React.FC<{ ai: AiReadinessScore }> = React.memo(({ ai }) => {
  const categories = [
    { label: 'GEO Readiness', score: ai.geoReady },
    { label: 'AEO Readiness', score: ai.aeoReady },
    { label: 'Structured Content', score: ai.structuredContent },
    { label: 'Semantic Headings', score: ai.semanticHeadings },
    { label: 'Entity Coverage', score: ai.entityCoverage },
    { label: 'FAQ Quality', score: ai.faqQuality },
    { label: 'Knowledge Graph Readiness', score: ai.kgReady },
    { label: 'Content Completeness', score: ai.contentCompleteness },
  ];

  return (
    <div style={sect}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>AI Search Readiness</h2>
      <div style={{ display: 'flex', alignItems: 'center', gap: 16, padding: 20, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
        <div style={{
          width: 100, height: 100, borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
          border: '8px solid', borderColor: ai.overall >= 70 ? 'var(--color-accent-green)' : ai.overall >= 50 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)',
          fontSize: 'var(--text-h1)', fontWeight: 700,
          color: ai.overall >= 70 ? 'var(--color-accent-green)' : ai.overall >= 50 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)',
        }}>{ai.overall}</div>
        <div>
          <div style={{ fontSize: 'var(--text-h4)', fontWeight: 600, color: 'var(--color-text-primary)' }}>AI Search Readiness Score</div>
          <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', marginTop: 4 }}>
            Measures preparedness for Generative Engine Optimization (GEO), Answer Engine Optimization (AEO), and AI-powered search engines.
          </div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 12 }}>Readiness Categories</h3>
          {categories.map((c) => <Meter key={c.label} label={c.label} score={c.score} />)}
        </div>

        <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>Recommendations</h3>
          {ai.recommendations.map((r, i) => (
            <div key={i} style={{ padding: '6px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body-xs)', color: 'var(--color-text-primary)', display: 'flex', gap: 6 }}>
              <span style={{ color: 'var(--color-accent-blue)' }}>→</span> {r}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
});
