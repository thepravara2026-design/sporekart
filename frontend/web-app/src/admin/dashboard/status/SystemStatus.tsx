import { Icon } from '../../../design-system/icons/Icon';
import type { SystemStatusData } from '../types';

interface SystemStatusProps {
  services: SystemStatusData[];
  columns?: number;
}

const statusConfig: Record<string, { color: string; label: string }> = {
  operational: { color: 'var(--color-success)', label: 'Operational' },
  degraded: { color: 'var(--color-warning)', label: 'Degraded' },
  down: { color: 'var(--color-error)', label: 'Down' },
  maintenance: { color: 'var(--color-info)', label: 'Maintenance' },
};

export function SystemStatus({ services, columns = 4 }: SystemStatusProps) {
  if (services.length === 0) {
    return (
      <div style={{ textAlign: 'center', padding: '24px 16px', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body)' }}>
        No services to display
      </div>
    );
  }

  return (
    <div
      style={{
        display: 'grid',
        gridTemplateColumns: `repeat(${Math.min(columns, services.length)}, 1fr)`,
        gap: 12,
      }}
    >
      {services.map((service) => {
        const config = statusConfig[service.status] || statusConfig.operational;
        return (
          <div
            key={service.id}
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: 10,
              padding: '10px 12px',
              borderRadius: 'var(--radius-md)',
              border: '1px solid var(--color-border)',
              background: 'var(--color-surface)',
            }}
          >
            <div style={{ width: 28, height: 28, borderRadius: 'var(--radius-md)', background: `${config.color}1A`, display: 'flex', alignItems: 'center', justifyContent: 'center', color: config.color, flexShrink: 0 }}>
              <Icon name={service.icon} size={14} />
            </div>
            <div style={{ flex: 1, minWidth: 0 }}>
              <div style={{ fontSize: 'var(--text-body)', fontWeight: 500, color: 'var(--color-text-primary)' }}>{service.label}</div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Uptime: {service.uptime}</div>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 4, flexShrink: 0 }}>
              <span style={{ width: 8, height: 8, borderRadius: '50%', background: config.color }} />
              <span style={{ fontSize: 'var(--text-caption)', color: config.color, fontWeight: 500 }}>{config.label}</span>
            </div>
          </div>
        );
      })}
    </div>
  );
}
