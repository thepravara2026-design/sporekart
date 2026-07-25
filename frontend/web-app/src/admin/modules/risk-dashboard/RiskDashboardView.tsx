import { useState, useEffect, memo } from 'react';
import { riskDashboardMockService } from './services/riskDashboardMockService';
import type { BusinessRisk, RiskSummary } from './types';

export const RiskDashboardView = memo(function RiskDashboardView() {
  const [risks, setRisks] = useState<BusinessRisk[]>([]);
  const [summary, setSummary] = useState<RiskSummary | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      riskDashboardMockService.getRisks(),
      riskDashboardMockService.getRiskSummary(),
    ]).then(([riskData, summaryData]) => {
      setRisks(riskData);
      setSummary(summaryData);
      setLoading(false);
    });
  }, []);

  if (loading) return <div style={{ padding: '24px' }}>Loading risk data...</div>;

  const severityColors: Record<string, string> = { CRITICAL: '#ef4444', HIGH: '#f97316', MEDIUM: '#eab308', LOW: '#3b82f6' };

  return (
    <div style={{ padding: '24px' }}>
      <h2 style={{ margin: '0 0 24px', fontSize: '24px', fontWeight: 700 }}>Risk Dashboard</h2>

      {summary && (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: '16px', marginBottom: '24px' }}>
          <SummaryCard label="Total Risks" value={summary.totalRisks} color="#3b82f6" />
          <SummaryCard label="Critical" value={summary.critical} color="#ef4444" />
          <SummaryCard label="High" value={summary.high} color="#f97316" />
          <SummaryCard label="Medium" value={summary.medium} color="#eab308" />
          <SummaryCard label="Low" value={summary.low} color="#3b82f6" />
          <SummaryCard label="Avg Risk Score" value={`${summary.averageRiskScore}`} color="#7c3aed" />
        </div>
      )}

      <div style={{ background: '#fff', borderRadius: '8px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)', overflow: 'hidden' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ background: '#f9fafb', borderBottom: '1px solid #e5e7eb' }}>
              <th style={thStyle}>Risk</th>
              <th style={thStyle}>Category</th>
              <th style={thStyle}>Severity</th>
              <th style={thStyle}>Score</th>
              <th style={thStyle}>Impact</th>
            </tr>
          </thead>
          <tbody>
            {risks.map((risk) => (
              <tr key={risk.id} style={{ borderBottom: '1px solid #e5e7eb' }}>
                <td style={tdStyle}>
                  <div style={{ fontWeight: 600 }}>{risk.title}</div>
                  <div style={{ fontSize: '12px', color: '#6b7280' }}>{risk.description}</div>
                </td>
                <td style={tdStyle}>{risk.category}</td>
                <td style={tdStyle}>
                  <span style={{
                    display: 'inline-block', padding: '2px 8px', borderRadius: '4px',
                    fontSize: '12px', fontWeight: 600, color: '#fff',
                    backgroundColor: severityColors[risk.severity] || '#6b7280'
                  }}>
                    {risk.severity}
                  </span>
                </td>
                <td style={tdStyle}>
                  <div style={{
                    width: '60px', height: '6px', background: '#e5e7eb', borderRadius: '3px', overflow: 'hidden'
                  }}>
                    <div style={{
                      width: `${risk.riskScore}%`, height: '100%',
                      background: risk.riskScore > 70 ? '#ef4444' : risk.riskScore > 50 ? '#f97316' : '#eab308',
                      borderRadius: '3px',
                    }} />
                  </div>
                  <div style={{ fontSize: '12px', color: '#6b7280', marginTop: '2px' }}>{risk.riskScore}/100</div>
                </td>
                <td style={{ ...tdStyle, fontSize: '13px', color: '#6b7280' }}>{risk.impact}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});

function SummaryCard({ label, value, color }: { label: string; value: string | number; color: string }) {
  return (
    <div style={{
      background: '#fff', borderRadius: '8px', padding: '16px',
      boxShadow: '0 1px 3px rgba(0,0,0,0.1)', borderLeft: `4px solid ${color}`
    }}>
      <div style={{ fontSize: '12px', color: '#6b7280', marginBottom: '4px' }}>{label}</div>
      <div style={{ fontSize: '28px', fontWeight: 700, color }}>{value}</div>
    </div>
  );
}

const thStyle: React.CSSProperties = {
  padding: '12px 16px', textAlign: 'left', fontSize: '12px',
  fontWeight: 600, color: '#6b7280', textTransform: 'uppercase',
};

const tdStyle: React.CSSProperties = {
  padding: '12px 16px', fontSize: '14px',
};
