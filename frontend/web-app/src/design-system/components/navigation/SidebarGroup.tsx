import React from 'react';

export interface SidebarGroupProps {
  label: string;
  children: React.ReactNode;
  icon?: React.ReactNode;
  className?: string;
}

const labelBase: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  fontWeight: 'var(--weight-semibold)',
  color: 'var(--color-text-secondary)',
  textTransform: 'uppercase',
  letterSpacing: 'var(--tracking-wide)',
  padding: 'var(--space-stack-sm) var(--space-page-x) var(--space-stack-xs)',
  display: 'flex',
  alignItems: 'center',
  gap: 'var(--space-inline-xs)',
};

export const SidebarGroup: React.FC<SidebarGroupProps> = ({
  label,
  children,
  icon,
  className = '',
}) => {
  return (
    <div className={className} role="group" aria-label={label}>
      <div style={labelBase}>
        {icon && <span style={{ flexShrink: 0, fontSize: 14, display: 'inline-flex' }}>{icon}</span>}
        <span>{label}</span>
      </div>
      {children}
    </div>
  );
};

SidebarGroup.displayName = 'SidebarGroup';

export default SidebarGroup;
