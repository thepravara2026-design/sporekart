import React, { useState } from 'react';
import type { CultivationStage } from './types/grower';

const stageIcons: Record<string, string> = {
  'spawn': '🧫',
  'inoculation': '💉',
  'incubation': '🌡️',
  'casing': '🧤',
  'pinning': '📌',
  'fruiting': '🍄',
  'harvest': '🧺',
  'postharvest': '📦',
};

const defaultStages: CultivationStage[] = [
  { stageId: 'spawn', stageName: 'Spawn Run', stageOrder: 1, description: 'Mycelium colonises the spawn medium.', durationDays: 10, optimalTempCelsius: 24, optimalHumidityPercent: 90, lightRequirement: 'Dark', co2Ppm: 5000, ventilationRequirement: 'Minimal', commonIssues: ['Contamination', 'Slow growth'] },
  { stageId: 'inoculation', stageName: 'Inoculation', stageOrder: 2, description: 'Spawn is mixed into the substrate.', durationDays: 1, optimalTempCelsius: 24, optimalHumidityPercent: 90, lightRequirement: 'Dark', co2Ppm: 5000, ventilationRequirement: 'Minimal', commonIssues: ['Uneven distribution'] },
  { stageId: 'incubation', stageName: 'Incubation', stageOrder: 3, description: 'Mycelium spreads through substrate.', durationDays: 14, optimalTempCelsius: 22, optimalHumidityPercent: 95, lightRequirement: 'Dark', co2Ppm: 4000, ventilationRequirement: 'Low', commonIssues: ['Overheating', 'Dry substrate'] },
  { stageId: 'casing', stageName: 'Casing Layer', stageOrder: 4, description: 'A moisture-retaining layer is added.', durationDays: 2, optimalTempCelsius: 22, optimalHumidityPercent: 98, lightRequirement: 'Indirect', co2Ppm: 3000, ventilationRequirement: 'Moderate', commonIssues: ['Drying out'] },
  { stageId: 'pinning', stageName: 'Pinning', stageOrder: 5, description: 'Primordia (pinheads) form.', durationDays: 7, optimalTempCelsius: 20, optimalHumidityPercent: 95, lightRequirement: '12h light', co2Ppm: 1500, ventilationRequirement: 'Moderate', commonIssues: ['Aborted pins', 'High CO₂'] },
  { stageId: 'fruiting', stageName: 'Fruiting', stageOrder: 6, description: 'Mushrooms mature and develop.', durationDays: 7, optimalTempCelsius: 18, optimalHumidityPercent: 88, lightRequirement: '12h light', co2Ppm: 800, ventilationRequirement: 'High', commonIssues: ['Long stems', 'Small caps'] },
  { stageId: 'harvest', stageName: 'Harvest', stageOrder: 7, description: 'Mushrooms are picked at peak size.', durationDays: 3, optimalTempCelsius: 18, optimalHumidityPercent: 85, lightRequirement: 'Ambient', co2Ppm: 600, ventilationRequirement: 'High', commonIssues: ['Over-maturity', 'Spore drop'] },
  { stageId: 'postharvest', stageName: 'Post-Harvest', stageOrder: 8, description: 'Cleaning, grading, and storage.', durationDays: 1, optimalTempCelsius: 4, optimalHumidityPercent: 75, lightRequirement: 'Low', co2Ppm: 400, ventilationRequirement: 'Moderate', commonIssues: ['Moisture loss', 'Bruising'] },
];

interface GrowerCultivationTimelineProps {
  stages?: CultivationStage[];
  currentStageId?: string;
}

const sectionStyle: React.CSSProperties = {
  padding: '16px',
  background: 'var(--cp-surface, #1a1a2e)',
  borderRadius: '12px',
  border: '1px solid var(--cp-border, #2a2a4a)',
};

const titleStyle: React.CSSProperties = {
  margin: '0 0 16px',
  fontSize: '18px',
  fontWeight: 600,
  color: 'var(--cp-text, #e0e0e0)',
};

const progressTrackStyle: React.CSSProperties = {
  position: 'relative',
  display: 'flex',
  alignItems: 'center',
  gap: '4px',
  padding: '12px 0',
  overflowX: 'auto',
};

const nodeContainerStyle = (isCurrent: boolean): React.CSSProperties => ({
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  flexShrink: 0,
  cursor: 'pointer',
  opacity: isCurrent ? 1 : 0.6,
  transition: 'opacity 0.2s',
});

