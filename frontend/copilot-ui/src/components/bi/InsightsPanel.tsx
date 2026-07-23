import React, { useState } from 'react';
import type { BusinessInsight } from './types/bi';

interface InsightsPanelProps { insights: BusinessInsight[]; }

type SeverityTab = 'ALL' | 'CRITICAL' | 'IMPORTANT' | 'INFO';

function SeverityBadge({ severity }: { severity: string }) {
  const colors: Record<string, { bg: string; text: string }> = {
    CRITICAL: { bg: '#fef2f2', text: '#ef4444' },
    IMPORTANT: { bg: '#fff7ed', text: '#f97316' },
    INFO: { bg: '#eff6ff', text: '#3b82f6' },
  };
  const c = colors[severity.toUpperCase()] || { bg: '#f3f4f6', text: '#6b7280' };
  return <span style={{ background: c.bg, color: c.text, padding: '2px 10px', borderRadius: 4, fontSize: 11, fontWeight: 600 }}>{severity}</span>;
}

export default function InsightsPanel({ insights }: InsightsPanelProps) {
  const [activeTab, setActiveTab] = useState<SeverityTab>('ALL');
  const [expanded, setExpanded] = useState<Set<string>>(new Set());

  const tabs: SeverityTab[] = ['ALL', 'CRITICAL', 'IMPORTANT', 'INFO'];
  const filtered = activeTab === 'ALL' ? insights : insights.filter(i => i.severity.toUpperCase() === activeTab);

  const toggleExpand = (id: string) => {
    setExpanded(prev => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id); else next.add(id);
      return next;
    });
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div style={{ display: 'flex', gap: 4, borderBottom: '1px solid #e5e7eb', paddingBottom: 8 }}>
        {tabs.map(tab => (
          <button key={tab} onClick={() => setActiveTab(tab)}
            style={{ padding: '6px 16px', border: 'none', borderRadius: 6, cursor: 'pointer', fontSize: 13, fontWeight: 600,
              background: activeTab === tab ? '#3b82f6' : 'transparent', color: activeTab === tab ? '#fff' : '#6b7280' }}>
            {tab}
          </button>
        ))}
      </div>
      {filtered.length === 0 && <div style={{ textAlign: 'center', padding: 32, color: '#9ca3af', fontSize: 14 }}>No insights found</div>}
      {filtered.map(insight => (
        <div key={insight.insightId} style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 8 }}>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 4, flex: 1 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <SeverityBadge severity={insight.severity} />
                <span style={{ fontSize: 11, color: '#9ca3af' }}>{insight.category}</span>
              </div>
              <h4 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: 0 }}>{insight.title}</h4>
            </div>
          </div>
          <p style={{ fontSize: 13, color: '#6b7280', margin: '0 0 12px', lineHeight: 1.4 }}>{insight.description}</p>
          <div style={{ display: 'flex', alignItems: 'center', gap: 16, marginBottom: 12 }}>
            <div style={{ flex: 1 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 11, color: '#6b7280', marginBottom: 2 }}>
                <span>Confidence</span>
                <span>{insight.confidenceScore}%</span>
              </div>
              <div style={{ height: 6, background: '#e5e7eb', borderRadius: 3, overflow: 'hidden' }}>
                <div style={{ width: `${insight.confidenceScore}%`, height: '100%', background: insight.confidenceScore >= 80 ? '#22c55e' : insight.confidenceScore >= 60 ? '#f97316' : '#ef4444', borderRadius: 3 }} />
              </div>
            </div>
            <span style={{ fontSize: 12, color: '#6b7280' }}>Impact: {insight.businessImpact}</span>
          </div>
          {insight.actionItems.length > 0 && (
            <div>
              <button onClick={() => toggleExpand(insight.insightId)} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 12, color: '#3b82f6', fontWeight: 600, padding: 0 }}>
                {expanded.has(insight.insightId) ? 'Hide' : 'Show'} Action Items ({insight.actionItems.length})
              </button>
              {expanded.has(insight.insightId) && (
                <ul style={{ margin: '8px 0 0', paddingLeft: 20, fontSize: 12, color: '#6b7280', display: 'flex', flexDirection: 'column', gap: 4 }}>
                  {insight.actionItems.map((item, i) => <li key={i}>{item}</li>)}
                </ul>
              )}
            </div>
          )}
        </div>
      ))}
    </div>
  );
}
