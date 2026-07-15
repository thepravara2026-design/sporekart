import { memo } from 'react';
import type { HealthMetric } from '../types';
import { getHealthColor, getHealthVariant } from '../utils';
import { StatusBadge } from '../../../components/status/StatusBadge';

interface HealthCardProps {
  health: HealthMetric;
  loading?: boolean;
}

export const HealthCard = memo(function HealthCard({ health, loading }: HealthCardProps) {
  if (loading) {
    return (
      <div aria-busy="true" style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: '20px 24px' }}>
        <div style={{ width: '60%', height: 16, background: 'var(--color-surface-hover)', borderRadius: 4, marginBottom: 16, animation: 'shimmer 1.5s infinite' }} />
        <div style={{ width: 80, height: 80, borderRadius: '50%', background: 'var(--color-surface-hover)', margin: '0 auto', animation: 'shimmer 1.5s infinite' }} />
      </div>
    );
  }

  const color = getHealthColor(health.score);
  const size = 96;
  const stroke = 10;
  const radius = (size - stroke) / 2;
  const circumference = 2 * Math.PI * radius;
  const offset = circumference - (health.score / 100) * circumference;

  return (
    <div style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: '20px 24px', textAlign: 'center' }}>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontWeight: 500, textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: 16 }}>
        {health.label}
      </div>
      <div style={{ position: 'relative', width: size, height: size, margin: '0 auto' }}>
        <svg width={size} height={size} role="img" aria-label={`${health.label} score ${health.score} out of 100`}>
          <circle cx={size / 2} cy={size / 2} r={radius} fill="none" stroke="var(--color-surface-hover)" strokeWidth={stroke} />
          <circle
            cx={size / 2} cy={size / 2} r={radius} fill="none" stroke={color} strokeWidth={stroke}
            strokeDasharray={circumference} strokeDashoffset={offset} strokeLinecap="round"
            transform={`rotate(-90 ${size / 2} ${size / 2})`}
          />
        </svg>
        <div style={{ position: 'absolute', inset: 0, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 'var(--text-h2)', fontWeight: 700, color: 'var(--color-text-primary)' }}>
          {health.score}%
        </div>
      </div>
      <div style={{ marginTop: 12 }}>
        <StatusBadge status={health.score >= 90 ? 'Healthy' : health.score >= 75 ? 'Monitor' : 'Risk'} variant={getHealthVariant(health.score)} />
      </div>
    </div>
  );
});
