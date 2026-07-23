import React from 'react';
import type { BusinessInsight } from './types/bi';

const SEVERITY_STYLES: Record<string, { border: string; badge: string; bg: string }> = {
  CRITICAL: { border: '#ef4444', badge: '#ef4444', bg: '#fef2f2' },
  IMPORTANT: { border: '#f97316', badge: '#f97316', bg: '#fff7ed' },
  INFO: { border: '#3b82f6', badge: '#3b82f6', bg: '#eff6ff' },
};

interface BiInsightCardProps {
  insight: BusinessInsight;
}

export const BiInsightCard: React.FC<BiInsightCardProps> = ({ insight }) => {
  const style = SEVERITY_STYLES[insight.severity] || SEVERITY_STYLES.INFO;

  return (
    <div style={{
      background: '#fff',
      borderRadius: 8,
      padding: 16,
      border: `1px solid ${style.border}`,
      borderLeft: `4px solid ${style.border}`,
      boxShadow: '0 1px 3px rgba(0,0,0,0.08)',
    }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 8 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <span style={{
            background: style.badge,
            color: '#fff',
            fontSize: 10,
            fontWeight: 700,
            padding: '2px 8px',
            borderRadius: 10,
            textTransform: 'uppercase',
            letterSpacing: 0.5,
          }}>
            {insight.severity}
          </span>
          <span style={{ fontSize: 11, color: '#6b7280' }}>{insight.category}</span>
        </div>
        {insight.actionable && (
          <span style={{ fontSize: 11, color: '#22c55e', fontWeight: 600 }}>Actionable</span>
        )}
      </div>
      <h4 style={{ margin: '0 0 6px 0', fontSize: 15, fontWeight: 600, color: '#111827' }}>{insight.title}</h4>
      <p style={{ margin: '0 0 10px 0', fontSize: 13, color: '#6b7280', lineHeight: 1.4 }}>{insight.description}</p>
      {insight.recommendation && (
        <div style={{
          background: '#f9fafb',
          borderRadius: 6,
          padding: '8px 10px',
          marginBottom: 10,
          fontSize: 12,
          color: '#374151',
          border: '1px solid #e5e7eb',
        }}>
          <strong>Recommendation:</strong> {insight.recommendation}
        </div>
      )}
      <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
        <div style={{ flex: 1, height: 6, background: '#f3f4f6', borderRadius: 3, overflow: 'hidden' }}>
          <div style={{
            width: `${insight.confidenceScore * 100}%`,
            height: '100%',
            background: insight.confidenceScore >= 0.7 ? '#22c55e' : insight.confidenceScore >= 0.4 ? '#f59e0b' : '#ef4444',
            borderRadius: 3,
            transition: 'width 0.3s ease',
          }} />
        </div>
        <span style={{ fontSize: 11, color: '#9ca3af', whiteSpace: 'nowrap' }}>
          {(insight.confidenceScore * 100).toFixed(0)}% confidence
        </span>
      </div>
    </div>
  );
};
