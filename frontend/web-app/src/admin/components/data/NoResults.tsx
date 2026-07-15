import React from 'react';
import { Icon } from '../../../design-system/icons/Icon';

export interface NoResultsProps {
  title?: string;
  description?: string;
  action?: React.ReactNode;
  icon?: React.ReactNode;
  className?: string;
  style?: React.CSSProperties;
}

const wrapperStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
  padding: 'var(--space-stack-lg) var(--space-page-x)',
  textAlign: 'center',
  gap: 'var(--space-stack-sm)',
};

const iconContainer: React.CSSProperties = {
  width: 48,
  height: 48,
  borderRadius: 'var(--radius-full)',
  background: 'var(--color-bg-surface-raised)',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  color: 'var(--color-text-secondary)',
  marginBottom: 'var(--space-stack-xs)',
};

export const NoResults: React.FC<NoResultsProps> = ({
  title = 'No results found',
  description = 'Try adjusting your search or filters.',
  action,
  icon,
  className = '',
  style,
}) => {
  return (
    <div className={className} style={{ ...wrapperStyle, ...style }}>
      <div style={iconContainer}>
        {icon || <Icon name="search" size={20} color="currentColor" />}
      </div>
      <div style={{ fontSize: 'var(--text-h4)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)' }}>{title}</div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', maxWidth: 320 }}>{description}</div>
      {action && <div style={{ marginTop: 'var(--space-stack-sm)' }}>{action}</div>}
    </div>
  );
};

NoResults.displayName = 'NoResults';
export default NoResults;
