import React from 'react';
import type { WeatherData } from './types/grower';

const conditionIcons: Record<string, string> = {
  'sunny': '☀️',
  'clear': '🌙',
  'partly cloudy': '⛅',
  'cloudy': '☁️',
  'overcast': '☁️',
  'light rain': '🌦️',
  'rain': '🌧️',
  'heavy rain': '🌧️',
  'thunderstorm': '⛈️',
  'humid': '💧',
  'fog': '🌫️',
  'windy': '💨',
};

function getIcon(condition: string): string {
  const key = condition.toLowerCase();
  for (const [k, v] of Object.entries(conditionIcons)) {
    if (key.includes(k)) return v;
  }
  return '🌡️';
}

const riskColor: Record<string, { bg: string; text: string }> = {
  low: { bg: '#1b3a2a', text: '#4ade80' },
  moderate: { bg: '#3a3520', text: '#facc15' },
  high: { bg: '#3a2a1a', text: '#fb923c' },
  extreme: { bg: '#3a2020', text: '#f87171' },
};

const cardStyle: React.CSSProperties = {
  padding: '16px',
  background: 'var(--cp-surface, #1a1a2e)',
  borderRadius: '12px',
  border: '1px solid var(--cp-border, #2a2a4a)',
};

const headerStyle: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  marginBottom: 16,
};

const locationStyle: React.CSSProperties = {
  fontSize: '16px',
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
  margin: 0,
};

const mainRowStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '16px',
  marginBottom: 16,
};

const iconLargeStyle: React.CSSProperties = {
  fontSize: '40px',
  lineHeight: 1,
};

const tempStyle: React.CSSProperties = {
  fontSize: '28px',
  fontWeight: 700,
  color: 'var(--cp-text, #e0e0e0)',
};

const conditionStyle: React.CSSProperties = {
  fontSize: '13px',
  color: 'var(--cp-text-dim, #aaa)',
  marginTop: 2,
};

const detailRowStyle: React.CSSProperties = {
  display: 'flex',
  gap: '12px',
  marginBottom: 16,
};

const detailBoxStyle: React.CSSProperties = {
  flex: 1,
  padding: '8px',
  background: 'var(--cp-surface-alt, #16213e)',
  borderRadius: '8px',
  textAlign: 'center',
};

const detailBoxValue: React.CSSProperties = {
  fontSize: '16px',
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
};

const detailBoxLabel: React.CSSProperties = {
  fontSize: '10px',
  textTransform: 'uppercase' as const,
  letterSpacing: '0.5px',
  color: 'var(--cp-text-dim, #888)',
};

const forecastRowStyle: React.CSSProperties = {
  display: 'flex',
  gap: '8px',
  overflowX: 'auto',
  paddingBottom: 4,
};

const miniCardStyle: React.CSSProperties = {
  flexShrink: 0,
  width: 80,
  padding: '8px 6px',
  background: 'var(--cp-surface-alt, #16213e)',
  borderRadius: '8px',
  textAlign: 'center',
  border: '1px solid var(--cp-border, #2a2a4a)',
};

const miniDateStyle: React.CSSProperties = {
  fontSize: '10px',
  color: 'var(--cp-text-dim, #888)',
  marginBottom: 4,
};

const miniIconStyle: React.CSSProperties = {
  fontSize: '18px',
  lineHeight: 1,
  marginBottom: 4,
};

const miniTempStyle: React.CSSProperties = {
  fontSize: '13px',
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
};

const impactStyle: React.CSSProperties = {
  marginTop: 12,
  padding: '8px 12px',
  borderRadius: '8px',
  fontSize: '12px',
  background: 'var(--cp-surface-alt, #16213e)',
  color: 'var(--cp-text-dim, #aaa)',
};

interface GrowerWeatherCardProps {
  weather: WeatherData;
  forecast?: WeatherData[];
}

export default function GrowerWeatherCard({ weather, forecast = [] }: GrowerWeatherCardProps) {
  const risk = riskColor[weather.climateRisk.toLowerCase()] ?? riskColor.low;

  return (
    <div style={cardStyle}>
      <div style={headerStyle}>
        <h4 style={locationStyle}>{weather.location}</h4>
        <span style={{
          padding: '2px 10px',
          borderRadius: '20px',
          fontSize: '11px',
          fontWeight: 600,
          background: risk.bg,
          color: risk.text,
        }}>
          {weather.climateRisk}
        </span>
      </div>

      <div style={mainRowStyle}>
        <span style={iconLargeStyle}>{getIcon(weather.condition)}</span>
        <div>
          <div style={tempStyle}>{weather.temperatureCelsius}°C</div>
          <div style={conditionStyle}>{weather.condition}</div>
        </div>
      </div>

      <div style={detailRowStyle}>
        <div style={detailBoxStyle}>
          <div style={detailBoxValue}>{weather.humidityPercent}%</div>
          <div style={detailBoxLabel}>Humidity</div>
        </div>
        <div style={detailBoxStyle}>
          <div style={detailBoxValue}>{new Date(weather.forecastDate).toLocaleDateString()}</div>
          <div style={detailBoxLabel}>As Of</div>
        </div>
      </div>

      {forecast.length > 0 && (
        <div style={forecastRowStyle}>
          {forecast.map((f, i) => (
            <div key={i} style={miniCardStyle}>
              <div style={miniDateStyle}>{new Date(f.forecastDate).toLocaleDateString(undefined, { weekday: 'short', day: 'numeric' })}</div>
              <div style={miniIconStyle}>{getIcon(f.condition)}</div>
              <div style={miniTempStyle}>{f.temperatureCelsius}°C</div>
              <div style={{ fontSize: '9px', color: 'var(--cp-text-dim, #888)' }}>{f.humidityPercent}%</div>
            </div>
          ))}
        </div>
      )}

      <div style={impactStyle}>
        🌱 Cultivation impact: {weather.temperatureCelsius > 30 ? 'Heat stress risk – increase ventilation and misting.' : weather.humidityPercent > 90 ? 'High humidity – monitor for contamination.' : weather.humidityPercent < 70 ? 'Low humidity – increase misting frequency.' : 'Conditions are favourable for cultivation.'}
      </div>
    </div>
  );
}
