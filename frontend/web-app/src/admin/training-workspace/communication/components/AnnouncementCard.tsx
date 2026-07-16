import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import type { Announcement } from '../data/communicationTypes';
import { CATEGORY_LABELS } from '../data/communicationTypes';
import { CATEGORY_ICON } from '../data/communicationOptions';
import { excerpt, formatAudience, formatCount, formatRelative } from '../data/communicationFormatters';
import PriorityBadge from './PriorityBadge';
import StatusBadge from './StatusBadge';
import ChannelChip from './ChannelChip';

export interface AnnouncementCardProps {
  announcement: Announcement;
  onOpen?: (announcement: Announcement) => void;
}

const AnnouncementCard = memo(function AnnouncementCard({ announcement, onOpen }: AnnouncementCardProps) {
  const {
    title, summary, body, category, priority, status, author,
    createdAt, pinned, featured, channels, audience, viewsPlaceholder, attachments,
  } = announcement;

  return (
    <article
      aria-label={title}
      style={{
        display: 'flex',
        flexDirection: 'column',
        gap: 'var(--space-3)',
        padding: 'var(--space-4)',
        background: 'var(--color-bg-surface-default)',
        border: '1px solid var(--color-border-default)',
        borderLeft: `3px solid ${pinned ? 'var(--color-primary)' : 'var(--color-border-default)'}`,
        borderRadius: 'var(--radius-lg)',
        boxShadow: 'var(--shadow-1)',
      }}
    >
      <header style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 'var(--space-3)' }}>
        <div style={{ display: 'flex', gap: 'var(--space-3)', minWidth: 0 }}>
          <span
            aria-hidden
            style={{
              display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
              width: 36, height: 36, flexShrink: 0, borderRadius: 'var(--radius-md)',
              background: 'var(--color-bg-primary-weak)', color: 'var(--color-primary)',
            }}
          >
            <Icon name={CATEGORY_ICON[category]} size={18} />
          </span>
          <div style={{ minWidth: 0 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)', flexWrap: 'wrap' }}>
              {pinned && <Icon name="star" size={14} color="var(--color-primary)" aria-label="Pinned" />}
              {featured && <Icon name="zap" size={14} color="var(--color-warning-600)" aria-label="Featured" />}
              <h3 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
                {title}
              </h3>
            </div>
            <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
              {CATEGORY_LABELS[category]} · {author} · {formatRelative(createdAt)}
            </p>
          </div>
        </div>
        <div style={{ display: 'flex', gap: 'var(--space-1)', flexShrink: 0, flexWrap: 'wrap', justifyContent: 'flex-end' }}>
          <StatusBadge status={status} />
          <PriorityBadge priority={priority} />
        </div>
      </header>

      <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 1.5 }}>
        {excerpt(summary || body, 160)}
      </p>

      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2)', alignItems: 'center' }}>
        <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1)', fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
          <Icon name="users" size={14} /> {formatAudience(audience)}
        </span>
        {channels.map((ch) => <ChannelChip key={ch} channel={ch} />)}
        {attachments.length > 0 && (
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1)', fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
            <Icon name="paperclip" size={14} /> {attachments.length}
          </span>
        )}
      </div>

      <footer style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-2)', paddingTop: 'var(--space-2)', borderTop: '1px solid var(--color-border-default)' }}>
        <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1)', fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
          <Icon name="eye" size={14} /> {formatCount(viewsPlaceholder)} views
        </span>
        {onOpen && (
          <button
            type="button"
            onClick={() => onOpen(announcement)}
            style={{
              display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1)',
              padding: '4px 10px', borderRadius: 'var(--radius-sm)',
              border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)',
              color: 'var(--color-primary)', fontSize: 'var(--text-body-sm)', fontWeight: 500, cursor: 'pointer',
            }}
          >
            View details <Icon name="chevron-right" size={14} />
          </button>
        )}
      </footer>
    </article>
  );
});

export default AnnouncementCard;
