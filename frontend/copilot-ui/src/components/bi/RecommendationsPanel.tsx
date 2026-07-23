import React, { useState } from 'react';
import type { DecisionRecommendation } from './types/bi';

interface RecommendationsPanelProps { recommendations: DecisionRecommendation[]; }

function PriorityBadge({ priority }: { priority: string }) {
  const colors: Record<string, { bg: string; text: string }> = {
    critical: { bg: '#fef2f2', text: '#ef4444' },
    high: { bg: '#fff7ed', text: '#f97316' },
    medium: { bg: '#fefce8', text: '#eab308' },
    low: { bg: '#f0fdf4', text: '#22c55e' },
  };
  const c = colors[priority.toLowerCase()] || { bg: '#f3f4f6', text: '#6b7280' };
  return <span style={{ background: c.bg, color: c.text, padding: '2px 10px', borderRadius: 4, fontSize: 11, fontWeight: 600 }}>{priority}</span>;
}

export default function RecommendationsPanel({ recommendations }: RecommendationsPanelProps) {
  const [filter, setFilter] = useState<string>('ALL');
  const [expanded, setExpanded] = useState<Set<string>>(new Set());
  const [completed, setCompleted] = useState<Set<string>>(new Set());

  const priorities = ['ALL', ...new Set(recommendations.map(r => r.priority))];
  const filtered = filter === 'ALL' ? recommendations : recommendations.filter(r => r.priority.toUpperCase() === filter.toUpperCase());

  const toggleExpand = (id: string) => {
    setExpanded(prev => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id); else next.add(id);
      return next;
    });
  };

  const toggleItem = (id: string, item: string) => {
    const key = `${id}::${item}`;
    setCompleted(prev => {
      const next = new Set(prev);
      if (next.has(key)) next.delete(key); else next.add(key);
      return next;
    });
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div style={{ display: 'flex', gap: 4, borderBottom: '1px solid #e5e7eb', paddingBottom: 8 }}>
        {priorities.map(p => (
          <button key={p} onClick={() => setFilter(p)}
            style={{ padding: '6px 16px', border: 'none', borderRadius: 6, cursor: 'pointer', fontSize: 13, fontWeight: 600,
              background: filter === p ? '#3b82f6' : 'transparent', color: filter === p ? '#fff' : '#6b7280' }}>
            {p}
          </button>
        ))}
      </div>
      {filtered.length === 0 && <div style={{ textAlign: 'center', padding: 32, color: '#9ca3af', fontSize: 14 }}>No recommendations found</div>}
      {filtered.map(rec => (
        <div key={rec.recommendationId} style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 8 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <PriorityBadge priority={rec.priority} />
              <span style={{ fontSize: 11, color: '#9ca3af' }}>{rec.category}</span>
            </div>
            <span style={{ fontSize: 12, color: '#6b7280', background: '#f3f4f6', padding: '2px 8px', borderRadius: 4 }}>Impact: {rec.expectedImpact}</span>
          </div>
          <h4 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 4px' }}>{rec.title}</h4>
          <p style={{ fontSize: 13, color: '#6b7280', margin: '0 0 12px', lineHeight: 1.4 }}>{rec.description}</p>
          <div style={{ display: 'flex', alignItems: 'center', gap: 16, marginBottom: 12 }}>
            <div style={{ flex: 1 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 11, color: '#6b7280', marginBottom: 2 }}>
                <span>Confidence</span>
                <span>{rec.confidenceScore}%</span>
              </div>
              <div style={{ height: 6, background: '#e5e7eb', borderRadius: 3, overflow: 'hidden' }}>
                <div style={{ width: `${rec.confidenceScore}%`, height: '100%', background: rec.confidenceScore >= 80 ? '#22c55e' : rec.confidenceScore >= 60 ? '#f97316' : '#ef4444', borderRadius: 3 }} />
              </div>
            </div>
          </div>
          {rec.rationale && (
            <div style={{ marginBottom: 12 }}>
              <button onClick={() => toggleExpand(rec.recommendationId)} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 12, color: '#3b82f6', fontWeight: 600, padding: 0 }}>
                {expanded.has(rec.recommendationId) ? 'Hide' : 'Show'} Rationale
              </button>
              {expanded.has(rec.recommendationId) && (
                <p style={{ fontSize: 12, color: '#6b7280', margin: '8px 0 0', lineHeight: 1.4, background: '#f9fafb', padding: 12, borderRadius: 6 }}>{rec.rationale}</p>
              )}
            </div>
          )}
          {rec.actionItems.length > 0 && (
            <div style={{ marginBottom: 12 }}>
              <span style={{ fontSize: 12, color: '#6b7280', fontWeight: 600 }}>Action Items:</span>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 4, marginTop: 6 }}>
                {rec.actionItems.map((item, i) => {
                  const key = `${rec.recommendationId}::${item}`;
                  return (
                    <label key={i} style={{ display: 'flex', alignItems: 'center', gap: 8, fontSize: 12, color: '#374151', cursor: 'pointer' }}>
                      <input type="checkbox" checked={completed.has(key)} onChange={() => toggleItem(rec.recommendationId, item)} style={{ accentColor: '#3b82f6' }} />
                      <span style={{ textDecoration: completed.has(key) ? 'line-through' : 'none', color: completed.has(key) ? '#9ca3af' : '#374151' }}>{item}</span>
                    </label>
                  );
                })}
              </div>
            </div>
          )}
          <button style={{ padding: '8px 20px', background: '#3b82f6', color: '#fff', border: 'none', borderRadius: 6, cursor: 'pointer', fontSize: 13, fontWeight: 600 }}>
            Implement
          </button>
        </div>
      ))}
    </div>
  );
}
