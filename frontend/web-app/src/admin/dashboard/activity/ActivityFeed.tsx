import { useState, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import type { ActivityItemData } from '../types';

interface ActivityFeedProps {
  items: ActivityItemData[];
  maxItems?: number;
}

const typeIcons: Record<string, string> = {
  create: 'plus',
  update: 'edit',
  delete: 'trash',
  system: 'settings',
  warning: 'alert-triangle',
  success: 'check',
};

const typeColors: Record<string, string> = {
  create: 'var(--color-success)',
  update: 'var(--color-primary)',
  delete: 'var(--color-error)',
  system: 'var(--color-info)',
  warning: 'var(--color-warning)',
  success: 'var(--color-success)',
};

export const ActivityFeed = memo(function ActivityFeed({ items, maxItems = 10 }: ActivityFeedProps) {
  const [expandedId, setExpandedId] = useState<string | null>(null);
  const displayItems = items.slice(0, maxItems);

  if (displayItems.length === 0) {
    return (
      <div style={{ textAlign: 'center', padding: '32px 16px', color: 'var(--color-text-tertiary)' }}>
        <Icon name="clock" size={32} />
        <p style={{ margin: '8px 0 0', fontSize: 'var(--text-body)' }}>No recent activity</p>
      </div>
    );
  }

  return (
    <div style={{ width: '100%' }}>
      {displayItems.map((item) => {
        const isExpanded = expandedId === item.id;
        const statusColor = item.status === 'completed' ? 'var(--color-success)' : item.status === 'failed' ? 'var(--color-error)' : 'var(--color-warning)';
        return (
          <div key={item.id}>
            <div
              onClick={() => setExpandedId(isExpanded ? null : item.id)}
              onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); setExpandedId(isExpanded ? null : item.id); } }}
              role="button"
              tabIndex={0}
              aria-expanded={isExpanded}
              style={{
                display: 'flex',
                gap: 12,
                padding: '10px 0',
                borderBottom: '1px solid var(--color-border)',
                cursor: item.details ? 'pointer' : undefined,
                alignItems: 'flex-start',
              }}
            >
              <div
                style={{
                  width: 32,
                  height: 32,
                  borderRadius: 'var(--radius-full)',
                  background: `${typeColors[item.type]}1A`,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  color: typeColors[item.type],
                  flexShrink: 0,
                }}
              >
                <Icon name={typeIcons[item.type] || 'circle'} size={14} />
              </div>
              <div style={{ flex: 1, minWidth: 0 }}>
                <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>
                  <strong>{item.user}</strong> {item.action} <span style={{ color: 'var(--color-primary)' }}>{item.target}</span>
                </div>
                <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginTop: 2 }}>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{item.timestamp}</span>
                  {item.status && (
                    <span style={{ display: 'flex', alignItems: 'center', gap: 3, fontSize: 'var(--text-caption)', color: statusColor }}>
                      <span style={{ width: 6, height: 6, borderRadius: '50%', background: statusColor }} />
                      {item.status}
                    </span>
                  )}
                </div>
              </div>
              {item.details && (
                <Icon name={isExpanded ? 'chevron-up' : 'chevron-down'} size={14} style={{ color: 'var(--color-text-tertiary)', marginTop: 4, flexShrink: 0 }} />
              )}
            </div>
            {isExpanded && item.details && (
              <div style={{ padding: '8px 0 8px 44px', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)', marginBottom: 4 }}>
                {item.details}
              </div>
            )}
          </div>
        );
      })}
    </div>
  );
});
