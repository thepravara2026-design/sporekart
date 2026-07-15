import React, { useMemo } from 'react';
import type { PackagingValidationResult } from '../types';
import { MOCK_VALIDATION_SCORES } from '../mock/mockValidation';

interface PackagingValidatorProps {
  selectedId: string | null;
}

function generatePackagingResult(productId: string): PackagingValidationResult {
  const product = MOCK_VALIDATION_SCORES.find((p) => p.productId === productId);
  const name = product?.productName ?? 'Unknown Product';
  const seed = productId.charCodeAt(productId.length - 1) || 0;
  const ok = (offset: number) => (seed + offset) % 3 !== 0;
  const issues: string[] = [];
  if (!ok(1)) issues.push('Product weight not specified');
  if (!ok(2)) issues.push('Package dimensions missing');
  if (!ok(3)) issues.push('Packaging material not defined');
  if (!ok(4)) issues.push('Storage requirements not specified');
  if (!ok(5)) issues.push('Handling instructions missing');
  if (!ok(6)) issues.push('Packaging notes incomplete');
  if (!ok(7)) issues.push('Shipping notes not provided');
  if (!ok(8)) issues.push('Product label information incomplete');
  return {
    productId, productName: name,
    weightOk: ok(1), dimensionsOk: ok(2), materialOk: ok(3),
    storageOk: ok(4), handlingOk: ok(5), packagingNotesOk: ok(6),
    shippingNotesOk: ok(7), labelComplete: ok(8),
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
      <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>Packaging Score</div>
    </div>
  );
}

export const PackagingValidator: React.FC<PackagingValidatorProps> = React.memo(({ selectedId }) => {
  const result = useMemo(() => selectedId ? generatePackagingResult(selectedId) : null, [selectedId]);

  if (!result) {
    return (
      <div style={section}>
        <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Packaging Validation</h2>
        <div style={{ padding: 40, textAlign: 'center', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 32, marginBottom: 8, opacity: 0.3, color: 'var(--color-text-tertiary)' }}>📋</div>
          <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>No product selected for packaging validation</div>
        </div>
      </div>
    );
  }

  const checks: Array<{ label: string; ok: boolean }> = [
    { label: 'Weight', ok: result.weightOk },
    { label: 'Dimensions', ok: result.dimensionsOk },
    { label: 'Material', ok: result.materialOk },
    { label: 'Storage Requirements', ok: result.storageOk },
    { label: 'Handling Instructions', ok: result.handlingOk },
    { label: 'Packaging Notes', ok: result.packagingNotesOk },
    { label: 'Shipping Notes', ok: result.shippingNotesOk },
    { label: 'Label Complete', ok: result.labelComplete },
  ];

  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Packaging Validation</h2>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', marginBottom: 4 }}>{result.productName}</div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: 16 }}>
        <ScoreDisplay score={result.score} />

        <div style={panel} aria-label="Packaging checks">
          <div style={panelTitle}>Packaging Checks</div>
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
