import React from 'react';

export interface AuditEntry {
  id: string;
  user: string;
  action: string;
  resource: string;
  details?: string;
  timestamp: string | Date;
  ip?: string;
  severity?: 'info' | 'warning' | 'error';
}

export interface AuditTimelineProps {
  entries: AuditEntry[];
  className?: string;
  style?: React.CSSProperties;
}

const severityColors: Record<string, string> = {
  info: 'var(--color-info-500)',
  warning: 'var(--color-warning-500)',
  error: 'var(--color-danger-500)',
};

const severityBgColors: Record<string, string> = {
  info: 'var(--color-info-50)',
  warning: 'var(--color-warning-50)',
  error: 'var(--color-danger-50)',
};

function formatTimestamp(d: string | Date): string {
  const dt = d instanceof Date ? d : new Date(d);
  return dt.toLocaleString();
}

export const AuditTimeline: React.FC<AuditTimelineProps> = ({
  entries,
  className = '',
  style,
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 0,
    position: 'relative',
    paddingLeft: 'var(--space-6)',
    ...style,
  };

  const lineStyle: React.CSSProperties = {
    position: 'absolute',
    left: 11,
    top: 0,
    bottom: 0,
    width: 2,
    backgroundColor: 'var(--color-border-default)',
  };

  const entryStyle: React.CSSProperties = {
    position: 'relative',
    paddingBottom: 'var(--space-4)',
  };

  const dotStyle: React.CSSProperties = {
    position: 'absolute',
    left: -22,
    top: 6,
    width: 12,
    height: 12,
    borderRadius: 'var(--radius-full)',
    zIndex: 1,
    border: '2px solid var(--color-bg-surface-default)',
  };

  const cardStyle: React.CSSProperties = {
    padding: 'var(--space-3) var(--space-4)',
    borderRadius: 'var(--radius-sm)',
    border: '1px solid var(--color-border-default)',
    backgroundColor: 'var(--color-bg-surface-default)',
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-2)',
    flexWrap: 'wrap',
    fontSize: 'var(--text-body-sm)',
  };

  const userStyle: React.CSSProperties = {
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
  };

  const actionStyle: React.CSSProperties = {
    color: 'var(--color-text-secondary)',
  };

  const resourceStyle: React.CSSProperties = {
    fontWeight: 'var(--weight-medium)',
    color: 'var(--color-text-primary)',
    fontFamily: 'var(--font-family-mono)',
    fontSize: 'var(--text-caption)',
    padding: '1px var(--space-1)',
    borderRadius: 'var(--radius-xs)',
    backgroundColor: 'var(--color-bg-background)',
  };

  const metaStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-3)',
    marginTop: 'var(--space-1)',
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-disabled)',
  };

  const detailsStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    color: 'var(--color-text-secondary)',
    marginTop: 'var(--space-1)',
    padding: 'var(--space-2)',
    borderRadius: 'var(--radius-xs)',
    backgroundColor: 'var(--color-bg-background)',
    fontFamily: 'var(--font-family-mono)',
    wordBreak: 'break-all',
  };

  return (
    <div
      className={`sk-audit-timeline ${className}`.trim()}
      style={containerStyle}
      role="list"
      aria-label="Audit trail timeline"
    >
      <div style={lineStyle} aria-hidden="true" />
      {entries.map((entry) => {
        const sevColor = severityColors[entry.severity || 'info'] || 'var(--color-neutral-300)';
        const sevBg = severityBgColors[entry.severity || 'info'] || 'var(--color-neutral-100)';

        return (
          <div
            key={entry.id}
            style={entryStyle}
            role="listitem"
            aria-label={`${entry.user} ${entry.action} ${entry.resource}`}
          >
            <span
              style={{
                ...dotStyle,
                backgroundColor: sevColor,
                boxShadow: entry.severity === 'error'
                  ? `0 0 0 3px color-mix(in srgb, ${sevColor} 25%, transparent)`
                  : undefined,
              }}
              aria-hidden="true"
            />
            <div style={{ ...cardStyle, borderLeftColor: sevColor, borderLeftWidth: 3 }}>
              <div style={headerStyle}>
                <span style={userStyle}>{entry.user}</span>
                <span style={actionStyle}>{entry.action}</span>
                <span style={resourceStyle}>{entry.resource}</span>
                <span
                  style={{
                    marginLeft: 'auto',
                    fontSize: 'var(--text-caption)',
                    padding: '1px var(--space-2)',
                    borderRadius: 'var(--radius-badge)',
                    backgroundColor: sevBg,
                    color: sevColor,
                    fontWeight: 'var(--weight-medium)',
                  }}
                >
                  {entry.severity || 'info'}
                </span>
              </div>
              <div style={metaStyle}>
                <span>{formatTimestamp(entry.timestamp)}</span>
                {entry.ip && <span>IP: {entry.ip}</span>}
              </div>
              {entry.details && <div style={detailsStyle}>{entry.details}</div>}
            </div>
          </div>
        );
      })}
    </div>
  );
};

AuditTimeline.displayName = 'AuditTimeline';
