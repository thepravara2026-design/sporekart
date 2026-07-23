import React from 'react';
import type { SubstrateRecommendation } from './types/grower';

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
  marginBottom: 8,
};

const nameStyle: React.CSSProperties = {
  fontSize: '16px',
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
  margin: 0,
};

const typeBadgeStyle: React.CSSProperties = {
  padding: '2px 10px',
  borderRadius: '20px',
  fontSize: '11px',
  fontWeight: 600,
  background: 'var(--cp-accent-dim, #2e7d4f)',
  color: 'var(--cp-accent, #4ade80)',
};

const descStyle: React.CSSProperties = {
  fontSize: '12px',
  color: 'var(--cp-text-dim, #aaa)',
  margin: '0 0 12px',
};

const gridStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: '1fr 1fr',
  gap: '8px',
  marginBottom: 12,
};

const gridItemStyle: React.CSSProperties = {
  padding: '8px 10px',
  background: 'var(--cp-surface-alt, #16213e)',
  borderRadius: '8px',
  fontSize: '12px',
};

const gridLabelStyle: React.CSSProperties = {
  fontSize: '10px',
  textTransform: 'uppercase' as const,
  letterSpacing: '0.5px',
  color: 'var(--cp-text-dim, #888)',
  marginBottom: 2,
};

const gridValueStyle: React.CSSProperties = {
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
};

const tagStyle: React.CSSProperties = {
  display: 'inline-block',
  padding: '2px 8px',
  borderRadius: '12px',
  fontSize: '11px',
  background: 'var(--cp-surface-alt, #16213e)',
  color: 'var(--cp-text-dim, #aaa)',
  border: '1px solid var(--cp-border, #2a2a4a)',
  margin: '2px 4px 2px 0',
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

interface GrowerSubstrateCardProps {
  recommendation: SubstrateRecommendation;
}

export default function GrowerSubstrateCard({ recommendation }: GrowerSubstrateCardProps) {
  const { name, type, description, moisturePercent, sterilizationMethod, preparationDays, costPerKg, suitableSpecies, advantages, disadvantages } = recommendation;

  return (
    <div style={cardStyle}>
      <div style={headerStyle}>
        <h4 style={nameStyle}>{name}</h4>
        <span style={typeBadgeStyle}>{type}</span>
      </div>
      <p style={descStyle}>{description}</p>

      <div style={gridStyle}>
        <div style={gridItemStyle}>
          <div style={gridLabelStyle}>Moisture</div>
          <div style={gridValueStyle}>{moisturePercent}%</div>
        </div>
        <div style={gridItemStyle}>
          <div style={gridLabelStyle}>Sterilization</div>
          <div style={gridValueStyle}>{sterilizationMethod}</div>
        </div>
        <div style={gridItemStyle}>
          <div style={gridLabelStyle}>Prep Time</div>
          <div style={gridValueStyle}>{preparationDays} days</div>
        </div>
        <div style={gridItemStyle}>
          <div style={gridLabelStyle}>Cost</div>
          <div style={gridValueStyle}>₹{costPerKg}/kg</div>
        </div>
      </div>

      {suitableSpecies.length > 0 && (
        <div style={{ marginBottom: 12 }}>
          <div style={{ ...listTitleStyle, marginBottom: 6 }}>Suitable Species</div>
          {suitableSpecies.map(s => <span key={s} style={tagStyle}>{s}</span>)}
        </div>
      )}

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
