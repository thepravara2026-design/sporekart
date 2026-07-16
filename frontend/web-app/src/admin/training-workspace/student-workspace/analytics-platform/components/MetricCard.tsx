import { memo } from 'react';

interface MetricCardProps {
  label: string;
  value: string | number;
  subtitle?: string;
  color?: string;
  icon?: string;
}

export const MetricCard = memo(function MetricCard({ label, value, subtitle, color, icon }: MetricCardProps) {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: `1px solid ${color || 'var(--color-border-default)'}`,
      background: color ? `${color}0d` : 'var(--color-bg-surface-default)',
      display: 'flex', flexDirection: 'column', gap: 4,
    }}>
      {icon && <span style={{ fontSize: 20 }}>{icon}</span>}
      <span style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: color || 'inherit' }}>{value}</span>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{label}</span>
      {subtitle && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{subtitle}</span>}
    </div>
  );
});
