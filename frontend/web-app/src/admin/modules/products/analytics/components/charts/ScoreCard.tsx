import React, { memo } from 'react';

export interface ScoreCardProps {
  label: string;
  value: number | string;
  unit?: string;
  trend?: 'up' | 'down' | 'neutral';
  trendValue?: string;
  icon: string;
  color: string;
}

const trendIcons: Record<string, string> = {
  up: '\u25B2',
  down: '\u25BC',
  neutral: '\u25C6',
};

const trendColors: Record<string, string> = {
  up: 'var(--color-success)',
  down: 'var(--color-danger)',
  neutral: 'var(--color-text-secondary)',
};

export const ScoreCard: React.FC<ScoreCardProps> = memo(({ label, value, unit, trend, trendValue, icon, color }) => {
  return (
    <div
      aria-label={`${label}: ${value}${unit || ''}${trend ? `, ${trend} ${trendValue || ''}` : ''}`}
      style={{
        background: `linear-gradient(135deg, ${color}10, var(--color-bg-surface-default))`,
        borderRadius: 'var(--radius-md)',
        border: `1px solid var(--color-border)`,
        padding: 16,
        display: 'flex',
        flexDirection: 'column',
        gap: 8,
        width: '100%',
        overflow: 'hidden',
      }}
    >
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <span style={{ fontSize: 20, color, lineHeight: 1 }}>{icon}</span>
        {trend && (
          <div style={{ display: 'flex', alignItems: 'center', gap: 4, fontSize: 'var(--text-body-xs)', color: trendColors[trend] }}>
            <span style={{ fontSize: 'var(--text-body-xs)' }}>{trendIcons[trend]}</span>
            {trendValue && <span>{trendValue}</span>}
          </div>
        )}
      </div>
      <div style={{ display: 'flex', alignItems: 'baseline', gap: 4 }}>
        <span style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', lineHeight: 1.2 }}>{value}</span>
        {unit && <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>{unit}</span>}
      </div>
      <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>{label}</span>
    </div>
  );
});

ScoreCard.displayName = 'ScoreCard';
export default ScoreCard;
