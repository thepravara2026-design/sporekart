import React from 'react';
import type { DiseaseInfo } from './types/grower';

const severityColor: Record<string, { bg: string; text: string }> = {
  LOW: { bg: '#1b3a2a', text: '#4ade80' },
  MODERATE: { bg: '#3a3520', text: '#facc15' },
  HIGH: { bg: '#3a2a1a', text: '#fb923c' },
  CRITICAL: { bg: '#3a2020', text: '#f87171' },
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
  alignItems: 'flex-start',
  marginBottom: 4,
};

const diseaseNameStyle: React.CSSProperties = {
  fontSize: '16px',
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
  margin: 0,
};

const scientificStyle: React.CSSProperties = {
  fontStyle: 'italic',
  fontSize: '12px',
  color: 'var(--cp-text-dim, #aaa)',
  margin: '2px 0 12px',
};

const probRowStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '10px',
  marginBottom: 12,
};

const probBarTrackStyle: React.CSSProperties = {
  flex: 1,
  height: 8,
  borderRadius: 4,
  background: 'var(--cp-border, #2a2a4a)',
  overflow: 'hidden',
};

const probBarFillStyle = (score: number): React.CSSProperties => ({
  height: '100%',
  width: `${Math.min(score, 100)}%`,
  borderRadius: 4,
  background: score > 75 ? '#f87171' : score > 50 ? '#fb923c' : score > 25 ? '#facc15' : '#4ade80',
  transition: 'width 0.4s',
});

const sectionLabelStyle: React.CSSProperties = {
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

const textBlockStyle: React.CSSProperties = {
  fontSize: '12px',
  color: 'var(--cp-text-dim, #aaa)',
  margin: '0 0 8px',
  lineHeight: 1.5,
};

interface GrowerDiseaseCardProps {
  disease: DiseaseInfo;
}

export default function GrowerDiseaseCard({ disease }: GrowerDiseaseCardProps) {
  const sev = severityColor[disease.severity] ?? severityColor.HIGH;

  return (
    <div style={cardStyle}>
      <div style={headerStyle}>
        <div>
          <h4 style={diseaseNameStyle}>{disease.diseaseName}</h4>
          <p style={scientificStyle}>{disease.scientificName}</p>
        </div>
        <span style={{
          padding: '2px 10px',
          borderRadius: '20px',
          fontSize: '11px',
          fontWeight: 600,
          background: sev.bg,
          color: sev.text,
        }}>
          {disease.severity}
        </span>
      </div>

      <div style={probRowStyle}>
        <span style={{ fontSize: '12px', color: 'var(--cp-text-dim, #aaa)', whiteSpace: 'nowrap' }}>Probability</span>
        <div style={probBarTrackStyle}>
          <div style={probBarFillStyle(disease.probabilityScore)} />
        </div>
        <span style={{ fontSize: '12px', fontWeight: 600, color: 'var(--cp-text, #e0e0e0)', minWidth: 30, textAlign: 'right' }}>
          {disease.probabilityScore}%
        </span>
      </div>

      <div style={sectionLabelStyle}>🩺 Symptoms</div>
      <ul style={listStyle}>
        {disease.symptoms.map(s => <li key={s}>{s}</li>)}
      </ul>

      <div style={sectionLabelStyle}>💊 Treatment</div>
      <p style={textBlockStyle}>{disease.treatmentPlan}</p>

      <div style={sectionLabelStyle}>🛡️ Prevention</div>
      <ul style={listStyle}>
        {disease.preventionMethods.map(p => <li key={p}>{p}</li>)}
      </ul>
    </div>
  );
}
