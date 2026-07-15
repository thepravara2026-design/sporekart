import type { ServiceStatus } from './types';

const STATUS_COLORS: Record<ServiceStatus, string> = {
  operational: 'var(--color-success)',
  degraded: 'var(--color-warning)',
  partial_outage: 'var(--color-warning)',
  major_outage: 'var(--color-error)',
  maintenance: 'var(--color-text-tertiary)',
};

const STATUS_LABELS: Record<ServiceStatus, string> = {
  operational: 'Operational',
  degraded: 'Degraded Performance',
  partial_outage: 'Partial Outage',
  major_outage: 'Major Outage',
  maintenance: 'Under Maintenance',
};

interface SystemStatusIndicatorProps {
  status: ServiceStatus;
  showLabel?: boolean;
  size?: 'sm' | 'md' | 'lg';
}

export function SystemStatusIndicator({ status, showLabel = true, size = 'sm' }: SystemStatusIndicatorProps) {
  const dotSize = size === 'lg' ? 12 : size === 'md' ? 8 : 6;

  return (
    <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6 }}>
      <span
        style={{
          width: dotSize,
          height: dotSize,
          borderRadius: '50%',
          background: STATUS_COLORS[status],
          flexShrink: 0,
        }}
      />
      {showLabel && (
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
          {STATUS_LABELS[status]}
        </span>
      )}
    </span>
  );
}
