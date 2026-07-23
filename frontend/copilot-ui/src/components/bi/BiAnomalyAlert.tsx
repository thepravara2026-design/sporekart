import React from 'react';
import type { AnomalyAlert } from './types/bi';

const SEVERITY_COLORS: Record<string, string> = {
  CRITICAL: '#ef4444',
  HIGH: '#f97316',
  MEDIUM: '#f59e0b',
  LOW: '#3b82f6',
};

interface BiAnomalyAlertProps {
  alert: AnomalyAlert;
}

export const BiAnomalyAlert: React.FC<BiAnomalyAlertProps> = ({ alert }) => {
  const color = SEVERITY_COLORS[alert.severity] || '#6b7280';

  return (
    <div style={{
      background: '#fff',
      borderRadius: 8,
      padding: 16,
      border: '1px solid #e5e7eb',
      borderLeft: `4px solid ${color}`,
      boxShadow: '0 1px 3px rgba(0,0,0,0.08)',
    }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 10 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <span style={{
            background: color,
            color: '#fff',
            fontSize: 10,
            fontWeight: 700,
            padding: '2px 8px',
            borderRadius: 10,
            textTransform: 'uppercase',
          }}>
            {alert.severity}
          </span>
          <span style={{ fontSize: 13, fontWeight: 600, color: '#374151' }}>{alert.metric}</span>
        </div>
        <span style={{ fontSize: 12, color: '#6b7280' }}>
          Deviation: {alert.deviationScore.toFixed(1)}
        </span>
      </div>

      <p style={{ margin: '0 0 12px 0', fontSize: 13, color: '#6b7280' }}>{alert.description}</p>

      <div style={{ display: 'flex', gap: 16, marginBottom: 12 }}>
        <div style={{ flex: 1 }}>
          <div style={{ fontSize: 11, color: '#9ca3af', marginBottom: 2 }}>Observed</div>
          <div style={{ fontSize: 16, fontWeight: 700, color: '#ef4444' }}>{alert.observedValue.toLocaleString()}</div>
        </div>
        <div style={{ flex: 1 }}>
          <div style={{ fontSize: 11, color: '#9ca3af', marginBottom: 2 }}>Expected</div>
          <div style={{ fontSize: 16, fontWeight: 700, color: '#22c55e' }}>{alert.expectedValue.toLocaleString()}</div>
        </div>
      </div>

      {alert.recommendedAction && (
        <div style={{
          background: '#f9fafb',
          borderRadius: 6,
          padding: '8px 10px',
          fontSize: 12,
          color: '#374151',
          border: '1px solid #e5e7eb',
        }}>
          <strong>Action:</strong> {alert.recommendedAction}
        </div>
      )}
    </div>
  );
};
