import { memo } from 'react';
import type { InboxItem } from '../types';
import { TypeBadge } from './TypeBadge';
import { PriorityBadge } from './PriorityBadge';

interface InboxItemCardProps {
  item: InboxItem;
  onToggleRead: () => void;
  onToggleStar: () => void;
}

export const InboxItemCard = memo(function InboxItemCard({ item, onToggleRead, onToggleStar }: InboxItemCardProps) {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: `1px solid ${item.isRead ? 'var(--color-border-default)' : '#2563eb'}`,
      background: item.isRead ? 'var(--color-bg-surface-default)' : '#eff6ff',
      display: 'flex', gap: 12, alignItems: 'flex-start',
    }}>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 4, alignItems: 'center', flexShrink: 0 }}>
        <button onClick={onToggleRead} aria-label={item.isRead ? 'Mark as unread' : 'Mark as read'} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 16, padding: 2, color: item.isRead ? '#d1d5db' : '#2563eb' }}>
          {item.isRead ? '○' : '●'}
        </button>
        <button onClick={onToggleStar} aria-label={item.isStarred ? 'Unstar' : 'Star'} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 16, padding: 2, color: item.isStarred ? '#f59e0b' : '#d1d5db' }}>
          {item.isStarred ? '★' : '☆'}
        </button>
      </div>
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 4 }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
          <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
            <TypeBadge type={item.type} />
            <PriorityBadge priority={item.priority} />
          </div>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{new Date(item.receivedDate).toLocaleDateString()}</span>
        </div>
        <h4 style={{ fontSize: 'var(--text-body)', fontWeight: item.isRead ? 'var(--weight-normal)' : 'var(--weight-semibold)', margin: 0 }}>{item.title}</h4>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: 0 }}>{item.subtitle}</p>
        <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          <span>{item.sender}</span>
          <span>{item.courseName}</span>
          {item.isImportant && <span style={{ color: '#dc2626' }}>❗Important</span>}
          {item.isPinned && <span>📌 Pinned</span>}
        </div>
      </div>
    </div>
  );
});
