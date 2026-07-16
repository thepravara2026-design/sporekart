import { memo, useMemo, useState } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { MOCK_DELIVERY_QUEUE } from '../data/communicationMockData';
import { CHANNEL_LABELS } from '../data/communicationTypes';
import { CHANNEL_ICON } from '../data/communicationOptions';
import type { DeliveryQueueItem } from '../data/communicationTypes';
import type { ToneIntent } from '../data/communicationOptions';
import { formatRelative, toneTokens } from '../data/communicationFormatters';
import { CommEmptyState } from '../components';

const STATE_TONE: Record<DeliveryQueueItem['state'], ToneIntent> = {
  queued: 'neutral',
  processing: 'info',
  delivered: 'success',
  failed: 'danger',
  retry: 'warning',
};

const STATE_LABEL: Record<DeliveryQueueItem['state'], string> = {
  queued: 'Queued',
  processing: 'Processing',
  delivered: 'Delivered',
  failed: 'Failed',
  retry: 'Retry',
};

const FILTERS: Array<{ key: string; label: string }> = [
  { key: 'all', label: 'All' },
  { key: 'queued', label: 'Queued' },
  { key: 'processing', label: 'Processing' },
  { key: 'delivered', label: 'Delivered' },
  { key: 'failed', label: 'Failed' },
  { key: 'retry', label: 'Retry' },
];

const CommunicationDeliveryQueuePage = memo(function CommunicationDeliveryQueuePage() {
  const [filter, setFilter] = useState('all');

  const rows = useMemo(
    () => MOCK_DELIVERY_QUEUE.filter((d) => (filter === 'all' ? true : d.state === filter)),
    [filter],
  );

  return (
    <section id="panel-delivery" aria-labelledby="tab-delivery" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
      <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
        Simulated delivery pipeline. External channels (Email, WhatsApp, SMS, Push) are placeholders and never dispatch (Mock Mode).
      </p>

      <div role="tablist" aria-label="Delivery state filter" style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-1)' }}>
        {FILTERS.map((f) => {
          const active = filter === f.key;
          return (
            <button
              key={f.key}
              type="button"
              role="tab"
              aria-selected={active}
              onClick={() => setFilter(f.key)}
              style={{
                padding: '6px 12px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)',
                background: active ? 'var(--color-bg-primary-weak)' : 'var(--color-bg-surface-default)',
                color: active ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                fontSize: 'var(--text-body-sm)', fontWeight: active ? 600 : 500, cursor: 'pointer',
              }}
            >
              {f.label}
            </button>
          );
        })}
      </div>

      {rows.length === 0 ? (
        <CommEmptyState icon="send" title="Queue empty" description="No items match the selected state." />
      ) : (
        <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
          {rows.map((d) => {
            const tokens = toneTokens(STATE_TONE[d.state]);
            return (
              <li
                key={d.id}
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
                    width: 36, height: 36, flexShrink: 0, borderRadius: 'var(--radius-md)',
                    background: 'var(--color-bg-surface-muted)', color: 'var(--color-text-secondary)',
                  }}
                >
                  <Icon name={CHANNEL_ICON[d.channel]} size={16} />
                </span>
                <div style={{ minWidth: 0, flex: 1 }}>
                  <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{d.title}</span>
                  <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
                    {CHANNEL_LABELS[d.channel]} · {d.audienceLabel} · {formatRelative(d.queuedAt)}
                    {d.attempts > 0 ? ` · ${d.attempts} attempt${d.attempts === 1 ? '' : 's'}` : ''}
                  </p>
                </div>
                <span
                  style={{
                    flexShrink: 0, padding: '2px 10px', borderRadius: 'var(--radius-sm)',
                    fontSize: 'var(--text-caption)', fontWeight: 600,
                    color: tokens.fg, background: tokens.bg, border: `1px solid ${tokens.border}`,
                  }}
                >
                  {STATE_LABEL[d.state]}
                </span>
              </li>
            );
          })}
        </ul>
      )}
    </section>
  );
});

export default CommunicationDeliveryQueuePage;
