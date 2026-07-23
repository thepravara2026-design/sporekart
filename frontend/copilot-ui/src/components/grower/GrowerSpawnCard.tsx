import React from 'react';
import type { SpawnRecommendation } from './types/grower';

const cardStyle: React.CSSProperties = {
  padding: '16px',
  background: 'var(--cp-surface, #1a1a2e)',
  borderRadius: '12px',
  border: '1px solid var(--cp-border, #2a2a4a)',
};

const headerStyle: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'flex-start',
  marginBottom: 12,
};

const speciesStyle: React.CSSProperties = {
  fontSize: '16px',
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
  margin: 0,
};

const varietyStyle: React.CSSProperties = {
  fontSize: '12px',
  color: 'var(--cp-text-dim, #aaa)',
  margin: '2px 0 0',
};

const difficultyBadge = (d: string): React.CSSProperties => ({
  padding: '2px 10px',
  borderRadius: '20px',
  fontSize: '11px',
  fontWeight: 600,
  background: d === 'Beginner' ? 'var(--cp-success-bg, #1b3a2a)' : d === 'Intermediate' ? 'var(--cp-warning-bg, #3a3520)' : 'var(--cp-error-bg, #3a2020)',
  color: d === 'Beginner' ? 'var(--cp-success, #4ade80)' : d === 'Intermediate' ? 'var(--cp-warning, #facc15)' : 'var(--cp-error, #f87171)',
});

const rowStyle: React.CSSProperties = {
  display: 'flex',
  gap: '16px',
  marginBottom: 12,
  fontSize: '13px',
  color: 'var(--cp-text-dim, #aaa)',
};

const metricBoxStyle: React.CSSProperties = {
  flex: 1,
  padding: '8px 10px',
  background: 'var(--cp-surface-alt, #16213e)',
  borderRadius: '8px',
  textAlign: 'center',
};

const metricValueStyle: React.CSSProperties = {
  fontSize: '15px',
  fontWeight: 700,
  color: 'var(--cp-accent, #4ade80)',
};

const metricLabelStyle: React.CSSProperties = {
  fontSize: '10px',
  textTransform: 'uppercase' as const,
  letterSpacing: '0.5px',
  color: 'var(--cp-text-dim, #888)',
};

const listTitleStyle: React.CSSProperties = {
  fontSize: '12px',
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
  marginBottom: 4,
};

const listStyle: React.CSSProperties = {
  margin: '0 0 8px',
  paddingLeft: 16,
  fontSize: '12px',
  color: 'var(--cp-text-dim, #aaa)',
};

interface GrowerSpawnCardProps {
  recommendation: SpawnRecommendation;
}

export default function GrowerSpawnCard({ recommendation }: GrowerSpawnCardProps) {
  const { speciesName, variety, difficulty, optimalTempLow, optimalTempHigh, optimalHumidityLow, optimalHumidityHigh, expectedYieldKg, advantages, disadvantages, description } = recommendation;

  return (
    <div style={cardStyle}>
      <div style={headerStyle}>
        <div>
          <h4 style={speciesStyle}>{speciesName}</h4>
          <p style={varietyStyle}>{variety}</p>
        </div>
        <span style={difficultyBadge(difficulty)}>{difficulty}</span>
      </div>

      <p style={{ fontSize: '12px', color: 'var(--cp-text-dim, #aaa)', margin: '0 0 12px' }}>{description}</p>

      <div style={rowStyle}>
        <div style={metricBoxStyle}>
          <div style={metricValueStyle}>{optimalTempLow}–{optimalTempHigh}°C</div>
          <div style={metricLabelStyle}>Temp Range</div>
        </div>
        <div style={metricBoxStyle}>
          <div style={metricValueStyle}>{optimalHumidityLow}–{optimalHumidityHigh}%</div>
          <div style={metricLabelStyle}>Humidity</div>
        </div>
        <div style={metricBoxStyle}>
          <div style={metricValueStyle}>{expectedYieldKg} kg</div>
          <div style={metricLabelStyle}>Est. Yield</div>
        </div>
      </div>

      {advantages.length > 0 && (
        <>
          <div style={listTitleStyle}>✅ Advantages</div>
          <ul style={listStyle}>
            {advantages.map(a => <li key={a}>{a}</li>)}
          </ul>
        </>
      )}

      {disadvantages.length > 0 && (
        <>
          <div style={listTitleStyle}>⚠️ Disadvantages</div>
          <ul style={listStyle}>
            {disadvantages.map(d => <li key={d}>{d}</li>)}
          </ul>
        </>
      )}
    </div>
  );
}
