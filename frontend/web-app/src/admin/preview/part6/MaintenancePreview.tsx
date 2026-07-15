import { useState } from 'react';
import { OperationalStateDisplay } from '../../operational-states';
import { SystemStatusPanel, useSystemStatus } from '../../system-status';
import { Icon } from '../../../design-system/icons/Icon';

export function MaintenancePreview() {
  const { services, overallStatus } = useSystemStatus();
  const [mode, setMode] = useState<'normal' | 'maintenance' | 'updating'>('normal');

  const statusBanner = mode === 'maintenance' ? {
    icon: 'tool',
    title: 'Under Maintenance',
    description: 'Scheduled maintenance is in progress. Some features may be unavailable.',
    color: 'var(--color-warning)',
  } : mode === 'updating' ? {
    icon: 'refresh-cw',
    title: 'System Updating',
    description: 'We are rolling out an update. This should only take a few minutes.',
    color: 'var(--color-info)',
  } : null;

  return (
    <div style={{ padding: 32, display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0 }}>Maintenance Mode & System Status</h2>

      <div style={{ display: 'flex', gap: 8 }}>
        {(['normal', 'maintenance', 'updating'] as const).map((m) => (
          <button
            key={m}
            onClick={() => setMode(m)}
            style={{
              padding: '8px 16px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)',
              background: mode === m ? 'var(--color-primary)' : 'var(--color-surface)',
              color: mode === m ? '#fff' : 'var(--color-text-primary)',
              cursor: 'pointer', fontSize: 'var(--text-body)',
            }}
          >
            {m.charAt(0).toUpperCase() + m.slice(1)}
          </button>
        ))}
      </div>

      {/* Status Banner */}
      {statusBanner && (
        <div
          role="alert"
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: 12,
            padding: '12px 20px',
            borderRadius: 'var(--radius-lg)',
            background: `${statusBanner.color}15`,
            border: `1px solid ${statusBanner.color}30`,
            color: statusBanner.color,
          }}
        >
          <Icon name={statusBanner.icon} size={20} />
          <div>
            <div style={{ fontWeight: 600, fontSize: 'var(--text-body)' }}>{statusBanner.title}</div>
            <div style={{ fontSize: 'var(--text-caption)', opacity: 0.8 }}>{statusBanner.description}</div>
          </div>
        </div>
      )}

      {/* Maintenance Blocks */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 16 }}>
        <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
          <div style={{ padding: '4px 8px', fontSize: 10, color: 'var(--color-text-tertiary)', textTransform: 'uppercase' }}>maintenance</div>
          <OperationalStateDisplay state="maintenance" />
        </div>
        <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
          <div style={{ padding: '4px 8px', fontSize: 10, color: 'var(--color-text-tertiary)', textTransform: 'uppercase' }}>system_updating</div>
          <OperationalStateDisplay state="system_updating" />
        </div>
        <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
          <div style={{ padding: '4px 8px', fontSize: 10, color: 'var(--color-text-tertiary)', textTransform: 'uppercase' }}>server_unavailable</div>
          <OperationalStateDisplay state="server_unavailable" />
        </div>
      </div>

      {/* System Status */}
      <section>
        <h3 style={{ marginBottom: 8 }}>System Status</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 16 }}>
          <SystemStatusPanel services={services} lastUpdated="Just now" />
          <SystemStatusPanel services={services} lastUpdated="Just now" compact />
        </div>
        <div style={{ marginTop: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          Overall: <strong style={{ color: overallStatus === 'operational' ? 'var(--color-success)' : overallStatus === 'degraded' ? 'var(--color-warning)' : 'var(--color-error)' }}>{overallStatus}</strong>
        </div>
      </section>

      {/* Maintenance actions */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Maintenance Mode Actions</h3>
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
          <OperationalStateDisplay
            state="maintenance"
            config={{
              title: 'Scheduled Maintenance',
              description: 'The system is undergoing scheduled maintenance from 2:00 AM to 4:00 AM UTC.',
              action: { label: 'Check status page', onClick: () => alert('Status page') },
              secondaryAction: { label: 'Contact support', onClick: () => alert('Support') },
            }}
          />
        </div>
      </section>
    </div>
  );
}
