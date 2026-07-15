import { memo } from 'react';
import type { AuditEntry } from './types';

interface AuditTimelineProps {
  entries: AuditEntry[];
  maxItems?: number;
}

export const AuditTimeline = memo(function AuditTimeline({ entries, maxItems = 10 }: AuditTimelineProps) {
  const display = entries.slice(0, maxItems);

  if (display.length === 0) {
    return (
      <div style={{ padding: 24, textAlign: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body)' }}>
        No audit entries yet.
      </div>
    );
  }

  return (
    <div role="list" aria-label="Audit timeline">
      {display.map((entry, i) => (
        <div
          key={entry.id}
          role="listitem"
          style={{
            display: 'flex',
            gap: 12,
            padding: '12px 16px',
            position: 'relative',
            borderBottom: i < display.length - 1 ? '1px solid var(--color-border)' : 'none',
          }}
        >
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', width: 20, flexShrink: 0 }}>
            <div style={{ width: 8, height: 8, borderRadius: '50%', background: 'var(--color-primary)', marginTop: 4 }} />
            {i < display.length - 1 && (
              <div style={{ width: 1, flex: 1, background: 'var(--color-border)', marginTop: 4 }} />
            )}
          </div>
          <div style={{ flex: 1, minWidth: 0 }}>
            <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)', fontWeight: 500 }}>{entry.action}</div>
            {entry.details && (
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', marginTop: 2 }}>{entry.details}</div>
            )}
            <div style={{ display: 'flex', gap: 12, marginTop: 4, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
              <span>{entry.performedBy}</span>
              <span>{entry.timestamp}</span>
              {entry.resource && <span>{entry.resource}</span>}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
});
