import React from 'react';
import type { TrendDataPoint } from './types/bi';

interface BiRevenueChartProps {
  data: TrendDataPoint[];
  height?: number;
}

export const BiRevenueChart: React.FC<BiRevenueChartProps> = ({ data, height = 250 }) => {
  if (!data.length) return <div style={{ height, display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#9ca3af' }}>No data</div>;

  const maxValue = Math.max(...data.map(d => d.value));
  const barWidth = Math.max(8, Math.min(40, (600 - 40) / data.length - 4));

  return (
    <div style={{ background: '#fff', borderRadius: 8, padding: 16, border: '1px solid #e5e7eb' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 16 }}>
        <h3 style={{ margin: 0, fontSize: 16, fontWeight: 600, color: '#111827' }}>Revenue Trends</h3>
      </div>
      <div style={{ position: 'relative', height, display: 'flex', alignItems: 'flex-end', gap: 4, padding: '0 16px' }}>
        {data.map((point, i) => {
          const pct = (point.value / maxValue) * 100;
          return (
            <div key={point.period} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', flex: 1 }}>
              <div
                style={{
                  width: barWidth,
                  height: `${pct}%`,
                  background: 'linear-gradient(180deg, #3b82f6 0%, #60a5fa 100%)',
                  borderRadius: '4px 4px 0 0',
                  transition: 'height 0.3s ease',
                  minHeight: 4,
                }}
                title={`${point.period}: ${point.value}`}
              />
              {data.length <= 12 && (
                <div style={{ fontSize: 10, color: '#9ca3af', marginTop: 6, transform: 'rotate(-45deg)', whiteSpace: 'nowrap' }}>
                  {point.period}
                </div>
              )}
            </div>
          );
        })}
      </div>
      <div style={{ position: 'relative', height: 2, background: '#ef4444', opacity: 0.5, margin: '0 16px' }}>
        {data.filter((_, i) => i > 0).map((point, i) => {
          const prevMa = data[i].movingAverage;
          const pct = maxValue ? (point.movingAverage / maxValue) * height : 0;
          const prevPct = maxValue ? (prevMa / maxValue) * height : 0;
          return (
            <div
              key={`ma-${point.period}`}
              style={{
                position: 'absolute',
                left: `${((i + 1) / data.length) * 100}%`,
                bottom: `${pct}px`,
                width: 6,
                height: 6,
                background: '#ef4444',
                borderRadius: '50%',
                transform: 'translate(-50%, 50%)',
              }}
            />
          );
        })}
      </div>
    </div>
  );
};
