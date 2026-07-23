import React from 'react';
import type { RiskAlert } from './types/bi';

interface RiskPanelProps { risks: RiskAlert[]; }

function SeverityBadge({ severity }: { severity: string }) {
  const colors: Record<string, { bg: string; text: string }> = {
    critical: { bg: '#fef2f2', text: '#ef4444' },
    high: { bg: '#fff7ed', text: '#f97316' },
    medium: { bg: '#fefce8', text: '#eab308' },
    low: { bg: '#f0fdf4', text: '#22c55e' },
  };
  const c = colors[severity.toLowerCase()] || { bg: '#f3f4f6', text: '#6b7280' };
  return <span style={{ background: c.bg, color: c.text, padding: '2px 10px', borderRadius: 4, fontSize: 11, fontWeight: 600 }}>{severity}</span>;
}

function StatusBadge({ status }: { status: string }) {
  const colors: Record<string, { bg: string; text: string }> = {
    active: { bg: '#fef2f2', text: '#ef4444' },
    monitoring: { bg: '#fff7ed', text: '#f97316' },
    mitigated: { bg: '#f0fdf4', text: '#22c55e' },
    resolved: { bg: '#f0fdf4', text: '#16a34a' },
  };
  const c = colors[status.toLowerCase()] || { bg: '#f3f4f6', text: '#6b7280' };
  return <span style={{ background: c.bg, color: c.text, padding: '2px 10px', borderRadius: 4, fontSize: 11, fontWeight: 600 }}>{status}</span>;
}

export default function RiskPanel({ risks }: RiskPanelProps) {
  const severityColor = (severity: string) => {
    const colors: Record<string, string> = { critical: '#ef4444', high: '#f97316', medium: '#eab308', low: '#22c55e' };
    return colors[severity.toLowerCase()] || '#6b7280';
  };

  const counts = risks.reduce<Record<string, number>>((acc, r) => {
    const s = r.severity.toLowerCase();
    acc[s] = (acc[s] || 0) + 1;
    return acc;
  }, {});
  const active = risks.filter(r => r.status.toLowerCase() === 'active').length;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 12 }}>
        <div style={{ background: '#fef2f2', border: '1px solid #fecaca', borderRadius: 8, padding: 12, textAlign: 'center' }}>
          <span style={{ fontSize: 24, fontWeight: 700, color: '#ef4444' }}>{counts.critical || 0}</span>
          <div style={{ fontSize: 12, color: '#ef4444', fontWeight: 500 }}>Critical</div>
        </div>
        <div style={{ background: '#fff7ed', border: '1px solid #fed7aa', borderRadius: 8, padding: 12, textAlign: 'center' }}>
          <span style={{ fontSize: 24, fontWeight: 700, color: '#f97316' }}>{counts.high || 0}</span>
          <div style={{ fontSize: 12, color: '#f97316', fontWeight: 500 }}>High</div>
        </div>
        <div style={{ background: '#fefce8', border: '1px solid #fde68a', borderRadius: 8, padding: 12, textAlign: 'center' }}>
          <span style={{ fontSize: 24, fontWeight: 700, color: '#eab308' }}>{counts.medium || 0}</span>
          <div style={{ fontSize: 12, color: '#eab308', fontWeight: 500 }}>Medium</div>
        </div>
        <div style={{ background: '#f0fdf4', border: '1px solid #bbf7d0', borderRadius: 8, padding: 12, textAlign: 'center' }}>
          <span style={{ fontSize: 24, fontWeight: 700, color: '#22c55e' }}>{counts.low || 0}</span>
          <div style={{ fontSize: 12, color: '#22c55e', fontWeight: 500 }}>Low</div>
        </div>
        <div style={{ background: '#f9fafb', border: '1px solid #e5e7eb', borderRadius: 8, padding: 12, textAlign: 'center' }}>
          <span style={{ fontSize: 24, fontWeight: 700, color: '#ef4444' }}>{active}</span>
          <div style={{ fontSize: 12, color: '#6b7280', fontWeight: 500 }}>Active</div>
        </div>
      </div>
      {risks.length === 0 && <div style={{ textAlign: 'center', padding: 32, color: '#9ca3af', fontSize: 14 }}>No risks detected</div>}
      {risks.map(risk => {
        const sevColor = severityColor(risk.severity);
        return (
          <div key={risk.riskId} style={{ border: `1px solid ${sevColor}40`, borderLeft: `4px solid ${sevColor}`, borderRadius: 8, padding: 16, background: '#fff' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 8 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <SeverityBadge severity={risk.severity} />
                <StatusBadge status={risk.status} />
                <span style={{ fontSize: 11, color: '#9ca3af' }}>{risk.riskType}</span>
              </div>
            </div>
            <h4 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 4px' }}>{risk.title}</h4>
            <p style={{ fontSize: 13, color: '#6b7280', margin: '0 0 12px', lineHeight: 1.4 }}>{risk.description}</p>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16, marginBottom: 12 }}>
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 11, color: '#6b7280', marginBottom: 2 }}>
                  <span>Probability</span>
                  <span>{(risk.probability * 100).toFixed(0)}%</span>
                </div>
                <div style={{ height: 8, background: '#e5e7eb', borderRadius: 4, overflow: 'hidden' }}>
                  <div style={{ width: `${risk.probability * 100}%`, height: '100%', background: risk.probability >= 0.7 ? '#ef4444' : risk.probability >= 0.4 ? '#f97316' : '#22c55e', borderRadius: 4 }} />
                </div>
              </div>
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 11, color: '#6b7280', marginBottom: 2 }}>
                  <span>Impact</span>
                  <span>{(risk.impact * 100).toFixed(0)}%</span>
                </div>
                <div style={{ height: 8, background: '#e5e7eb', borderRadius: 4, overflow: 'hidden' }}>
                  <div style={{ width: `${risk.impact * 100}%`, height: '100%', background: risk.impact >= 0.7 ? '#ef4444' : risk.impact >= 0.4 ? '#f97316' : '#22c55e', borderRadius: 4 }} />
                </div>
              </div>
            </div>
            <p style={{ fontSize: 12, color: '#374151', fontStyle: 'italic', margin: '0 0 12px' }}>Recommended: {risk.recommendedAction}</p>
            <button style={{ padding: '8px 20px', background: '#ef4444', color: '#fff', border: 'none', borderRadius: 6, cursor: 'pointer', fontSize: 13, fontWeight: 600 }}>
              Mitigate
            </button>
          </div>
        );
      })}
    </div>
  );
}
