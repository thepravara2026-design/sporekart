import React from 'react';
import type { YieldPrediction } from './types/grower';

const cardStyle: React.CSSProperties = {
  padding: '16px',
  background: 'var(--cp-surface, #1a1a2e)',
  borderRadius: '12px',
  border: '1px solid var(--cp-border, #2a2a4a)',
};

const headerStyle: React.CSSProperties = {
  fontSize: '16px',
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
  margin: '0 0 12px',
};

const topRowStyle: React.CSSProperties = {
  display: 'flex',
  gap: '12px',
  marginBottom: 16,
};

const largeMetricStyle: React.CSSProperties = {
  flex: 1,
  padding: '12px',
  background: 'var(--cp-surface-alt, #16213e)',
  borderRadius: '8px',
  textAlign: 'center',
};

const largeValueStyle: React.CSSProperties = {
  fontSize: '22px',
  fontWeight: 700,
  color: 'var(--cp-accent, #4ade80)',
};

const largeLabelStyle: React.CSSProperties = {
  fontSize: '10px',
  textTransform: 'uppercase' as const,
  letterSpacing: '0.5px',
  color: 'var(--cp-text-dim, #888)',
  marginTop: 2,
};

const gaugeStyle: React.CSSProperties = {
  marginBottom: 16,
};

const gaugeLabelRowStyle: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  fontSize: '12px',
  color: 'var(--cp-text-dim, #aaa)',
  marginBottom: 4,
};

const gaugeTrackStyle: React.CSSProperties = {
  height: 10,
  borderRadius: 5,
  background: 'var(--cp-border, #2a2a4a)',
  overflow: 'hidden',
};

const gaugeFillStyle = (pct: number): React.CSSProperties => ({
  height: '100%',
  width: `${Math.min(pct * 100, 100)}%`,
  borderRadius: 5,
  background: pct > 0.85 ? '#4ade80' : pct > 0.7 ? '#facc15' : '#f87171',
  transition: 'width 0.4s',
});

const barChartStyle: React.CSSProperties = {
  marginBottom: 16,
};

const barRowStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '8px',
  marginBottom: 6,
};

const barLabelStyle: React.CSSProperties = {
  width: 60,
  fontSize: '12px',
  color: 'var(--cp-text-dim, #aaa)',
  textAlign: 'right',
};

const barTrackStyle: React.CSSProperties = {
  flex: 1,
  height: 14,
  borderRadius: 4,
  background: 'var(--cp-border, #2a2a4a)',
  overflow: 'hidden',
};

const barFillStyle = (pct: number, color: string): React.CSSProperties => ({
  height: '100%',
  width: `${Math.min(pct, 100)}%`,
  borderRadius: 4,
  background: color,
  transition: 'width 0.4s',
});

const barAmountStyle: React.CSSProperties = {
  width: 50,
  fontSize: '11px',
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
  textAlign: 'left',
};

const rowStyle: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  fontSize: '13px',
  color: 'var(--cp-text-dim, #aaa)',
  marginBottom: 6,
};

const rowValueStyle: React.CSSProperties = {
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
};

const riskDot = (score: number): string => {
  if (score <= 25) return '#4ade80';
  if (score <= 50) return '#facc15';
  if (score <= 75) return '#fb923c';
  return '#f87171';
};

const hpwStyle: React.CSSProperties = {
  marginTop: 12,
  padding: '10px 12px',
  background: 'var(--cp-surface-alt, #16213e)',
  borderRadius: '8px',
  fontSize: '12px',
  color: 'var(--cp-text, #e0e0e0)',
};

interface GrowerYieldCardProps {
  prediction: YieldPrediction;
}

export default function GrowerYieldCard({ prediction }: GrowerYieldCardProps) {
  const maxBar = Math.max(prediction.estimatedRevenue, prediction.productionCost);

  return (
    <div style={cardStyle}>
      <h4 style={headerStyle}>Yield Prediction</h4>

      <div style={topRowStyle}>
        <div style={largeMetricStyle}>
          <div style={largeValueStyle}>{prediction.expectedYieldKg} kg</div>
          <div style={largeLabelStyle}>Expected Yield</div>
        </div>
        <div style={largeMetricStyle}>
          <div style={largeValueStyle}>{prediction.profitMargin}%</div>
          <div style={largeLabelStyle}>Profit Margin</div>
        </div>
      </div>

      <div style={gaugeStyle}>
        <div style={gaugeLabelRowStyle}>
          <span>Efficiency</span>
          <span>{(prediction.yieldEfficiency * 100).toFixed(0)}%</span>
        </div>
        <div style={gaugeTrackStyle}>
          <div style={gaugeFillStyle(prediction.yieldEfficiency)} />
        </div>
      </div>

      <div style={barChartStyle}>
        <div style={barRowStyle}>
          <span style={barLabelStyle}>Revenue</span>
          <div style={barTrackStyle}>
            <div style={barFillStyle((prediction.estimatedRevenue / maxBar) * 100, '#4ade80')} />
          </div>
          <span style={barAmountStyle}>₹{prediction.estimatedRevenue.toLocaleString()}</span>
        </div>
        <div style={barRowStyle}>
          <span style={barLabelStyle}>Cost</span>
          <div style={barTrackStyle}>
            <div style={barFillStyle((prediction.productionCost / maxBar) * 100, '#f87171')} />
          </div>
          <span style={barAmountStyle}>₹{prediction.productionCost.toLocaleString()}</span>
        </div>
      </div>

      <div style={rowStyle}>
        <span>Risk Score</span>
        <span style={{ ...rowValueStyle, color: riskDot(prediction.riskScore) }}>{prediction.riskScore}/100</span>
      </div>

      <div style={rowStyle}>
        <span>Harvest Window</span>
        <span style={rowValueStyle}>{prediction.harvestWindow}</span>
      </div>

      {prediction.recommendations && (
        <div style={hpwStyle}>
          💡 {prediction.recommendations}
        </div>
      )}
    </div>
  );
}
