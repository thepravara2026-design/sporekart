import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { useNotificationFeedState } from '../state/useNotificationFeedState';
import { NOTIFICATION_TYPES } from '../data/communicationTypes';
import { PRIORITY_OPTIONS } from '../data/communicationOptions';
import type { SelectOption } from '../data/communicationOptions';
import type { NotificationType } from '../data/communicationTypes';
import { formatRelative, formatAudience } from '../data/communicationFormatters';
import { CommEmptyState, CommToolbar, PriorityBadge } from '../components';

const TYPE_OPTIONS: SelectOption<NotificationType>[] = NOTIFICATION_TYPES.map((t) => ({ value: t.type, label: t.label }));
const TYPE_ICON: Record<string, string> = Object.fromEntries(NOTIFICATION_TYPES.map((t) => [t.type, t.icon]));

const READ_TABS: Array<{ key: 'all' | 'unread' | 'read'; label: string }> = [
  { key: 'all', label: 'All' },
  { key: 'unread', label: 'Unread' },
  { key: 'read', label: 'Read' },
];

const CommunicationNotificationsPage = memo(function CommunicationNotificationsPage() {
  const state = useNotificationFeedState();

  return (
    <section id="panel-notifications" aria-labelledby="tab-notifications" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
      <div style={{ display: 'flex', flexWrap: 'wrap', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-3)' }}>
        <div role="tablist" aria-label="Read filter" style={{ display: 'flex', gap: 'var(--space-1)' }}>
          {READ_TABS.map((t) => {
            const active = state.filters.read === t.key;
            return (
              <button
                key={t.key}
                type="button"
                role="tab"
                aria-selected={active}
                onClick={() => state.setFilter('read', t.key)}
                style={{
                  padding: '6px 12px', borderRadius: 'var(--radius-md)', border: 'none',
                  background: active ? 'var(--color-bg-primary-weak)' : 'transparent',
                  color: active ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                  fontSize: 'var(--text-body-sm)', fontWeight: active ? 600 : 500, cursor: 'pointer',
                }}
              >
                {t.label}{t.key === 'unread' && state.unreadCount > 0 ? ` (${state.unreadCount})` : ''}
              </button>
            );
          })}
        </div>
        <button
          type="button"
          onClick={state.markAllRead}
          disabled={state.unreadCount === 0}
          style={{
            display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1)',
            padding: '6px 12px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)',
            color: state.unreadCount === 0 ? 'var(--color-text-disabled)' : 'var(--color-primary)',
            fontSize: 'var(--text-body-sm)', fontWeight: 500,
            cursor: state.unreadCount === 0 ? 'not-allowed' : 'pointer',
          }}
        >
          <Icon name="check" size={14} /> Mark all read
        </button>
      </div>

      <CommToolbar
        search={state.filters.search}
        searchPlaceholder="Search notifications…"
        onSearchChange={(v) => state.setFilter('search', v)}
        selects={[
          { id: 'type', label: 'Type', value: state.filters.type, options: TYPE_OPTIONS, onChange: (v) => state.setFilter('type', v as never) },
          { id: 'priority', label: 'Priority', value: state.filters.priority, options: PRIORITY_OPTIONS, onChange: (v) => state.setFilter('priority', v as never) },
        ]}
      />

      {state.filtered.length === 0 ? (
        <CommEmptyState icon="bell" title="No notifications" description="You're all caught up." />
      ) : (
        <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
          {state.filtered.map((n) => {
            const read = state.readState[n.id];
            return (
              <li
                key={n.id}
                style={{
                  display: 'flex', gap: 'var(--space-3)', alignItems: 'flex-start',
                  padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
                  background: read ? 'var(--color-bg-surface-default)' : 'var(--color-bg-primary-weak)',
                  border: '1px solid var(--color-border-default)',
                }}
              >
                <span
                  aria-hidden
                  style={{
                    display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
                    width: 34, height: 34, flexShrink: 0, borderRadius: 'var(--radius-md)',
                    background: 'var(--color-bg-surface-muted)', color: 'var(--color-primary)',
                  }}
                >
                  <Icon name={TYPE_ICON[n.type] ?? 'bell'} size={16} />
                </span>
                <div style={{ minWidth: 0, flex: 1 }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)', flexWrap: 'wrap' }}>
                    <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: read ? 500 : 700, color: 'var(--color-text-primary)' }}>{n.title}</span>
                    <PriorityBadge priority={n.priority} />
                    {!read && <span aria-label="Unread" style={{ width: 8, height: 8, borderRadius: '50%', background: 'var(--color-primary)' }} />}
                  </div>
                  <p style={{ margin: '2px 0 0', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{n.message}</p>
                  <p style={{ margin: '4px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
                    {formatAudience(n.audience)} · {formatRelative(n.createdAt)}
                  </p>
                </div>
                <button
                  type="button"
                  onClick={() => (read ? state.markUnread(n.id) : state.markRead(n.id))}
                  title={read ? 'Mark unread' : 'Mark read'}
                  style={{
                    flexShrink: 0, padding: '4px 8px', borderRadius: 'var(--radius-sm)',
                    border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)',
                    color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)', cursor: 'pointer',
                  }}
                >
                  {read ? 'Unread' : 'Read'}
                </button>
              </li>
            );
          })}
        </ul>
      )}
    </section>
  );
});

export default CommunicationNotificationsPage;
