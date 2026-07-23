import { useState } from 'react';
import type { KnowledgeReference } from '../types';

interface CopilotKnowledgeReferencesProps {
  references: KnowledgeReference[];
}

export function CopilotKnowledgeReferences({ references }: CopilotKnowledgeReferencesProps) {
  const [expandedIds, setExpandedIds] = useState<Set<string>>(new Set());

  const toggleExpand = (id: string) => {
    setExpandedIds((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  };

  const scoreColor = (score: number): string => {
    if (score >= 0.8) return 'var(--cp-color-success)';
    if (score >= 0.5) return 'var(--cp-color-warning)';
    return 'var(--cp-color-text-muted)';
  };

  if (references.length === 0) {
    return <div className="cp-knowledge__empty">No references</div>;
  }

  return (
    <div className="cp-knowledge" role="list" aria-label="Knowledge references">
      {references.map((ref) => {
        const isExpanded = expandedIds.has(ref.id);
        return (
          <div key={ref.id} className="cp-knowledge__item" role="listitem">
            <button
              type="button"
              className="cp-knowledge__header"
              onClick={() => toggleExpand(ref.id)}
              aria-expanded={isExpanded}
            >
              <div className="cp-knowledge__title">{ref.title}</div>
              <div className="cp-knowledge__meta">
                <span className="cp-knowledge__source">{ref.source}</span>
                <span
                  className="cp-knowledge__score"
                  style={{ color: scoreColor(ref.relevanceScore) }}
                  aria-label={`Relevance: ${Math.round(ref.relevanceScore * 100)}%`}
                >
                  {Math.round(ref.relevanceScore * 100)}%
                </span>
              </div>
            </button>
            {isExpanded && (
              <div className="cp-knowledge__body">
                <p className="cp-knowledge__snippet">{ref.snippet}</p>
                {ref.url && (
                  <a
                    href={ref.url}
                    className="cp-knowledge__link"
                    target="_blank"
                    rel="noopener noreferrer"
                  >
                    Open source &rarr;
                  </a>
                )}
              </div>
            )}
          </div>
        );
      })}
    </div>
  );
}
