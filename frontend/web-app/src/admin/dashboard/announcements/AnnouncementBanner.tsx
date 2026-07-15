import { useState, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import type { AnnouncementData } from '../types';

interface AnnouncementBannerProps {
  announcements: AnnouncementData[];
}

const priorityColors: Record<string, string> = {
  low: 'var(--color-info)',
  medium: 'var(--color-warning)',
  high: 'var(--color-error)',
  critical: 'var(--color-error)',
};

const categoryIcons: Record<string, string> = {
  system: 'settings',
  update: 'refresh-cw',
  maintenance: 'tool',
  feature: 'zap',
  alert: 'alert-triangle',
};

export const AnnouncementBanner = memo(function AnnouncementBanner({ announcements }: AnnouncementBannerProps) {
  const [dismissed, setDismissed] = useState<Set<string>>(new Set());
  const visible = announcements.filter((a) => !dismissed.has(a.id));

  if (visible.length === 0) return null;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      {visible.map((ann) => (
        <div
          key={ann.id}
          style={{
            display: 'flex',
            gap: 12,
            padding: '12px 16px',
            borderRadius: 'var(--radius-lg)',
            border: `1px solid ${!ann.read ? priorityColors[ann.priority] : 'var(--color-border)'}`,
            background: !ann.read ? `${priorityColors[ann.priority]}08` : 'var(--color-surface)',
            borderLeft: `4px solid ${priorityColors[ann.priority]}`,
          }}
        >
          <div
            style={{
              width: 32,
              height: 32,
              borderRadius: 'var(--radius-md)',
              background: `${priorityColors[ann.priority]}1A`,
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: priorityColors[ann.priority],
              flexShrink: 0,
            }}
          >
            <Icon name={categoryIcons[ann.category] || 'bell'} size={16} />
          </div>
          <div style={{ flex: 1, minWidth: 0 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 4 }}>
              <span style={{ fontWeight: 600, fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>{ann.title}</span>
              {!ann.read && (
                <span style={{ width: 8, height: 8, borderRadius: '50%', background: priorityColors[ann.priority], flexShrink: 0 }} />
              )}
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginLeft: 'auto' }}>{ann.timestamp}</span>
            </div>
            <p style={{ margin: '0 0 4px', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', lineHeight: 1.5 }}>{ann.message}</p>
            {ann.author && (
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>&mdash; {ann.author}</span>
            )}
          </div>
          <button
            onClick={() => setDismissed((prev) => new Set(prev).add(ann.id))}
            aria-label={`Dismiss ${ann.title}`}
            style={{
              background: 'none',
              border: 'none',
              cursor: 'pointer',
              color: 'var(--color-text-tertiary)',
              padding: 4,
              flexShrink: 0,
              alignSelf: 'flex-start',
              display: 'flex',
            }}
          >
            <Icon name="x" size={16} />
          </button>
        </div>
      ))}
    </div>
  );
});
