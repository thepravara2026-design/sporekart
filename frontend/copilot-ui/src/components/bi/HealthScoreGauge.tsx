import React from 'react';
import type { CompanyHealthScore } from './types/bi';

interface HealthScoreGaugeProps {
  healthScore: CompanyHealthScore;
}

function getScoreColor(score: number): string {
  if (score < 40) return '#ef4444';
  if (score < 60) return '#f97316';
  if (score < 75) return '#eab308';
  if (score < 90) return '#22c55e';
  return '#16a34a';
}

function getScoreLabel(score: number): string {
  if (score < 40) return 'Critical';
  if (score < 60) return 'At Risk';
  if (score < 75) return 'Fair';
  if (score < 90) return 'Good';
  return 'Excellent';
}

function TrendIndicator({ trend }: { trend: string }) {
  const up = trend.toLowerCase() === 'up' || trend.toLowerCase() === 'increasing';
  const down = trend.toLowerCase() === 'down' || trend.toLowerCase() === 'decreasing';
  return (
    <span style={{ color: up ? '#22c55e' : down ? '#ef4444' : '#6b7280', fontSize: 14, fontWeight: 600 }}>
      {up ? '\u25B2' : down ? '\u25BC' : '\u25C6'} {trend}
    </span>
  );
}

function SingleGauge({ label, score, size = 80 }: { label: string; score: number; size?: number }) {
  const cx = size / 2; const cy = size / 2; const r = (size - 12) / 2; const sw = 6;
  const circ = 2 * Math.PI * r;
  const offset = circ - (score / 100) * circ;
  const color = getScoreColor(score);
  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4 }}>
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        <circle cx={cx} cy={cy} r={r} fill="none" stroke="#e5e7eb" strokeWidth={sw} />
        <circle cx={cx} cy={cy} r={r} fill="none" stroke={color} strokeWidth={sw} strokeDasharray={circ} strokeDashoffset={offset} strokeLinecap="round" transform={`rotate(-90 ${cx} ${cy})`} />
        <text x={cx} y={cy} textAnchor="middle" dominantBaseline="central" fontSize={size * 0.22} fontWeight={700} fill={color}>{score}</text>
      </svg>
      <span style={{ fontSize: 11, color: '#6b7280', textAlign: 'center', lineHeight: 1.2 }}>{label}</span>
    </div>
  );
}

export default function HealthScoreGauge({ healthScore }: HealthScoreGaugeProps) {
  const { overall, revenueScore, customerScore, trainingScore, inventoryScore, operationsScore, growthScore, trend, factors } = healthScore;
  const size = 200; const cx = size / 2; const cy = size / 2; const r = (size - 20) / 2; const sw = 14;
  const circ = 2 * Math.PI * r;
  const offset = circ - (overall / 100) * circ;
  const color = getScoreColor(overall);

  const subScores = [
    { label: 'Revenue', score: revenueScore },
    { label: 'Customer', score: customerScore },
    { label: 'Training', score: trainingScore },
    { label: 'Inventory', score: inventoryScore },
    { label: 'Operations', score: operationsScore },
    { label: 'Growth', score: growthScore },
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 32, flexWrap: 'wrap' }}>
        <div style={{ position: 'relative', display: 'inline-flex', alignItems: 'center', justifyContent: 'center' }}>
          <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
            <circle cx={cx} cy={cy} r={r} fill="none" stroke="#e5e7eb" strokeWidth={sw} />
            <circle cx={cx} cy={cy} r={r} fill="none" stroke={color} strokeWidth={sw} strokeDasharray={circ} strokeDashoffset={offset} strokeLinecap="round" transform={`rotate(-90 ${cx} ${cy})`} />
          </svg>
          <div style={{ position: 'absolute', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <span style={{ fontSize: 36, fontWeight: 800, color, lineHeight: 1 }}>{overall}</span>
            <span style={{ fontSize: 12, color: '#6b7280', fontWeight: 500 }}>{getScoreLabel(overall)}</span>
          </div>
        </div>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          <div style={{ fontSize: 16, fontWeight: 700, color: '#111827' }}>Company Health Score</div>
          <TrendIndicator trend={trend} />
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4, marginTop: 8 }}>
            {factors.map(f => (
              <div key={f.name} style={{ display: 'flex', alignItems: 'center', gap: 8, fontSize: 12 }}>
                <span style={{ color: getScoreColor(f.score), fontWeight: 700, minWidth: 30 }}>{f.score}</span>
                <span style={{ color: '#374151', minWidth: 100 }}>{f.name}</span>
                <span style={{ color: '#9ca3af' }}>{f.status}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(100px, 1fr))', gap: 16 }}>
        {subScores.map(s => <SingleGauge key={s.label} label={s.label} score={s.score} />)}
      </div>
    </div>
  );
}
