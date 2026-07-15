import React, { useMemo } from 'react';
import type { AiReadinessValidationResult } from '../types';

interface AiReadinessValidatorProps {
  selectedId: string | null;
}

function generateMock(id: string | null): AiReadinessValidationResult {
  const map: Record<string, Partial<AiReadinessValidationResult>> = {
    'prod-001': { entityCoverageOk: true, structuredContentOk: true, faqOk: true, semanticStructureOk: true, knowledgeGraphOk: false, contextQualityOk: true, contentRichnessOk: false, score: 72, issues: ['No knowledge graph markup for product entities', 'Content richness below threshold — add more detailed descriptions'] },
    'prod-003': { entityCoverageOk: true, structuredContentOk: true, faqOk: true, semanticStructureOk: true, knowledgeGraphOk: true, contextQualityOk: true, contentRichnessOk: true, score: 92, issues: [] },
    'prod-005': { entityCoverageOk: true, structuredContentOk: true, faqOk: false, semanticStructureOk: true, knowledgeGraphOk: false, contextQualityOk: true, contentRichnessOk: true, score: 78, issues: ['No FAQ schema defined', 'Missing knowledge graph connection to agricultural taxonomy'] },
    'prod-009': { entityCoverageOk: false, structuredContentOk: false, faqOk: false, semanticStructureOk: false, knowledgeGraphOk: false, contextQualityOk: false, contentRichnessOk: false, score: 28, issues: ['No entity annotations found', 'Content is plain text with no structured markup', 'FAQ section missing entirely', 'No semantic HTML structure', 'Empty or missing knowledge graph entries', 'Poor contextual relevance scoring', 'Content length too short for rich extraction'] },
    'prod-007': { entityCoverageOk: true, structuredContentOk: true, faqOk: true, semanticStructureOk: true, knowledgeGraphOk: true, contextQualityOk: true, contentRichnessOk: false, score: 88, issues: ['Content richness could be improved with multimedia elements'] },
  };
  const base: AiReadinessValidationResult = {
    productId: id ?? 'prod-000',
    productName: 'Unknown Product',
    entityCoverageOk: false, structuredContentOk: false, faqOk: false, semanticStructureOk: false, knowledgeGraphOk: false, contextQualityOk: false, contentRichnessOk: false, score: 0, issues: ['No validation data'],
  };
  if (!id || !map[id]) return { ...base, productName: id ? 'Product Not Found' : 'No Product Selected' };
  return { ...base, ...map[id] };
}

const checksConfig = [
  { key: 'entityCoverageOk', label: 'Entity Coverage', desc: 'Product entities annotated for AI extraction' },
  { key: 'structuredContentOk', label: 'Structured Content', desc: 'Content uses structured data formats' },
  { key: 'faqOk', label: 'FAQ / Q&A', desc: 'Frequently asked questions defined' },
  { key: 'semanticStructureOk', label: 'Semantic Structure', desc: 'HTML uses semantic elements' },
  { key: 'knowledgeGraphOk', label: 'Knowledge Graph', desc: 'Entities linked in knowledge graph' },
  { key: 'contextQualityOk', label: 'Context Quality', desc: 'Content provides sufficient context' },
  { key: 'contentRichnessOk', label: 'Content Richness', desc: 'Rich media and detailed descriptions present' },
] as const;

function scoreColor(s: number): string {
  if (s >= 90) return 'var(--color-success)';
  if (s >= 70) return 'var(--color-warning)';
  return 'var(--color-danger)';
}

export const AiReadinessValidator = React.memo(function AiReadinessValidator({ selectedId }: AiReadinessValidatorProps) {
  const result = useMemo(() => generateMock(selectedId), [selectedId]);

  return (
    <div style={{ padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 20 }} aria-label="AI readiness validation result">
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
          <div style={{ fontSize: 'var(--text-h4)', fontWeight: 600, color: 'var(--color-text-primary)' }}>AI Readiness Score</div>
          <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{result.productName}</div>
        </div>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }} role="list" aria-label="AI readiness checks">
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
