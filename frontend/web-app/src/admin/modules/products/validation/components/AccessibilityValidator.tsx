import React, { useMemo } from 'react';
import type { AccessibilityValidationResult } from '../types';

interface AccessibilityValidatorProps {
  selectedId: string | null;
}

function generateMock(id: string | null): AccessibilityValidationResult {
  const map: Record<string, Partial<AccessibilityValidationResult>> = {
    'prod-001': { altTextOk: true, headingHierarchyOk: true, ariaOk: false, contrastOk: true, keyboardOk: true, score: 85, issues: ['Missing ARIA labels on navigation elements', 'Focus indicator too subtle on product cards'] },
    'prod-003': { altTextOk: true, headingHierarchyOk: true, ariaOk: true, contrastOk: true, keyboardOk: true, score: 100, issues: [] },
    'prod-005': { altTextOk: true, headingHierarchyOk: false, ariaOk: true, contrastOk: true, keyboardOk: false, score: 75, issues: ['Heading hierarchy skips from H2 to H4 on product details', 'Keyboard trap in checkout modal'] },
    'prod-009': { altTextOk: false, headingHierarchyOk: false, ariaOk: false, contrastOk: true, keyboardOk: false, score: 45, issues: ['Missing alt text on 3 product images', 'No H1 tag on page', 'ARIA roles incorrectly applied to interactive elements', 'Keyboard navigation broken in variant selector'] },
    'prod-007': { altTextOk: true, headingHierarchyOk: true, ariaOk: true, contrastOk: true, keyboardOk: true, score: 95, issues: [] },
  };
  const base: AccessibilityValidationResult = {
    productId: id ?? 'prod-000',
    productName: 'Unknown Product',
    altTextOk: false, headingHierarchyOk: false, ariaOk: false, contrastOk: false, keyboardOk: false, score: 0, issues: ['No validation data'],
  };
  if (!id || !map[id]) return { ...base, productName: id ? 'Product Not Found' : 'No Product Selected' };
  return { ...base, ...map[id] };
}

const checksConfig = [
  { key: 'altTextOk', label: 'Alt Text', desc: 'All images have descriptive alt attributes' },
  { key: 'headingHierarchyOk', label: 'Heading Hierarchy', desc: 'Headings follow logical H1-H6 order' },
  { key: 'ariaOk', label: 'ARIA Attributes', desc: 'ARIA roles and labels are correctly applied' },
  { key: 'contrastOk', label: 'Color Contrast', desc: 'Text and background meet WCAG contrast ratios' },
  { key: 'keyboardOk', label: 'Keyboard Navigation', desc: 'All interactive elements are keyboard accessible' },
] as const;

function scoreColor(s: number): string {
  if (s >= 90) return 'var(--color-success)';
  if (s >= 70) return 'var(--color-warning)';
  return 'var(--color-danger)';
}

export const AccessibilityValidator = React.memo(function AccessibilityValidator({ selectedId }: AccessibilityValidatorProps) {
  const result = useMemo(() => generateMock(selectedId), [selectedId]);

  return (
    <div style={{ padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 20 }} aria-label="Accessibility validation result">
      <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
        <div style={{ position: 'relative', width: 80, height: 80 }}>
          <svg width="80" height="80" viewBox="0 0 80 80" aria-hidden="true">
            <circle cx="40" cy="40" r="36" fill="none" stroke="var(--color-bg-surface-raised)" strokeWidth="6" />
            <circle cx="40" cy="40" r="36" fill="none" stroke={scoreColor(result.score)} strokeWidth="6"
              strokeDasharray={`${(result.score / 100) * 226} 226`} transform="rotate(-90 40 40)" strokeLinecap="round" />
          </svg>
          <span style={{ position: 'absolute', inset: 0, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 'var(--text-h4)', fontWeight: 700, color: scoreColor(result.score) }}>
            {result.score}
          </span>
        </div>
        <div>
          <div style={{ fontSize: 'var(--text-h4)', fontWeight: 600, color: 'var(--color-text-primary)' }}>Accessibility Score</div>
          <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{result.productName}</div>
        </div>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }} role="list" aria-label="Accessibility checks">
        {checksConfig.map((c) => {
          const passed = result[c.key as keyof typeof result] as boolean;
          return (
            <div key={c.key} role="listitem" style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '10px 12px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
              <span style={{ fontSize: 16, color: passed ? 'var(--color-success)' : 'var(--color-danger)' }} aria-label={passed ? 'Passed' : 'Failed'}>
                {passed ? '✓' : '✗'}
              </span>
              <div style={{ flex: 1 }}>
                <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{c.label}</div>
                <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>{c.desc}</div>
              </div>
            </div>
          );
        })}
      </div>

      {result.issues.length > 0 && (
        <div>
          <div style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>Issues ({result.issues.length})</div>
          <ul style={{ margin: 0, padding: 0, listStyle: 'none', display: 'flex', flexDirection: 'column', gap: 6 }}>
            {result.issues.map((issue, i) => (
              <li key={i} style={{ display: 'flex', gap: 8, padding: '8px 12px', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg-surface-raised)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
                <span style={{ color: 'var(--color-danger)', flexShrink: 0 }}>●</span>
                {issue}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
});
