import React from 'react';

export interface NotificationCategoryProps {
  label: string;
  count?: number;
  active?: boolean;
  onClick: () => void;
  icon?: React.ReactNode;
  className?: string;
  style?: React.CSSProperties;
}

export const NotificationCategory: React.FC<NotificationCategoryProps> = ({
  label,
  count,
  active = false,
  onClick,
  icon,
  className = '',
  style,
}) => {
  const chipStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    gap: 'var(--space-inline-xs)',
    padding: 'var(--space-inline-xs) var(--space-inline-md)',
    borderRadius: 'var(--radius-tag)',
    border: active
      ? '2px solid var(--color-border-focus)'
      : '1px solid var(--color-border-default)',
    background: active ? 'var(--color-bg-primary-weak)' : 'transparent',
    color: active ? 'var(--color-text-primary)' : 'var(--color-text-secondary)',
    fontWeight: active ? 'var(--weight-semibold)' : 'var(--weight-normal)',
    fontSize: 'var(--text-label)',
    cursor: 'pointer',
    whiteSpace: 'nowrap',
    transition: 'all var(--duration-fast) var(--easing-standard)',
    ...style,
  };

  const iconStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    width: 16,
    height: 16,
  };

  const countStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: active ? 'var(--color-text-primary)' : 'var(--color-text-disabled)',
    fontWeight: 'var(--weight-medium)',
    background: active ? 'var(--color-neutral-200)' : 'var(--color-neutral-100)',
    borderRadius: 'var(--radius-full)',
    padding: '0 var(--space-inline-xs)',
    minWidth: 18,
    height: 18,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    lineHeight: 1,
  };

  return (
    <button
      className={`sk-notification-category ${active ? 'sk-notification-category--active' : ''} ${className}`.trim()}
      style={chipStyle}
      onClick={onClick}
      type="button"
      role="tab"
      aria-selected={active}
    >
      {icon && <span style={iconStyle}>{icon}</span>}
      <span>{label}</span>
      {count !== undefined && (
        <span style={countStyle}>{count}</span>
      )}
    </button>
  );
};

NotificationCategory.displayName = 'NotificationCategory';
export default NotificationCategory;
