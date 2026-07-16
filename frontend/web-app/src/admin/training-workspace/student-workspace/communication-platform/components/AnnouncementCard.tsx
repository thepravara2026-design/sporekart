import { memo } from 'react';
import type { Announcement } from '../types';
import { StatusBadge } from './StatusBadge';

export const AnnouncementCard = memo(function AnnouncementCard({ announcement }: { announcement: Announcement }) {
  return (
    <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 8 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
        <span style={{ fontSize: 'var(--text-caption)', padding: '2px 8px', borderRadius: 'var(--radius-full)', background: '#8b5cf618', color: '#8b5cf6', fontWeight: 'var(--weight-medium)', textTransform: 'capitalize' }}>{announcement.category.replace(/-/g, ' ')}</span>
        <StatusBadge status={announcement.status} />
      </div>
      <h4 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{announcement.title}</h4>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: 1.5 }}>{announcement.subtitle}</p>
      <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', gap: 4, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>By {announcement.createdBy} • {announcement.views} views</span>
        <span>{new Date(announcement.createdDate).toLocaleDateString()}</span>
      </div>
      {announcement.attachments > 0 && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>📎 {announcement.attachments} attachment(s)</span>}
    </div>
  );
});
