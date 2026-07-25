import { useState, useEffect, memo } from 'react';
import { alertCenterMockService } from './services/alertCenterMockService';
import { SEVERITY_COLORS } from './constants';
import type { Alert, AlertMetrics } from './types';

function SeverityBadge({ severity }: { severity: string }) {
  return (
    <span style={{
      display: 'inline-block', padding: '2px 8px', borderRadius: '4px',
      fontSize: '12px', fontWeight: 600, color: '#fff',
      backgroundColor: SEVERITY_COLORS[severity] || '#6b7280'
    }}>
      {severity}
    </span>
  );
}

function StatusBadge({ status }: { status: string }) {
  const colors: Record<string, string> = {
    OPEN: '#ef4444', ACKNOWLEDGED: '#eab308', RESOLVED: '#22c55e', ESCALATED: '#f97316', CLOSED: '#6b7280',
  };
  return (
    <span style={{
      display: 'inline-block', padding: '2px 8px', borderRadius: '4px',
      fontSize: '12px', fontWeight: 600, color: '#fff',
      backgroundColor: colors[status] || '#6b7280'
    }}>
      {status}
    </span>
  );
}

export const AlertCenterDashboard = memo(function AlertCenterDashboard() {
  const [alerts, setAlerts] = useState<Alert[]>([]);
  const [metrics, setMetrics] = useState<AlertMetrics | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      alertCenterMockService.getAlerts(),
      alertCenterMockService.getAlertMetrics(),
    ]).then(([alertData, metricData]) => {
      setAlerts(alertData);
      setMetrics(metricData);
      setLoading(false);
    });
  }, []);

  const handleAcknowledge = async (id: string) => {
    const updated = await alertCenterMockService.acknowledgeAlert(id);
    setAlerts((prev) => prev.map((a) => (a.id === id ? updated : a)));
  };

  const handleResolve = async (id: string) => {
    const updated = await alertCenterMockService.resolveAlert(id);
    setAlerts((prev) => prev.map((a) => (a.id === id ? updated : a)));
  };

  if (loading) return <div style={{ padding: '24px' }}>Loading alerts...</div>;

  const metricCards = metrics ? [
    { label: 'Total', value: metrics.totalAlerts, color: '#3b82f6' },
    { label: 'Open', value: metrics.open, color: '#ef4444' },
    { label: 'Acknowledged', value: metrics.acknowledged, color: '#eab308' },
    { label: 'Resolved', value: metrics.resolved, color: '#22c55e' },
    { label: 'Critical', value: metrics.critical, color: '#ef4444' },
    { label: 'High', value: metrics.high, color: '#f97316' },
  ] : [];

  return (
    <div style={{ padding: '24px' }}>
      <h2 style={{ margin: '0 0 24px', fontSize: '24px', fontWeight: 700 }}>Alert Center</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(150px, 1fr))', gap: '16px', marginBottom: '24px' }}>
        {metricCards.map((m) => (
          <div key={m.label} style={{
            background: '#fff', borderRadius: '8px', padding: '16px',
            boxShadow: '0 1px 3px rgba(0,0,0,0.1)', borderLeft: `4px solid ${m.color}`
          }}>
            <div style={{ fontSize: '12px', color: '#6b7280', marginBottom: '4px' }}>{m.label}</div>
            <div style={{ fontSize: '28px', fontWeight: 700, color: m.color }}>{m.value}</div>
          </div>
        ))}
      </div>

      <div style={{ background: '#fff', borderRadius: '8px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)', overflow: 'hidden' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ background: '#f9fafb', borderBottom: '1px solid #e5e7eb' }}>
              <th style={thStyle}>Title</th>
              <th style={thStyle}>Domain</th>
              <th style={thStyle}>Severity</th>
              <th style={thStyle}>Status</th>
              <th style={thStyle}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {alerts.map((alert) => (
              <tr key={alert.id} style={{ borderBottom: '1px solid #e5e7eb' }}>
                <td style={tdStyle}>
                  <div style={{ fontWeight: 600 }}>{alert.title}</div>
                  <div style={{ fontSize: '12px', color: '#6b7280' }}>{alert.description}</div>
                </td>
                <td style={tdStyle}>{alert.domain}</td>
                <td style={tdStyle}><SeverityBadge severity={alert.severity} /></td>
                <td style={tdStyle}><StatusBadge status={alert.status} /></td>
                <td style={tdStyle}>
                  {alert.status === 'OPEN' && (
                    <button onClick={() => handleAcknowledge(alert.id)} style={btnStyle}>
                      Acknowledge
                    </button>
                  )}
                  {alert.status === 'ACKNOWLEDGED' && (
                    <button onClick={() => handleResolve(alert.id)} style={{ ...btnStyle, background: '#22c55e' }}>
                      Resolve
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});

const thStyle: React.CSSProperties = {
  padding: '12px 16px', textAlign: 'left', fontSize: '12px',
  fontWeight: 600, color: '#6b7280', textTransform: 'uppercase',
};

const tdStyle: React.CSSProperties = {
  padding: '12px 16px', fontSize: '14px',
};

const btnStyle: React.CSSProperties = {
  padding: '6px 12px', borderRadius: '6px', border: 'none',
  background: '#eab308', color: '#fff', fontSize: '12px',
  fontWeight: 600, cursor: 'pointer',
};
