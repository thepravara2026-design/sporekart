import React, { useCallback } from 'react';
import type { ComplianceResult } from '../types';

interface ComplianceCheckerProps {
  selectedCompliance: ComplianceResult | null;
  scores: Array<{ productId: string; productName: string }>;
  selectedId: string | null;
  onSelect: (id: string) => void;
}

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const panel: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' };
const panelTitle: React.CSSProperties = { fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 12 };
const selectStyle: React.CSSProperties = { padding: '8px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)', width: '100%' };
const icon: React.CSSProperties = { fontSize: 16, width: 20, textAlign: 'center' };

function ScoreRing({ score }: { score: number }) {
  const color = score >= 80 ? 'var(--color-accent-green)' : score >= 60 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)';
  return (
    <div style={{ textAlign: 'center', padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 8 }}>
      <div style={{ width: 80, height: 80, borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', border: `6px solid ${color}`, fontSize: 'var(--text-h3)', fontWeight: 700, color }}>{score}</div>
      <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>Compliance Score</div>
    </div>
  );
}

function StatusIcon({ status }: { status: 'pass' | 'fail' | 'warning' | 'na' }) {
  return (
    <span style={icon} aria-label={status}>
      {status === 'pass' && '✓'}
      {status === 'fail' && '✗'}
      {status === 'warning' && '⚠'}
      {status === 'na' && '—'}
    </span>
  );
}

function groupChecks(checks: ComplianceResult['checks']) {
  const groups: Record<string, typeof checks> = {};
  for (const c of checks) {
    if (!groups[c.category]) groups[c.category] = [];
    groups[c.category].push(c);
  }
  return groups;
}

export const ComplianceChecker: React.FC<ComplianceCheckerProps> = React.memo(({ selectedCompliance, scores, selectedId, onSelect }) => {
  const handleSelect = useCallback((e: React.ChangeEvent<HTMLSelectElement>) => onSelect(e.target.value), [onSelect]);

  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Compliance Checker</h2>
      <select value={selectedId ?? ''} onChange={handleSelect} style={selectStyle} aria-label="Select product for compliance check">
        <option value="" disabled>Select a product...</option>
        {scores.map((s) => <option key={s.productId} value={s.productId}>{s.productName}</option>)}
      </select>

      {!selectedCompliance ? (
        <div style={{ padding: 40, textAlign: 'center', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 32, marginBottom: 8, opacity: 0.3, color: 'var(--color-text-tertiary)' }}>🛡️</div>
          <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>Select a product to view compliance details</div>
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: 16 }}>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
            <ScoreRing score={selectedCompliance.overallScore} />

            <div style={panel} aria-label="Required approvals">
              <div style={panelTitle}>Required Approvals</div>
              {selectedCompliance.requiredApprovals.length === 0 ? (
                <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>No approvals required</div>
              ) : (
                selectedCompliance.requiredApprovals.map((a, i) => (
                  <div key={i} style={{ display: 'flex', gap: 6, padding: '4px 0', fontSize: 'var(--text-body-xs)', color: 'var(--color-text-primary)' }}>
                    <span style={{ color: 'var(--color-accent-blue)' }}>→</span> {a}
                  </div>
                ))
              )}
            </div>

            <div style={panel} aria-label="Warnings">
              <div style={panelTitle}>Warnings</div>
              {selectedCompliance.warnings.length === 0 ? (
                <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>No warnings</div>
              ) : (
                selectedCompliance.warnings.map((w, i) => (
                  <div key={i} style={{ display: 'flex', gap: 6, padding: '4px 0', fontSize: 'var(--text-body-xs)', color: 'var(--color-accent-orange)' }}>
                    <span>⚠</span> {w}
                  </div>
                ))
              )}
            </div>
          </div>

          <div style={panel} aria-label="Compliance checks">
            <div style={panelTitle}>Compliance Checks</div>
            {Object.entries(groupChecks(selectedCompliance.checks)).map(([cat, items]) => (
              <div key={cat} style={{ marginBottom: 12 }}>
                <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', textTransform: 'capitalize', marginBottom: 4, fontWeight: 600 }}>{cat.replace(/_/g, ' ')}</div>
                {items.map((c) => (
                  <div key={c.id} style={{ display: 'flex', alignItems: 'flex-start', gap: 8, padding: '6px 0', borderBottom: '1px solid var(--color-border)' }}>
                    <StatusIcon status={c.status} />
                    <div style={{ flex: 1, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{c.label}</div>
                    <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', maxWidth: 200, textAlign: 'right' }}>{c.message}</div>
                  </div>
                ))}
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
});
