import React, { useMemo } from 'react';
import type { MediaValidationResult } from '../types';
import { MOCK_VALIDATION_SCORES } from '../mock/mockValidation';

interface MediaValidatorProps {
  selectedId: string | null;
}

function generateMediaResult(productId: string): MediaValidationResult {
  const product = MOCK_VALIDATION_SCORES.find((p) => p.productId === productId);
  const name = product?.productName ?? 'Unknown Product';
  const seed = productId.charCodeAt(productId.length - 1) || 0;
  const ok = (offset: number) => (seed + offset) % 3 !== 0;
  const issues: string[] = [];
  if (!ok(1)) issues.push('Primary image missing or not uploaded');
  if (!ok(2)) issues.push('Gallery has fewer than 3 images');
  if (!ok(3)) issues.push('Image resolution below minimum requirement (800x800)');
  if (!ok(4)) issues.push('Aspect ratio is not 1:1');
  if (!ok(5)) issues.push('File naming convention not followed');
  if (!ok(6)) issues.push('Alt text missing on one or more images');
  if (!ok(7)) issues.push('No product video attached');
  if (!ok(8)) issues.push('Required documents not uploaded');
  return {
    productId, productName: name,
    primaryImage: ok(1), galleryCount: ok(2) ? 5 : 2, minGalleryRequired: 3,
    resolutionOk: ok(3), aspectRatioOk: ok(4), namingOk: ok(5),
    altTextPresent: ok(6), videoPresent: ok(7), documentsPresent: ok(8),
    qualityOk: ok(1) && ok(3) && ok(4),
    score: Math.round([ok(1), ok(2), ok(3), ok(4), ok(5), ok(6), ok(7), ok(8)].filter(Boolean).length / 8 * 100),
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
      <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>Media Score</div>
    </div>
  );
}

export const MediaValidator: React.FC<MediaValidatorProps> = React.memo(({ selectedId }) => {
  const result = useMemo(() => selectedId ? generateMediaResult(selectedId) : null, [selectedId]);

  if (!result) {
    return (
      <div style={section}>
        <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Media Validation</h2>
        <div style={{ padding: 40, textAlign: 'center', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 32, marginBottom: 8, opacity: 0.3, color: 'var(--color-text-tertiary)' }}>🖼️</div>
          <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>No product selected for media validation</div>
        </div>
      </div>
    );
  }

  const checks: Array<{ label: string; ok: boolean; detail: string }> = [
    { label: 'Primary Image', ok: result.primaryImage, detail: result.primaryImage ? 'Primary image uploaded' : 'Primary image missing' },
    { label: `Gallery (${result.galleryCount}/${result.minGalleryRequired} min)`, ok: result.galleryCount >= result.minGalleryRequired, detail: `${result.galleryCount} images in gallery` },
    { label: 'Resolution (800x800 min)', ok: result.resolutionOk, detail: result.resolutionOk ? 'Meets resolution requirement' : 'Below minimum resolution' },
    { label: 'Aspect Ratio (1:1)', ok: result.aspectRatioOk, detail: result.aspectRatioOk ? 'Correct aspect ratio' : 'Aspect ratio not 1:1' },
    { label: 'File Naming Convention', ok: result.namingOk, detail: result.namingOk ? 'Naming follows convention' : 'Naming convention not followed' },
    { label: 'Alt Text', ok: result.altTextPresent, detail: result.altTextPresent ? 'Alt text present' : 'Alt text missing' },
    { label: 'Product Video', ok: result.videoPresent, detail: result.videoPresent ? 'Video attached' : 'No video attached' },
    { label: 'Documents', ok: result.documentsPresent, detail: result.documentsPresent ? 'Documents uploaded' : 'Documents missing' },
  ];

  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Media Validation</h2>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', marginBottom: 4 }}>{result.productName}</div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: 16 }}>
        <ScoreDisplay score={result.score} />

        <div style={panel} aria-label="Media checks">
          <div style={panelTitle}>Media Checks</div>
          {checks.map((c) => (
            <div key={c.label} style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '8px 0', borderBottom: '1px solid var(--color-border)' }}>
              <span style={{ ...icon, color: c.ok ? 'var(--color-accent-green)' : 'var(--color-accent-red)' }} aria-label={c.ok ? 'pass' : 'fail'}>
                {c.ok ? '✓' : '✗'}
              </span>
              <div style={{ flex: 1, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{c.label}</div>
              <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>{c.detail}</div>
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
