import type { KnowledgeResult } from './types/trainer';

function scoreColor(score: number): string {
  if (score >= 0.9) return 'var(--cp-color-success)';
  if (score >= 0.7) return 'var(--cp-color-primary)';
  if (score >= 0.5) return 'var(--cp-color-warning)';
  return 'var(--cp-color-text-muted)';
}

export function TrainerKnowledgeCard({ result }: { result: KnowledgeResult }) {
  return (
    <div style={{ border: '1px solid var(--cp-color-border)', borderRadius: 8, padding: 16, background: 'var(--cp-color-surface)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 8 }}>
        <h3 style={{ margin: 0, fontSize: 15, color: 'var(--cp-color-text)' }}>{result.title}</h3>
        <span style={{
          background: scoreColor(result.relevanceScore),
          color: '#fff', padding: '2px 8px', borderRadius: 4, fontSize: 11, fontWeight: 600, whiteSpace: 'nowrap',
        }}>
          {Math.round(result.relevanceScore * 100)}%
        </span>
      </div>
      <div style={{ fontSize: 13, color: 'var(--cp-color-text-secondary)', marginBottom: 8, lineHeight: 1.5 }}>
        {result.content}
      </div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: 11, color: 'var(--cp-color-text-muted)' }}>
        <span>Source: <strong>{result.source}</strong></span>
        <span style={{ fontStyle: 'italic' }}>{result.citation}</span>
      </div>
    </div>
  );
}
