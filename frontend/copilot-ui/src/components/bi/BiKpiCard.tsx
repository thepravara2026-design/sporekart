import React from 'react';

interface BiKpiCardProps {
  title: string;
  value: string;
  changePercent?: number;
  period?: string;
}

export const BiKpiCard: React.FC<BiKpiCardProps> = ({ title, value, changePercent, period }) => {
  const isPositive = changePercent !== undefined && changePercent >= 0;
  const arrow = isPositive ? '\u25B2' : '\u25BC';
  const color = isPositive ? '#22c55e' : '#ef4444';

  return (
    <div style={{
      background: '#fff',
      borderRadius: 8,
      padding: 16,
      boxShadow: '0 1px 3px rgba(0,0,0,0.12)',
      border: '1px solid #e5e7eb',
    }}>
      <div style={{ fontSize: 12, color: '#6b7280', marginBottom: 4, textTransform: 'uppercase', letterSpacing: 0.5 }}>
        {title}
      </div>
      <div style={{ fontSize: 28, fontWeight: 700, color: '#111827', marginBottom: 8 }}>
        {value}
      </div>
      {changePercent !== undefined && (
        <div style={{ display: 'flex', alignItems: 'center', gap: 4, fontSize: 13, color }}>
          <span>{arrow}</span>
          <span>{Math.abs(changePercent).toFixed(1)}%</span>
          {period && <span style={{ color: '#9ca3af', marginLeft: 4 }}>{period}</span>}
        </div>
      )}
      <div style={{ marginTop: 12, height: 32, background: '#f3f4f6', borderRadius: 4 }}>
        <div style={{ width: '70%', height: '100%', background: 'linear-gradient(90deg, #3b82f6 0%, #60a5fa 100%)', borderRadius: 4, opacity: 0.3 }} />
      </div>
    </div>
  );
};
