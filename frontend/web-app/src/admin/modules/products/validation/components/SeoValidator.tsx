import React, { useMemo } from 'react';
import type { SeoValidationResult } from '../types';
import { MOCK_VALIDATION_SCORES } from '../mock/mockValidation';

interface SeoValidatorProps {
  selectedId: string | null;
}

function generateSeoResult(productId: string): SeoValidationResult {
  const product = MOCK_VALIDATION_SCORES.find((p) => p.productId === productId);
  const name = product?.productName ?? 'Unknown Product';
  const seed = productId.charCodeAt(productId.length - 1) || 0;
  const ok = (offset: number) => (seed + offset) % 3 !== 0;
  const issues: string[] = [];
  if (!ok(1)) issues.push('Meta title is missing or too short');
  if (!ok(2)) issues.push('Meta description is missing or too short');
  if (!ok(3)) issues.push('URL slug is not optimized');
  if (!ok(4)) issues.push('Target keywords not defined');
  if (!ok(5)) issues.push('Canonical URL not set');
  if (!ok(6)) issues.push('Structured data missing');
  if (!ok(7)) issues.push('Schema markup not configured');
  if (!ok(8)) issues.push('Open Graph tags missing');
  if (!ok(9)) issues.push('Twitter card not configured');
  if (!ok(10)) issues.push('Content length below recommended minimum');
  if (!ok(11)) issues.push('Heading structure is not hierarchical');
  if (!ok(12)) issues.push('GEO optimization not applied');
  if (!ok(13)) issues.push('AEO (Answer Engine Optimization) not configured');
  return {
    productId, productName: name,
    titleOk: ok(1), descriptionOk: ok(2), slugOk: ok(3), keywordsOk: ok(4),
    canonicalOk: ok(5), structuredDataOk: ok(6), schemaOk: ok(7),
    ogOk: ok(8), twitterOk: ok(9), contentLengthOk: ok(10),
    headingStructureOk: ok(11), geoOk: ok(12), aeoOk: ok(13),
    score: Math.round([ok(1), ok(2), ok(3), ok(4), ok(5), ok(6), ok(7), ok(8), ok(9), ok(10), ok(11), ok(12), ok(13)].filter(Boolean).length / 13 * 100),
    issues,
  };
}

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const panel: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' };
const panelTitle: React.CSSProperties = { fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 12 };
const icon: React.CSSProperties = { fontSize: 16, width: 20, textAlign: 'center' };

function ScoreDisplay({ score }: { score: number }) {
  const color = score >= 80 ? 'var(--color-accent-green)' : score >= 50 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)';
  return (
    <div style={{ textAlign: 'center', padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 8 }}>
      <div style={{ width: 80, height: 80, borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', border: `6px solid ${color}`, fontSize: 'var(--text-h3)', fontWeight: 700, color }}>{score}</div>
      <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>SEO Score</div>
    </div>
  );
}

export const SeoValidator: React.FC<SeoValidatorProps> = React.memo(({ selectedId }) => {
  const result = useMemo(() => selectedId ? generateSeoResult(selectedId) : null, [selectedId]);

  if (!result) {
    return (
      <div style={section}>
        <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>SEO Validation</h2>
        <div style={{ padding: 40, textAlign: 'center', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 32, marginBottom: 8, opacity: 0.3, color: 'var(--color-text-tertiary)' }}>🔍</div>
          <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>No product selected for SEO validation</div>
        </div>
      </div>
    );
  }

  const checks: Array<{ label: string; ok: boolean }> = [
    { label: 'Meta Title', ok: result.titleOk },
    { label: 'Meta Description', ok: result.descriptionOk },
    { label: 'URL Slug', ok: result.slugOk },
    { label: 'Target Keywords', ok: result.keywordsOk },
    { label: 'Canonical URL', ok: result.canonicalOk },
    { label: 'Structured Data', ok: result.structuredDataOk },
    { label: 'Schema Markup', ok: result.schemaOk },
    { label: 'Open Graph Tags', ok: result.ogOk },
    { label: 'Twitter Card', ok: result.twitterOk },
    { label: 'Content Length', ok: result.contentLengthOk },
    { label: 'Heading Structure', ok: result.headingStructureOk },
    { label: 'GEO Optimization', ok: result.geoOk },
    { label: 'AEO Configuration', ok: result.aeoOk },
  ];

  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>SEO Validation</h2>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', marginBottom: 4 }}>{result.productName}</div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: 16 }}>
        <ScoreDisplay score={result.score} />

        <div style={panel} aria-label="SEO checks">
          <div style={panelTitle}>SEO Checks</div>
          {checks.map((c) => (
            <div key={c.label} style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '8px 0', borderBottom: '1px solid var(--color-border)' }}>
              <span style={{ ...icon, color: c.ok ? 'var(--color-accent-green)' : 'var(--color-accent-red)' }} aria-label={c.ok ? 'pass' : 'fail'}>
                {c.ok ? '✓' : '✗'}
              </span>
              <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{c.label}</div>
            </div>
          ))}
        </div>
      </div>

      {result.issues.length > 0 && (
        <div style={panel} aria-label="Issues">
          <div style={panelTitle}>Issues</div>
          {result.issues.map((issue, i) => (
            <div key={i} style={{ display: 'flex', gap: 6, padding: '6px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body-xs)', color: 'var(--color-accent-red)' }}>
              <span>✗</span> {issue}
            </div>
          ))}
        </div>
      )}
    </div>
  );
});
