import React, { useState } from 'react';
import type { BusinessForecast, ForecastPoint } from './types/bi';

interface ForecastPanelProps { data: BusinessForecast; onMetricChange?: (metric: string) => void; }

const metrics = ['Revenue', 'Customers', 'Orders', 'Inventory', 'Growth Rate'];

export default function ForecastPanel({ data, onMetricChange }: ForecastPanelProps) {
  const [selectedMetric, setSelectedMetric] = useState(data.metric);
  const { points, confidenceInterval, accuracy, method, seasonality, trend, recommendations } = data;
  const maxVal = Math.max(...points.map(p => p.upperBound), 1);
  const minVal = Math.min(...points.map(p => p.lowerBound), 0);
  const range = maxVal - minVal || 1;

  const handleMetricChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedMetric(e.target.value);
    onMetricChange?.(e.target.value);
  };

  const chartW = 700; const chartH = 300; const padL = 60; const padR = 20; const padT = 20; const padB = 30;
  const w = chartW - padL - padR; const h = chartH - padT - padB;
  const stepX = points.length > 1 ? w / (points.length - 1) : w;

  const linePath = points.map((p, i) => `${i === 0 ? 'M' : 'L'} ${padL + i * stepX} ${padT + h - ((p.value - minVal) / range) * h}`).join(' ');
  const bandPath = points.length > 0 ? (
    points.map((p, i) => `${i === 0 ? 'M' : 'L'} ${padL + i * stepX} ${padT + h - ((p.upperBound - minVal) / range) * h}`).join(' ') +
    points.reverse().map((p, i) => `${i === 0 ? 'L' : 'L'} ${padL + (points.length - 1 - i) * stepX} ${padT + h - ((p.lowerBound - minVal) / range) * h}`).join(' ') + ' Z'
  ) : '';

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
        <label style={{ fontSize: 14, fontWeight: 600, color: '#374151' }}>Metric:</label>
        <select value={selectedMetric} onChange={handleMetricChange} style={{ padding: '6px 12px', border: '1px solid #e5e7eb', borderRadius: 6, fontSize: 13, background: '#fff' }}>
          {metrics.map(m => <option key={m} value={m}>{m}</option>)}
        </select>
      </div>
      <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
        <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 12px' }}>Forecast Trend</h3>
        <svg viewBox={`0 0 ${chartW} ${chartH}`} style={{ width: '100%', maxWidth: chartW }}>
          {points.length > 0 && <path d={bandPath} fill="#3b82f6" fillOpacity={0.15} />}
          <path d={linePath} fill="none" stroke="#3b82f6" strokeWidth={2.5} />
          {points.map((p, i) => (
            <circle key={i} cx={padL + i * stepX} cy={padT + h - ((p.value - minVal) / range) * h} r={3} fill="#3b82f6" />
          ))}
          {Array.from({ length: 5 }, (_, i) => {
            const y = padT + (h / 4) * i;
            const val = maxVal - (range / 4) * i;
            return <text key={i} x={padL - 8} y={y} textAnchor="end" dominantBaseline="middle" fontSize={10} fill="#9ca3af">{Math.round(val).toLocaleString()}</text>;
          })}
          {points.map((p, i) => (
            <text key={i} x={padL + i * stepX} y={chartH - 4} textAnchor="middle" fontSize={9} fill="#9ca3af">{p.period.length > 8 ? p.period.slice(0, 6) + '...' : p.period}</text>
          ))}
        </svg>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 12 }}>
        <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 12 }}>
          <span style={{ fontSize: 11, color: '#6b7280' }}>Method</span>
          <div style={{ fontSize: 14, fontWeight: 600, color: '#111827', marginTop: 4 }}>{method}</div>
        </div>
        <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 12 }}>
          <span style={{ fontSize: 11, color: '#6b7280' }}>Accuracy</span>
          <div style={{ fontSize: 14, fontWeight: 600, color: accuracy >= 80 ? '#22c55e' : accuracy >= 60 ? '#f97316' : '#ef4444', marginTop: 4 }}>{accuracy.toFixed(1)}%</div>
        </div>
        <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 12 }}>
          <span style={{ fontSize: 11, color: '#6b7280' }}>Confidence Interval</span>
          <div style={{ fontSize: 14, fontWeight: 600, color: '#111827', marginTop: 4 }}>&plusmn;{confidenceInterval}%</div>
        </div>
        <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 12 }}>
          <span style={{ fontSize: 11, color: '#6b7280' }}>Seasonality</span>
          <div style={{ fontSize: 14, fontWeight: 600, color: '#111827', marginTop: 4 }}>{seasonality}</div>
        </div>
        <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 12 }}>
          <span style={{ fontSize: 11, color: '#6b7280' }}>Trend</span>
          <div style={{ fontSize: 14, fontWeight: 600, color: '#111827', marginTop: 4 }}>{trend}</div>
        </div>
      </div>
      <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
        <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 8px' }}>Recommendations</h3>
        <p style={{ fontSize: 13, color: '#6b7280', lineHeight: 1.5, margin: 0 }}>{recommendations}</p>
      </div>
    </div>
  );
}
