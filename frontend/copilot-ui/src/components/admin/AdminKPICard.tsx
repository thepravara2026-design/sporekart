import React from 'react';

interface AdminKPICardProps {
  title: string;
  value: string;
  change: number;
  trend: 'up' | 'down' | 'stable';
  metric: string;
}

const AdminKPICard: React.FC<AdminKPICardProps> = ({ title, value, change, trend, metric }) => {
  const trendColor = trend === 'up' ? '#16a34a' : trend === 'down' ? '#dc2626' : '#6b7280';
  const trendIcon = trend === 'up' ? '\u2191' : trend === 'down' ? '\u2193' : '\u2192';

  return (
    <div style={{
      backgroundColor: '#ffffff',
      borderRadius: '12px',
      padding: '20px',
      boxShadow: '0 1px 3px rgba(0,0,0,0.1), 0 1px 2px rgba(0,0,0,0.06)',
      border: '1px solid #e5e7eb',
      display: 'flex',
      flexDirection: 'column',
      gap: '12px',
      minWidth: '200px'
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <span style={{ fontSize: '14px', fontWeight: 500, color: '#6b7280' }}>{title}</span>
        <span style={{ fontSize: '11px', fontWeight: 600, color: '#9ca3af', textTransform: 'uppercase', letterSpacing: '0.05em' }}>{metric}</span>
      </div>

      <div style={{ fontSize: '28px', fontWeight: 700, color: '#111827', lineHeight: 1.2 }}>
        {value}
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
        <span style={{ color: trendColor, fontWeight: 600, fontSize: '14px' }}>
          {trendIcon} {Math.abs(change).toFixed(1)}%
        </span>
        <span style={{ color: '#9ca3af', fontSize: '13px' }}>vs previous period</span>
      </div>

      <div style={{
        height: '40px',
        display: 'flex',
        alignItems: 'flex-end',
        gap: '2px',
        marginTop: '4px'
      }}>
        {Array.from({ length: 20 }, (_, i) => {
          const h = 8 + Math.sin(i * 0.8) * 10 + Math.random() * 8;
          const baseH = trend === 'up' ? h + i * 0.3 : trend === 'down' ? h - i * 0.3 : h;
          return (
            <div
              key={i}
              style={{
                width: '100%',
                height: `${Math.max(4, baseH)}px`,
                backgroundColor: trendColor,
                borderRadius: '2px 2px 0 0',
                opacity: 0.4 + (i / 20) * 0.4
              }}
            />
          );
        })}
      </div>
    </div>
  );
};

export default AdminKPICard;
