import React from 'react';
import { ForecastResult } from '../types/admin';

interface AdminForecastCardProps {
  forecast: ForecastResult | null;
  metric: string;
  onPeriodChange: (period: string) => void;
}

const AdminForecastCard: React.FC<AdminForecastCardProps> = ({ forecast, metric, onPeriodChange }) => {
  const [selectedPeriod, setSelectedPeriod] = React.useState('monthly');

  const handlePeriodChange = (period: string) => {
    setSelectedPeriod(period);
    onPeriodChange(period);
  };

  if (!forecast) {
    return (
      <div style={{
        backgroundColor: '#ffffff',
        borderRadius: '12px',
        padding: '24px',
        border: '1px solid #e5e7eb',
        textAlign: 'center',
        color: '#9ca3af'
      }}>
        Loading forecast data...
      </div>
    );
  }

  const currentValue = forecast.forecastValues.length > 0 ? forecast.forecastValues[0].value : 0;
  const lastValue = forecast.forecastValues.length > 0 ? forecast.forecastValues[forecast.forecastValues.length - 1].value : 0;
  const trendDirection = lastValue >= currentValue ? 'up' : 'down';

  const values = forecast.forecastValues.map((v) => v.value);
  const maxVal = Math.max(...values, 1) * 1.2;
  const chartHeight = 140;
  const chartWidth = 300;

  const confInterval = forecast.confidenceInterval || '85%';

  return (
    <div style={{
      backgroundColor: '#ffffff',
      borderRadius: '12px',
      padding: '20px',
      border: '1px solid #e5e7eb',
      display: 'flex',
      flexDirection: 'column',
      gap: '16px'
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h3 style={{ fontSize: '16px', fontWeight: 600, color: '#111827', margin: 0 }}>
          {metric.charAt(0).toUpperCase() + metric.slice(1)} Forecast
        </h3>
        <span style={{
          backgroundColor: trendDirection === 'up' ? '#d1fae5' : '#fef2f2',
          color: trendDirection === 'up' ? '#047857' : '#dc2626',
          padding: '3px 10px',
          borderRadius: '6px',
          fontSize: '12px',
          fontWeight: 600,
          display: 'flex',
          alignItems: 'center',
          gap: '4px'
        }}>
          {trendDirection === 'up' ? '\u2191' : '\u2193'} {trendDirection === 'up' ? 'Upward' : 'Downward'}
        </span>
      </div>

      <div style={{ display: 'flex', gap: '24px', alignItems: 'flex-start' }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', minWidth: '140px' }}>
          <div>
            <div style={{ fontSize: '12px', color: '#9ca3af', marginBottom: '2px' }}>Current Value</div>
            <div style={{ fontSize: '18px', fontWeight: 700, color: '#111827' }}>{currentValue.toLocaleString()}</div>
          </div>
          <div>
            <div style={{ fontSize: '12px', color: '#9ca3af', marginBottom: '2px' }}>Forecasted ({forecast.period})</div>
            <div style={{ fontSize: '18px', fontWeight: 700, color: '#2563eb' }}>{lastValue.toLocaleString()}</div>
          </div>
          <div style={{
            backgroundColor: '#f3f4f6',
            borderRadius: '6px',
            padding: '8px 10px',
            fontSize: '12px',
            color: '#6b7280'
          }}>
            Confidence: <strong>{confInterval}</strong>
          </div>
        </div>

        <div style={{ flex: 1 }}>
          <svg width={chartWidth} height={chartHeight}>
            <line x1={0} y1={chartHeight - 20} x2={chartWidth} y2={chartHeight - 20} stroke="#e5e7eb" />

            {values.map((v, i) => {
              const x = (i / (values.length - 1)) * chartWidth;
              const barH = (v / maxVal) * (chartHeight - 30);
              const y = chartHeight - 20 - barH;
              const barW = Math.max(4, chartWidth / values.length - 2);

              return (
                <g key={i}>
                  <rect x={x - barW / 2} y={y} width={barW} height={barH} rx={2} fill="#2563eb" opacity={0.7} />
                </g>
              );
            })}
          </svg>

          <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '4px', fontSize: '10px', color: '#9ca3af' }}>
            {forecast.forecastValues.length > 0 && (
              <>
                <span>{forecast.forecastValues[0].label}</span>
                <span>{forecast.forecastValues[forecast.forecastValues.length - 1].label}</span>
              </>
            )}
          </div>
        </div>
      </div>

      <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '4px', backgroundColor: '#f3f4f6', borderRadius: '6px', padding: '3px', alignSelf: 'flex-end' }}>
        {['daily', 'weekly', 'monthly', 'yearly'].map((p) => (
          <button
            key={p}
            onClick={() => handlePeriodChange(p)}
            style={{
              padding: '4px 10px',
              borderRadius: '4px',
              border: 'none',
              backgroundColor: selectedPeriod === p ? '#ffffff' : 'transparent',
              color: selectedPeriod === p ? '#111827' : '#6b7280',
              fontWeight: 500,
              fontSize: '11px',
              cursor: 'pointer',
              textTransform: 'capitalize',
              boxShadow: selectedPeriod === p ? '0 1px 2px rgba(0,0,0,0.1)' : 'none'
            }}
          >
            {p}
          </button>
        ))}
      </div>
    </div>
  );
};

export default AdminForecastCard;
