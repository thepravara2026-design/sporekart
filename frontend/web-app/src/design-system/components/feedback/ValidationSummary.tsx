import React from 'react';

export interface ValidationSummaryProps {
  errors?: Record<string, string | undefined>;
  warnings?: Record<string, string | undefined>;
  title?: string;
  className?: string;
}

function ErrorIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden="true">
      <circle cx="8" cy="8" r="7" stroke="currentColor" strokeWidth="1.5" />
      <path d="M8 4.5v4M8 11.5h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  );
}

function WarningIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden="true">
      <path d="M8 1L1 14h14L8 1z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
      <path d="M8 6v3M8 11.5h0" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
    </svg>
  );
}

export const ValidationSummary: React.FC<ValidationSummaryProps> = ({
  errors = {},
  warnings = {},
  title,
  className = '',
}) => {
  const errorEntries = Object.entries(errors).filter(([_, msg]) => msg);
  const warningEntries = Object.entries(warnings).filter(([_, msg]) => msg);
  const total = errorEntries.length + warningEntries.length;

  if (total === 0) return null;

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-sm)',
    padding: 'var(--space-inline-md) var(--space-inline-md)',
    borderRadius: 'var(--radius-md)',
    border: '1px solid',
    backgroundColor: 'var(--color-bg-surface-default)',
  };

  const errorContainerStyle: React.CSSProperties = {
    ...containerStyle,
    borderColor: 'var(--color-border-error)',
  };

  const warningContainerStyle: React.CSSProperties = {
    ...containerStyle,
    borderColor: 'var(--color-border-default)',
  };

  const titleStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    margin: 0,
  };

  const listStyle: React.CSSProperties = {
    listStyle: 'none',
    margin: 0,
    padding: 0,
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-xs)',
  };

  const itemStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'flex-start',
    gap: 'var(--space-inline-sm)',
    fontSize: 'var(--text-body-sm)',
    lineHeight: 'var(--leading-normal)',
  };

  const labelStyle: React.CSSProperties = {
    fontWeight: 'var(--weight-medium)',
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
    textTransform: 'uppercase' as const,
    letterSpacing: 'var(--tracking-wide)',
  };

  if (total > 0) {
    return (
      <div
        className={className}
        style={errorEntries.length > 0 ? errorContainerStyle : warningContainerStyle}
        role="alert"
        aria-live="polite"
        aria-atomic="true"
      >
        {title && <p style={titleStyle}>{title}</p>}
        {errorEntries.length > 0 && (
          <>
            <span style={labelStyle}>Errors ({errorEntries.length})</span>
            <ul style={listStyle}>
              {errorEntries.map(([field, msg]) => (
                <li key={field} style={{ ...itemStyle, color: 'var(--color-text-danger)' }}>
                  <span style={{ flexShrink: 0, color: 'var(--color-text-danger)' }}><ErrorIcon /></span>
                  <span>{msg}</span>
                </li>
              ))}
            </ul>
          </>
        )}
        {warningEntries.length > 0 && (
          <>
            <span style={{ ...labelStyle, marginTop: warningEntries.length > 0 && errorEntries.length > 0 ? 'var(--space-stack-xs)' : 0 }}>Warnings ({warningEntries.length})</span>
            <ul style={listStyle}>
              {warningEntries.map(([field, msg]) => (
                <li key={field} style={{ ...itemStyle, color: 'var(--color-text-warning)' }}>
                  <span style={{ flexShrink: 0, color: 'var(--color-text-warning)' }}><WarningIcon /></span>
                  <span>{msg}</span>
                </li>
              ))}
            </ul>
          </>
        )}
      </div>
    );
  }

  return null;
};

ValidationSummary.displayName = 'ValidationSummary';

export default ValidationSummary;
