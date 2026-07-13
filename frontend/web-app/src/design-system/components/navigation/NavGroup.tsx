import React from 'react';

export interface NavGroupProps {
  label: string;
  children: React.ReactNode;
  icon?: React.ReactNode;
  className?: string;
}

const groupStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-stack-xs)',
};

const labelStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: 'var(--space-inline-xs)',
  fontSize: 'var(--text-caption)',
  fontWeight: 'var(--weight-semibold)',
  color: 'var(--color-text-secondary)',
  textTransform: 'uppercase',
  letterSpacing: 'var(--tracking-wide)',
  padding: 'var(--space-stack-xs) 0',
};

const childrenStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-stack-xs)',
};

export const NavGroup: React.FC<NavGroupProps> = ({
  label,
  children,
  icon,
  className = '',
}) => {
  return (
    <div className={className} role="group" aria-label={label} style={groupStyle}>
      <div style={labelStyle}>
        {icon && <span style={{ flexShrink: 0, fontSize: 14, display: 'inline-flex' }}>{icon}</span>}
        <span>{label}</span>
      </div>
      <div style={childrenStyle}>{children}</div>
    </div>
  );
};

NavGroup.displayName = 'NavGroup';

export default NavGroup;
