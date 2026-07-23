import React from 'react';
import { BusinessInsight } from '../types/admin';

interface AdminInsightsListProps {
  insights: BusinessInsight[];
  onInsightClick?: (insight: BusinessInsight) => void;
}

const categoryColors: Record<string, { bg: string; text: string }> = {
  sales: { bg: '#dbeafe', text: '#1d4ed8' },
  customer: { bg: '#d1fae5', text: '#047857' },
  inventory: { bg: '#fef3c7', text: '#b45309' },
  risk: { bg: '#fce4ec', text: '#c62828' }
};

const severityOrder: Record<string, number> = {
  critical: 0,
  warning: 1,
  info: 2
};

const AdminInsightsList: React.FC<AdminInsightsListProps> = ({ insights, onInsightClick }) => {
  const [expandedId, setExpandedId] = React.useState<number | null>(null);

  const sorted = [...insights].sort(
    (a, b) => (severityOrder[a.severity] ?? 2) - (severityOrder[b.severity] ?? 2)
  );

  const grouped: Record<string, BusinessInsight[]> = {};
  for (const insight of sorted) {
    const category = insight.metric || 'general';
    if (!grouped[category]) grouped[category] = [];
    grouped[category].push(insight);
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
      <h3 style={{ fontSize: '16px', fontWeight: 600, color: '#111827', margin: 0 }}>Business Insights</h3>

      {Object.entries(grouped).map(([category, items]) => {
        const colors = categoryColors[category.toLowerCase()] || { bg: '#f3f4f6', text: '#374151' };
        return (
          <div key={category} style={{
            backgroundColor: '#ffffff',
            borderRadius: '12px',
            border: '1px solid #e5e7eb',
            overflow: 'hidden'
          }}>
            <div style={{
              padding: '12px 16px',
              backgroundColor: colors.bg,
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center'
            }}>
              <span style={{ fontWeight: 600, fontSize: '14px', color: colors.text, textTransform: 'capitalize' }}>
                {category}
              </span>
              <span style={{
                backgroundColor: 'rgba(255,255,255,0.7)',
                padding: '2px 8px',
                borderRadius: '10px',
                fontSize: '12px',
                fontWeight: 600,
                color: colors.text
              }}>
                {items.length}
              </span>
            </div>

            {items.map((insight, idx) => {
              const isExpanded = expandedId === idx;
              const severityColor = insight.severity === 'critical' ? '#dc2626'
                : insight.severity === 'warning' ? '#f59e0b'
                : '#3b82f6';

              return (
                <div key={idx} style={{
                  padding: '12px 16px',
                  borderTop: '1px solid #f3f4f6',
                  cursor: onInsightClick ? 'pointer' : 'default'
                }}
                  onClick={() => {
                    if (onInsightClick) onInsightClick(insight);
                    setExpandedId(isExpanded ? null : idx);
                  }}
                >
                  <div style={{ display: 'flex', alignItems: 'flex-start', gap: '10px' }}>
                    <div style={{
                      width: '10px',
                      height: '10px',
                      borderRadius: '50%',
                      backgroundColor: severityColor,
                      marginTop: '4px',
                      flexShrink: 0
                    }} />
                    <div style={{ flex: 1 }}>
                      <div style={{ fontSize: '13px', color: '#4b5563', lineHeight: 1.5 }}>
                        {insight.summary}
                      </div>

                      {isExpanded && (
                        <div style={{ marginTop: '10px', padding: '10px', backgroundColor: '#f9fafb', borderRadius: '8px' }}>
                          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '8px', fontSize: '13px' }}>
                            <div><strong style={{ color: '#6b7280' }}>Current:</strong> {insight.currentValue}</div>
                            <div><strong style={{ color: '#6b7280' }}>Previous:</strong> {insight.previousValue}</div>
                            <div><strong style={{ color: '#6b7280' }}>Change:</strong>
                              <span style={{ color: insight.trend === 'up' ? '#16a34a' : insight.trend === 'down' ? '#dc2626' : '#6b7280' }}>
                                {insight.change > 0 ? '+' : ''}{insight.change}%
                              </span>
                            </div>
                            <div><strong style={{ color: '#6b7280' }}>Trend:</strong>
                              <span style={{
                                display: 'inline-block',
                                marginLeft: '4px',
                                color: insight.trend === 'up' ? '#16a34a' : insight.trend === 'down' ? '#dc2626' : '#6b7280'
                              }}>
                                {insight.trend === 'up' ? '\u2191 Up' : insight.trend === 'down' ? '\u2193 Down' : '\u2192 Stable'}
                              </span>
                            </div>
                          </div>
                          {insight.recommendation && (
                            <div style={{ marginTop: '8px', fontSize: '13px', color: '#2563eb', fontStyle: 'italic' }}>
                              Recommendation: {insight.recommendation}
                            </div>
                          )}
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        );
      })}
    </div>
  );
};

export default AdminInsightsList;
