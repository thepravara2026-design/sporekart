import React from 'react';
import type { ForecastResult } from './types/bi';

interface BiForecastCardProps {
  forecast: ForecastResult;
  height?: number;
  actualData?: { period: string; value: number }[];
}

export const BiForecastCard: React.FC<BiForecastCardProps> = ({ forecast, height = 250, actualData }) => {
  if (!forecast.points.length) {
    return (
      <div style={{ height, display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#9ca3af' }}>
        No forecast data
      </div>
    );
  }

  const allValues = [
    ...(actualData?.map(d => d.value) || []),
    ...forecast.points.map(p => p.upperBound),
  ];
  const maxVal = Math.max(...allValues, 1);
  const chartHeight = height - 60;

  const allPeriods = [
    ...(actualData?.map(d => d.period) || []),
    ...forecast.points.map(p => p.period),
  ];

  return (
    <div style={{ background: '#fff', borderRadius: 8, padding: 16, border: '1px solid #e5e7eb' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 12 }}>
        <div>
          <h3 style={{ margin: 0, fontSize: 16, fontWeight: 600, color: '#111827' }}>{forecast.metric}</h3>
          <span style={{ fontSize: 11, color: '#9ca3af' }}>Method: {forecast.method} &middot; {(forecast.confidenceInterval * 100).toFixed(0)}% CI</span>
        </div>
      </div>

      <div style={{ position: 'relative', height: chartHeight, margin: '0 16px' }}>
        {/* Confidence band */}
        {forecast.points.map((point, i) => {
          const left = `${((actualData?.length || 0) + i) / allPeriods.length * 100}%`;
          const lowerPct = (point.lowerBound / maxVal) * chartHeight;
          const upperPct = (point.upperBound / maxVal) * chartHeight;
          return (
            <div
              key={`band-${point.period}`}
              style={{
                position: 'absolute',
                left,
                bottom: `${lowerPct}px`,
                width: `${100 / allPeriods.length}%`,
                height: `${upperPct - lowerPct}px`,
                background: 'rgba(59, 130, 246, 0.15)',
                borderLeft: '1px solid rgba(59, 130, 246, 0.3)',
                borderRight: '1px solid rgba(59, 130, 246, 0.3)',
              }}
              title={`${point.period}: ${point.lowerBound.toFixed(0)} - ${point.upperBound.toFixed(0)}`}
            />
          );
        })}

        {/* Actual data points */}
        {actualData?.map((d, i) => {
          const pct = (d.value / maxVal) * chartHeight;
          const left = `${(i / allPeriods.length) * 100}%`;
          return (
            <div
              key={`actual-${d.period}`}
              style={{
                position: 'absolute',
                left,
                bottom: `${pct}px`,
                width: 6,
                height: 6,
                background: '#22c55e',
                borderRadius: '50%',
                transform: 'translate(-50%, 50%)',
              }}
              title={`${d.period}: ${d.value}`}
            />
          );
        })}

        {/* Predicted points */}
        {forecast.points.map((point, i) => {
          const pct = (point.predictedValue / maxVal) * chartHeight;
          const left = `${((actualData?.length || 0) + i) / allPeriods.length * 100}%`;
          return (
            <div
              key={`pred-${point.period}`}
              style={{
                position: 'absolute',
                left,
                bottom: `${pct}px`,
                width: 6,
                height: 6,
                background: '#3b82f6',
                borderRadius: '50%',
                transform: 'translate(-50%, 50%)',
              }}
              title={`${point.period}: ${point.predictedValue.toFixed(0)}`}
            />
          );
        })}
      </div>

      {forecast.recommendations && (
        <div style={{
          marginTop: 12,
          padding: '8px 10px',
          background: '#f0f9ff',
          borderRadius: 6,
          fontSize: 12,
          color: '#1e40af',
          border: '1px solid #bfdbfe',
        }}>
          {forecast.recommendations}
        </div>
      )}
    </div>
  );
};
