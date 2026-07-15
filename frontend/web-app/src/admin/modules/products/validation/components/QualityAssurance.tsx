import React, { useCallback } from 'react';
import type { QualityCheckResult } from '../types';

interface QualityAssuranceProps {
  selectedQuality: QualityCheckResult | null;
  scores: Array<{ productId: string; productName: string }>;
  selectedId: string | null;
  onSelect: (id: string) => void;
}

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const panel: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' };
const selectStyle: React.CSSProperties = { padding: '8px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)', width: '100%' };
const icon: React.CSSProperties = { fontSize: 16, width: 20, textAlign: 'center' };
const badgeBase: React.CSSProperties = { display: 'inline-flex', alignItems: 'center', gap: 6, padding: '4px 10px', borderRadius: 'var(--radius-sm)', fontSize: 'var(--text-body-xs)', fontWeight: 600 };

function StatusIcon({ status }: { status: 'pass' | 'fail' | 'warning' }) {
  return (
    <span style={icon} aria-label={status}>
      {status === 'pass' && '✓'}
      {status === 'fail' && '✗'}
      {status === 'warning' && '⚠'}
    </span>
  );
}

export const QualityAssurance: React.FC<QualityAssuranceProps> = React.memo(({ selectedQuality, scores, selectedId, onSelect }) => {
  const handleSelect = useCallback((e: React.ChangeEvent<HTMLSelectElement>) => onSelect(e.target.value), [onSelect]);

  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Quality Assurance</h2>
      <select value={selectedId ?? ''} onChange={handleSelect} style={selectStyle} aria-label="Select product for quality check">
        <option value="" disabled>Select a product...</option>
        {scores.map((s) => <option key={s.productId} value={s.productId}>{s.productName}</option>)}
      </select>

      {!selectedQuality ? (
        <div style={{ padding: 40, textAlign: 'center', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 32, marginBottom: 8, opacity: 0.3, color: 'var(--color-text-tertiary)' }}>✓</div>
          <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>Select a product to view quality checks</div>
        </div>
      ) : (
        <>
          <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
            <div style={{ ...badgeBase, color: 'var(--color-accent-green)', background: 'var(--color-bg-surface-raised)' }}>
              <span>✓</span> {selectedQuality.passed} Passed
            </div>
            <div style={{ ...badgeBase, color: 'var(--color-accent-red)', background: 'var(--color-bg-surface-raised)' }}>
              <span>✗</span> {selectedQuality.failed} Failed
            </div>
            <div style={{ ...badgeBase, color: 'var(--color-text-secondary)', background: 'var(--color-bg-surface-raised)' }}>
              <span>∑</span> {selectedQuality.total} Total
            </div>
            <div style={{
              ...badgeBase, color: selectedQuality.score >= 80 ? 'var(--color-accent-green)' : selectedQuality.score >= 50 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)',
              background: 'var(--color-bg-surface-raised)',
            }}>
              Score: {selectedQuality.score}/100
            </div>
          </div>

          <div style={panel} aria-label="Quality checks">
            {selectedQuality.checks.map((c) => (
              <div key={c.id} style={{ display: 'flex', alignItems: 'flex-start', gap: 8, padding: '8px 0', borderBottom: '1px solid var(--color-border)' }}>
                <StatusIcon status={c.status} />
                <div style={{ flex: 1 }}>
                  <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', fontWeight: 500 }}>{c.label}</div>
                  <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>{c.message}</div>
                </div>
                <span style={{
                  fontSize: 'var(--text-body-xs)', padding: '2px 6px', borderRadius: 'var(--radius-xs)',
                  background: 'var(--color-bg-surface-raised)', color: 'var(--color-text-secondary)',
                }}>{c.category}</span>
              </div>
            ))}
          </div>
        </>
      )}
    </div>
  );
});
