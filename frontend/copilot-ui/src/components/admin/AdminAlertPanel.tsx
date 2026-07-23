import React from 'react';
import { OperationalAlert } from '../types/admin';

interface AdminAlertPanelProps {
  alerts: OperationalAlert[];
  onAcknowledge: (alertId: string) => void;
  onDismiss: (alertId: string) => void;
}

const severityConfig: Record<string, { icon: string; color: string; bg: string; label: string }> = {
  CRITICAL: { icon: '\u26A0\uFE0F', color: '#dc2626', bg: '#fef2f2', label: 'Critical' },
  WARNING: { icon: '\u26A0', color: '#f59e0b', bg: '#fffbeb', label: 'Warning' },
  INFO: { icon: '\u2139\uFE0F', color: '#3b82f6', bg: '#eff6ff', label: 'Info' }
};

const AdminAlertPanel: React.FC<AdminAlertPanelProps> = ({ alerts, onAcknowledge, onDismiss }) => {
  const [severityFilter, setSeverityFilter] = React.useState<string>('ALL');
  const [acknowledged, setAcknowledged] = React.useState<Set<string>>(new Set());

  const filtered = alerts
    .filter((a) => severityFilter === 'ALL' || a.type === severityFilter)
    .filter((a) => !acknowledged.has(a.id));

  const criticalCount = alerts.filter((a) => a.type === 'CRITICAL').length;

  const handleAcknowledge = (id: string) => {
    setAcknowledged((prev) => new Set(prev).add(id));
    onAcknowledge(id);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <h3 style={{ fontSize: '16px', fontWeight: 600, color: '#111827', margin: 0 }}>Alerts</h3>
          {criticalCount > 0 && (
            <span style={{
              backgroundColor: '#dc2626',
              color: '#ffffff',
              padding: '2px 8px',
              borderRadius: '10px',
              fontSize: '12px',
              fontWeight: 600
            }}>
              {criticalCount} critical
            </span>
          )}
        </div>

        <div style={{ display: 'flex', gap: '4px', backgroundColor: '#f3f4f6', borderRadius: '6px', padding: '2px' }}>
          {['ALL', 'CRITICAL', 'WARNING', 'INFO'].map((s) => (
            <button
              key={s}
              onClick={() => setSeverityFilter(s)}
              style={{
                padding: '4px 10px',
                borderRadius: '4px',
                border: 'none',
                backgroundColor: severityFilter === s ? '#ffffff' : 'transparent',
                color: severityFilter === s ? '#111827' : '#6b7280',
                fontWeight: 500,
                fontSize: '11px',
                cursor: 'pointer',
                textTransform: 'capitalize',
                boxShadow: severityFilter === s ? '0 1px 2px rgba(0,0,0,0.1)' : 'none'
              }}
            >
              {s === 'ALL' ? 'All' : s.charAt(0) + s.slice(1).toLowerCase()}
            </button>
          ))}
        </div>
      </div>

      {filtered.length === 0 ? (
        <div style={{
          backgroundColor: '#ffffff',
          borderRadius: '12px',
          padding: '32px',
          border: '1px solid #e5e7eb',
          textAlign: 'center',
          color: '#9ca3af',
          fontSize: '14px'
        }}>
          No {severityFilter !== 'ALL' ? severityFilter.toLowerCase() : ''} alerts to display.
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
          {filtered.map((alert) => {
            const config = severityConfig[alert.type] || severityConfig.INFO;
            return (
              <div
                key={alert.id}
                style={{
                  backgroundColor: alert.type === 'CRITICAL' ? config.bg : '#ffffff',
                  borderRadius: '10px',
                  padding: '14px 16px',
                  border: `1px solid ${alert.type === 'CRITICAL' ? '#fecaca' : '#e5e7eb'}`,
                  display: 'flex',
                  gap: '12px',
                  alignItems: 'flex-start'
                }}
              >
                <span style={{ fontSize: '18px', lineHeight: 1 }}>{config.icon}</span>

                <div style={{ flex: 1, minWidth: 0 }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
                    <span style={{ fontWeight: 600, fontSize: '14px', color: '#111827' }}>{alert.title}</span>
                    <span style={{
                      backgroundColor: config.bg,
                      color: config.color,
                      padding: '1px 6px',
                      borderRadius: '4px',
                      fontSize: '11px',
                      fontWeight: 600,
                      border: `1px solid ${config.color}33`
                    }}>
                      {config.label}
                    </span>
                  </div>
                  <p style={{ margin: '4px 0', fontSize: '13px', color: '#6b7280', lineHeight: 1.4 }}>{alert.description}</p>

                  <div style={{ display: 'flex', gap: '16px', fontSize: '12px', color: '#9ca3af', marginTop: '6px' }}>
                    <span>Metric: {alert.metric}</span>
                    <span>Value: {alert.currentValue}{alert.threshold != null ? ` / ${alert.threshold}` : ''}</span>
                    <span>{new Date(alert.timestamp).toLocaleString()}</span>
                  </div>

                  <div style={{ marginTop: '8px', fontSize: '13px', color: '#2563eb', fontStyle: 'italic' }}>
                    Suggested: {alert.suggestedAction}
                  </div>
                </div>

                <div style={{ display: 'flex', gap: '6px', flexShrink: 0 }}>
                  <button
                    onClick={() => handleAcknowledge(alert.id)}
                    style={{
                      padding: '5px 12px',
                      backgroundColor: '#f9fafb',
                      border: '1px solid #d1d5db',
                      borderRadius: '6px',
                      fontSize: '12px',
                      fontWeight: 500,
                      color: '#374151',
                      cursor: 'pointer'
                    }}
                  >
                    Acknowledge
                  </button>
                  <button
                    onClick={() => onDismiss(alert.id)}
                    style={{
                      padding: '5px 12px',
                      backgroundColor: 'transparent',
                      border: '1px solid #d1d5db',
                      borderRadius: '6px',
                      fontSize: '12px',
                      fontWeight: 500,
                      color: '#6b7280',
                      cursor: 'pointer'
                    }}
                  >
                    Dismiss
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default AdminAlertPanel;
