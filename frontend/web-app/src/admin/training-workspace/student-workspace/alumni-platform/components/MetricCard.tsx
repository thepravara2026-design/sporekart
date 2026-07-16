import { memo } from 'react';

export const MetricCard = memo(function MetricCard({ label, value, color, icon, subtitle }: { label: string; value: string | number; color?: string; icon?: string; subtitle?: string }) {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: `1px solid ${color || 'var(--color-border-default)'}`,
      background: color ? `${color}0d` : 'var(--color-bg-surface-default)',
      display: 'flex', flexDirection: 'column', gap: 4,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{label}</span>
        {icon && <span style={{ fontSize: 18 }}>{icon}</span>}
      </div>
      <span style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: color || 'inherit' }}>{value}</span>
      {subtitle && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{subtitle}</span>}
    </div>
  );
});
