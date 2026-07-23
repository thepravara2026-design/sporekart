import React, { useState } from 'react';
import { KnowledgeArticle } from '../../types/customer';

interface KnowledgeCitationCardProps {
  article: KnowledgeArticle;
}

function relevanceColor(score: number): string {
  if (score >= 0.9) return '#22c55e';
  if (score >= 0.7) return '#f59e0b';
  return '#ef4444';
}

export default function KnowledgeCitationCard({ article }: KnowledgeCitationCardProps) {
  const [expanded, setExpanded] = useState(false);

  return (
    <div style={{
      border: '1px solid #e5e7eb',
      borderRadius: '8px',
      padding: '12px',
      background: '#fff',
      fontFamily: 'system-ui, sans-serif',
      maxWidth: '380px',
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '6px' }}>
        <h4 style={{ margin: '0', fontSize: '14px', fontWeight: 600, color: '#111827', flex: 1 }}>
          {article.title}
        </h4>
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '4px',
          marginLeft: '8px',
          flexShrink: 0,
        }}>
          <div style={{
            width: '8px',
            height: '8px',
            borderRadius: '50%',
            background: relevanceColor(article.relevanceScore),
          }} />
          <span style={{ fontSize: '11px', color: '#6b7280', fontWeight: 500 }}>
            {Math.round(article.relevanceScore * 100)}%
          </span>
        </div>
      </div>

      <p style={{
        margin: '0 0 8px',
        fontSize: '13px',
        color: '#6b7280',
        lineHeight: '1.4',
      }}>
        {article.snippet}
      </p>

      {expanded && (
        <div style={{
          marginBottom: '8px',
          padding: '8px',
          background: '#f9fafb',
          borderRadius: '6px',
          fontSize: '13px',
          color: '#374151',
          lineHeight: '1.5',
        }}>
          {article.content}
        </div>
      )}

      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
          <span style={{ fontSize: '11px', color: '#9ca3af' }}>Source:</span>
          <span style={{ fontSize: '12px', color: '#374151', fontWeight: 500 }}>
            {article.source}
          </span>
        </div>
        <button
          onClick={() => setExpanded(!expanded)}
          style={{
            background: 'none',
            border: 'none',
            color: '#2563eb',
            fontSize: '12px',
            fontWeight: 600,
            cursor: 'pointer',
            padding: '2px 4px',
          }}
        >
          {expanded ? 'Show less' : 'Read more'}
        </button>
      </div>

      {article.url && (
        <a
          href={article.url}
          target="_blank"
          rel="noopener noreferrer"
          style={{
            display: 'block',
            marginTop: '8px',
            fontSize: '12px',
            color: '#2563eb',
            textDecoration: 'none',
            fontWeight: 500,
          }}
        >
          View full article ↗
        </a>
      )}
    </div>
  );
}
