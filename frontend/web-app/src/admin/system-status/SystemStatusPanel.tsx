import { memo } from 'react';
import { SystemStatusIndicator } from './SystemStatusIndicator';
import type { SystemService } from './types';

interface SystemStatusPanelProps {
  services: SystemService[];
  lastUpdated?: string;
  compact?: boolean;
}

export const SystemStatusPanel = memo(function SystemStatusPanel({ services, lastUpdated, compact = false }: SystemStatusPanelProps) {
  const operationalCount = services.filter((s) => s.status === 'operational').length;

  return (
    <div
      role="region"
      aria-label="System Status"
      style={{
        border: '1px solid var(--color-border)',
        borderRadius: 'var(--radius-lg)',
        overflow: 'hidden',
        background: 'var(--color-surface)',
      }}
    >
      <div style={{ padding: '12px 16px', borderBottom: '1px solid var(--color-border)', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <span style={{ fontWeight: 600, fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>System Status</span>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          {operationalCount}/{services.length} operational
        </span>
      </div>
      <div>
        {services.map((service) => (
          <div
            key={service.id}
            style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              padding: compact ? '8px 16px' : '10px 16px',
              borderBottom: '1px solid var(--color-border)',
            }}
          >
            <div>
              <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)', fontWeight: 500 }}>{service.label}</div>
              {service.description && (
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginTop: 2 }}>{service.description}</div>
              )}
            </div>
            <SystemStatusIndicator status={service.status} size={compact ? 'sm' : 'md'} />
          </div>
        ))}
      </div>
      {lastUpdated && (
        <div style={{ padding: '8px 16px', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', borderTop: '1px solid var(--color-border)' }}>
          Last updated: {lastUpdated}
        </div>
      )}
    </div>
  );
});