const nodeCircleStyle = (completed: boolean, isCurrent: boolean): React.CSSProperties => ({
  width: 40,
  height: 40,
  borderRadius: '50%',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  fontSize: '18px',
  background: completed
    ? 'var(--cp-accent, #4ade80)'
    : isCurrent
      ? 'var(--cp-accent-dim, #2e7d4f)'
      : 'var(--cp-border, #2a2a4a)',
  border: isCurrent ? '2px solid var(--cp-accent, #4ade80)' : '2px solid transparent',
  transition: 'all 0.3s',
});

const nodeLabelStyle: React.CSSProperties = {
  marginTop: '6px',
  fontSize: '11px',
  color: 'var(--cp-text-dim, #888)',
  whiteSpace: 'nowrap',
};

const connectorStyle = (completed: boolean): React.CSSProperties => ({
  flex: '1',
  minWidth: 20,
  height: 3,
  borderRadius: 2,
  background: completed ? 'var(--cp-accent, #4ade80)' : 'var(--cp-border, #2a2a4a)',
  transition: 'background 0.3s',
});

const expandedCardStyle: React.CSSProperties = {
  marginTop: '16px',
  padding: '16px',
  background: 'var(--cp-surface-alt, #16213e)',
  borderRadius: '8px',
  border: '1px solid var(--cp-border, #2a2a4a)',
};

const detailGridStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: '1fr 1fr',
  gap: '8px',
  fontSize: '13px',
  color: 'var(--cp-text-dim, #aaa)',
};

const detailLabelStyle: React.CSSProperties = { fontWeight: 600, color: 'var(--cp-text, #e0e0e0)' };

export default function GrowerCultivationTimeline({
  stages = defaultStages,
  currentStageId = 'pinning',
}: GrowerCultivationTimelineProps) {
  const [expandedId, setExpandedId] = useState<string | null>(null);

  const sorted = [...stages].sort((a, b) => a.stageOrder - b.stageOrder);
  const currentIdx = sorted.findIndex(s => s.stageId === currentStageId);
  const resolvedCurrent = currentIdx >= 0 ? currentIdx : 0;
  const expanded = sorted.find(s => s.stageId === expandedId);

  return (
    <div style={sectionStyle}>
      <h3 style={titleStyle}>Cultivation Timeline</h3>

      <div style={progressTrackStyle}>
        {sorted.map((s, i) => {
          const isCurrent = i === resolvedCurrent;
          const completed = i < resolvedCurrent;
          return (
            <React.Fragment key={s.stageId}>
              {i > 0 && <div style={connectorStyle(completed)} />}
              <div style={nodeContainerStyle(isCurrent)} onClick={() => setExpandedId(expandedId === s.stageId ? null : s.stageId)}>
                <div style={nodeCircleStyle(completed, isCurrent)}>
                  {stageIcons[s.stageId] ?? '●'}
                </div>
                <span style={nodeLabelStyle}>{s.stageName}</span>
              </div>
            </React.Fragment>
          );
        })}
      </div>

      {expanded && (
        <div style={expandedCardStyle}>
          <div style={{ fontSize: '15px', fontWeight: 600, color: 'var(--cp-text, #e0e0e0)', marginBottom: 8 }}>
            {stageIcons[expanded.stageId] ?? '●'} {expanded.stageName}
          </div>
          <p style={{ fontSize: '13px', color: 'var(--cp-text-dim, #aaa)', margin: '0 0 12px' }}>{expanded.description}</p>
          <div style={detailGridStyle}>
            <span><span style={detailLabelStyle}>Duration:</span> {expanded.durationDays} days</span>
            <span><span style={detailLabelStyle}>Temp:</span> {expanded.optimalTempCelsius}°C</span>
            <span><span style={detailLabelStyle}>Humidity:</span> {expanded.optimalHumidityPercent}%</span>
            <span><span style={detailLabelStyle}>Light:</span> {expanded.lightRequirement}</span>
            <span><span style={detailLabelStyle}>CO₂:</span> {expanded.co2Ppm} ppm</span>
            <span><span style={detailLabelStyle}>Ventilation:</span> {expanded.ventilationRequirement}</span>
          </div>
          {expanded.commonIssues.length > 0 && (
            <div style={{ marginTop: 12 }}>
              <span style={{ ...detailLabelStyle, fontSize: '13px' }}>Common Issues:</span>
              <ul style={{ margin: '4px 0 0', paddingLeft: 18, fontSize: '13px', color: 'var(--cp-text-dim, #aaa)' }}>
                {expanded.commonIssues.map((issue) => (
                  <li key={issue}>{issue}</li>
                ))}
              </ul>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
