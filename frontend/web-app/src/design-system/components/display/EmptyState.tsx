import React from 'react';
import { Button } from '../core/Button';

export type EmptyStateType = 'noData' | 'noResults' | 'comingSoon' | 'accessDenied' | 'offline' | 'maintenance' | 'searchEmpty' | 'filterEmpty';

export interface EmptyStateProps {
  type?: EmptyStateType;
  title?: string;
  description?: string;
  icon?: React.ReactNode;
  action?: {
    label: string;
    onClick: () => void;
  };
  secondaryAction?: {
    label: string;
    onClick: () => void;
  };
  compact?: boolean;
  className?: string;
}

const defaultIcons: Record<EmptyStateType, React.ReactNode> = {
  noData: (
    <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <polyline points="22 12 16 12 14 15 10 15 8 12 2 12" />
      <path d="M5.45 5.11L2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z" />
    </svg>
  ),
  noResults: (
    <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="11" cy="11" r="8" />
      <line x1="21" y1="21" x2="16.65" y2="16.65" />
    </svg>
  ),
  comingSoon: (
    <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="10" />
      <polyline points="12 6 12 12 16 14" />
    </svg>
  ),
  accessDenied: (
    <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
      <path d="M7 11V7a5 5 0 0 1 10 0v4" />
    </svg>
  ),
  offline: (
    <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <line x1="1" y1="1" x2="23" y2="23" />
      <path d="M16.72 11.06A10.94 10.94 0 0 1 19 12.55" />
      <path d="M5 12.55A10.94 10.94 0 0 1 8.32 8.69" />
      <path d="M1.52 8.86A15.9 15.9 0 0 1 10.08 4.89" />
      <path d="M22.48 8.86A15.9 15.9 0 0 0 14 4.89" />
      <line x1="12" y1="20" x2="12.01" y2="20" />
    </svg>
  ),
  maintenance: (
    <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z" />
    </svg>
  ),
  searchEmpty: (
    <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="11" cy="11" r="8" />
      <line x1="21" y1="21" x2="16.65" y2="16.65" />
    </svg>
  ),
  filterEmpty: (
    <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46" />
    </svg>
  ),
};

const defaultConfig: Record<EmptyStateType, { title: string; description: string }> = {
  noData: { title: 'No data', description: "There's nothing here yet." },
  noResults: { title: 'No results', description: 'Try adjusting your search.' },
  comingSoon: { title: 'Coming soon', description: 'This feature is on its way.' },
  accessDenied: { title: 'Access denied', description: "You don't have permission." },
  offline: { title: "You're offline", description: 'Check your connection.' },
  maintenance: { title: 'Under maintenance', description: "We'll be back shortly." },
  searchEmpty: { title: 'No matches', description: 'Try different keywords.' },
  filterEmpty: { title: 'No matches', description: 'Try clearing your filters.' },
};

export const EmptyState: React.FC<EmptyStateProps> = ({
  type = 'noData',
  title,
  description,
  icon,
  action,
  secondaryAction,
  compact = false,
  className = '',
}) => {
  const config = defaultConfig[type];
  const resolvedTitle = title ?? config.title;
  const resolvedDesc = description ?? config.description;
  const resolvedIcon = icon ?? defaultIcons[type];

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    textAlign: 'center',
    padding: compact ? 'var(--space-section-gap)' : 'calc(var(--space-section-gap) * 2)',
    gap: compact ? 'var(--space-component-gap)' : 'var(--space-section-gap)',
  };

  const iconWrapperStyle: React.CSSProperties = {
    width: 'var(--illustration-md)',
    height: 'var(--illustration-md)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    color: 'var(--color-text-secondary)',
  };

  const titleStyle: React.CSSProperties = {
    fontSize: compact ? 'var(--text-h4)' : 'var(--text-h3)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    margin: 0,
  };

  const descStyle: React.CSSProperties = {
    fontSize: compact ? 'var(--text-small)' : 'var(--text-body)',
    color: 'var(--color-text-secondary)',
    margin: 0,
    marginTop: 'var(--space-inline-xs)',
    maxWidth: 400,
  };

  const actionsStyle: React.CSSProperties = {
    display: 'flex',
    gap: 'var(--space-component-gap)',
    marginTop: 'var(--space-component-gap)',
  };

  return (
    <div
      className={`sk-empty-state ${className}`.trim()}
      style={containerStyle}
      role="status"
    >
      <div style={iconWrapperStyle}>
        {resolvedIcon}
      </div>
      <div>
        <h3 style={titleStyle}>{resolvedTitle}</h3>
        {resolvedDesc && <p style={descStyle}>{resolvedDesc}</p>}
      </div>
      {(action || secondaryAction) && (
        <div style={actionsStyle}>
          {action && (
            <Button variant="primary" onClick={action.onClick}>
              {action.label}
            </Button>
          )}
          {secondaryAction && (
            <Button variant="ghost" onClick={secondaryAction.onClick}>
              {secondaryAction.label}
            </Button>
          )}
        </div>
      )}
    </div>
  );
};

EmptyState.displayName = 'EmptyState';
export default EmptyState;
