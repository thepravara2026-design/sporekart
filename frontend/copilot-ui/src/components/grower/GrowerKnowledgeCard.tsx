import React, { useState } from 'react';
import type { KnowledgeArticle } from './types/grower';

const cardStyle: React.CSSProperties = {
  padding: '16px',
  background: 'var(--cp-surface, #1a1a2e)',
  borderRadius: '12px',
  border: '1px solid var(--cp-border, #2a2a4a)',
};

const titleStyle: React.CSSProperties = {
  fontSize: '15px',
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
  margin: '0 0 8px',
};

const badgeRowStyle: React.CSSProperties = {
  display: 'flex',
  gap: '6px',
  marginBottom: 8,
};

const badgeStyle = (color: string): React.CSSProperties => ({
  padding: '2px 8px',
  borderRadius: '12px',
  fontSize: '10px',
  fontWeight: 600,
  background: 'var(--cp-surface-alt, #16213e)',
  color,
  border: `1px solid ${color}`,
});

const citationStyle: React.CSSProperties = {
  fontSize: '11px',
  fontStyle: 'italic',
  color: 'var(--cp-text-dim, #888)',
  margin: '0 0 10px',
};

const contentPreviewStyle: React.CSSProperties = {
  fontSize: '12px',
  color: 'var(--cp-text-dim, #aaa)',
  lineHeight: 1.5,
  margin: 0,
  cursor: 'pointer',
};

const expandToggleStyle: React.CSSProperties = {
  marginTop: 8,
  fontSize: '11px',
  color: 'var(--cp-accent, #4ade80)',
  cursor: 'pointer',
  border: 'none',
  background: 'none',
  padding: 0,
  textDecoration: 'underline',
};

interface GrowerKnowledgeCardProps {
  article: KnowledgeArticle;
}

export default function GrowerKnowledgeCard({ article }: GrowerKnowledgeCardProps) {
  const [expanded, setExpanded] = useState(false);
  const previewLength = 120;
  const isLong = article.content.length > previewLength;

  return (
    <div style={cardStyle}>
      <h4 style={titleStyle}>{article.title}</h4>

      <div style={badgeRowStyle}>
        <span style={badgeStyle('var(--cp-accent, #4ade80)')}>{article.source}</span>
        <span style={badgeStyle('var(--cp-text-dim, #888)')}>{article.type}</span>
      </div>

      <p style={citationStyle}>📖 {article.citation}</p>

      <div onClick={() => isLong && setExpanded(!expanded)} style={contentPreviewStyle}>
        {expanded || !isLong ? article.content : `${article.content.slice(0, previewLength)}...`}
      </div>

      {isLong && (
        <button
          onClick={() => setExpanded(!expanded)}
          style={expandToggleStyle}
        >
          {expanded ? 'Show less' : 'Read more'}
        </button>
      )}
    </div>
  );
}
