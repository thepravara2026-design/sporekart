

import { memo } from 'react';

interface VersionEntry {
  version: number;
  label: string;
  timestamp: string;
  author: string;
  current?: boolean;
}

interface VersionHistoryProps {
  versions: VersionEntry[];
  onRestore?: (version: number) => void;
}

export const VersionHistory = memo(function VersionHistory({ versions, onRestore }: VersionHistoryProps) {
  if (versions.length === 0) {
    return (
      <div style={{ padding: 24, textAlign: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body)' }}>
        No version history available.
      </div>
    );
  }

  return (
    <div role="list" aria-label="Version history">
      {versions.map((v) => (
        <div
          key={v.version}
          role="listitem"
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: 12,
            padding: '10px 16px',
            borderBottom: '1px solid var(--color-border)',
            background: v.current ? 'var(--color-primary-alpha)' : 'transparent',
          }}
        >
          <div
            style={{
              width: 32, height: 32, borderRadius: 'var(--radius-md)',
              background: v.current ? 'var(--color-primary)' : 'var(--color-surface-hover)',
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              color: v.current ? '#fff' : 'var(--color-text-secondary)',
              fontSize: 'var(--text-caption)', fontWeight: 700, flexShrink: 0,
            }}
          >
            v{v.version}
          </div>
          <div style={{ flex: 1, minWidth: 0 }}>
            <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)', fontWeight: v.current ? 600 : 400 }}>
              {v.label}
              {v.current && <span style={{ marginLeft: 8, fontSize: 'var(--text-caption)', color: 'var(--color-primary)' }}>(current)</span>}
            </div>
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginTop: 2 }}>
              {v.author} · {v.timestamp}
            </div>
          </div>
          {!v.current && onRestore && (
            <button
              onClick={() => onRestore(v.version)}
              style={{
                padding: '4px 10px', border: '1px solid var(--color-border)',
                borderRadius: 'var(--radius-sm)', background: 'transparent',
                color: 'var(--color-text-secondary)', cursor: 'pointer',
                fontSize: 'var(--text-caption)',
              }}
            >
              Restore
            </button>
          )}
        </div>
      ))}
    </div>
  );
});
