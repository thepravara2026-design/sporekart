import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { MOCK_SCHEDULED } from '../data/communicationMockData';
import { TEMPLATE_KIND_LABELS } from '../data/communicationTypes';
import { formatAudience, formatDateTime, formatRelative } from '../data/communicationFormatters';
import { ChannelChip, CommEmptyState } from '../components';

const CommunicationScheduledPage = memo(function CommunicationScheduledPage() {
  const items = MOCK_SCHEDULED;

  return (
    <section id="panel-scheduled" aria-labelledby="tab-scheduled" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
      <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
        Messages queued for future delivery. Delivery is simulated (Mock Mode) — nothing is actually sent.
      </p>

      {items.length === 0 ? (
        <CommEmptyState icon="clock" title="Nothing scheduled" description="Scheduled messages will appear here." />
      ) : (
        <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
          {items.map((s) => {
            const paused = s.status === 'paused';
            return (
              <li
                key={s.id}
                style={{
                  display: 'flex', gap: 'var(--space-3)', alignItems: 'center',
                  padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
                  background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)',
                }}
              >
                <span
                  aria-hidden
                  style={{
                    display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
                    width: 40, height: 40, flexShrink: 0, borderRadius: 'var(--radius-md)',
                    background: 'var(--color-bg-primary-weak)', color: 'var(--color-primary)',
                  }}
                >
                  <Icon name={paused ? 'pause-circle' : 'clock'} size={18} />
                </span>
                <div style={{ minWidth: 0, flex: 1 }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)', flexWrap: 'wrap' }}>
                    <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{s.title}</span>
                    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>{TEMPLATE_KIND_LABELS[s.kind]}</span>
                    {paused && (
                      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-warning-700)', background: 'var(--color-warning-50)', padding: '1px 6px', borderRadius: 'var(--radius-sm)' }}>Paused</span>
                    )}
                  </div>
                  <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
                    {formatAudience(s.audience)} · {s.createdBy}
                  </p>
                  <div style={{ display: 'flex', gap: 'var(--space-1)', marginTop: 4, flexWrap: 'wrap' }}>
                    {s.channels.map((ch) => <ChannelChip key={ch} channel={ch} />)}
                  </div>
                </div>
                <div style={{ textAlign: 'right', flexShrink: 0 }}>
                  <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{formatRelative(s.scheduledFor)}</p>
                  <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>{formatDateTime(s.scheduledFor)}</p>
                </div>
              </li>
            );
          })}
        </ul>
      )}
    </section>
  );
});

export default CommunicationScheduledPage;
