import React from 'react';

export interface Crumb {
  label: string;
  href?: string;
  icon?: React.ReactNode;
}

export interface BreadcrumbItemProps {
  crumb: Crumb;
  isLast?: boolean;
}

const itemStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 'var(--space-inline-xs)',
};

const linkStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 'var(--space-inline-xs)',
  color: 'var(--color-text-secondary)',
  textDecoration: 'none',
  fontSize: 'var(--text-caption)',
  fontFamily: 'var(--font-family-sans)',
  transition: 'color var(--duration-fast) var(--easing-standard)',
};

const textStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 'var(--space-inline-xs)',
  color: 'var(--color-text-primary)',
  fontSize: 'var(--text-caption)',
  fontWeight: 'var(--weight-medium)',
  fontFamily: 'var(--font-family-sans)',
};

export const BreadcrumbItem: React.FC<BreadcrumbItemProps> = ({
  crumb,
  isLast = false,
}) => {
  if (isLast) {
    return (
      <li style={itemStyle} aria-current="page">
        <span style={textStyle}>
          {crumb.icon && <span style={{ flexShrink: 0, fontSize: 14 }}>{crumb.icon}</span>}
          <span>{crumb.label}</span>
        </span>
      </li>
    );
  }

  return (
    <li style={itemStyle}>
      <a
        href={crumb.href || '#'}
        style={linkStyle}
        tabIndex={0}
      >
        {crumb.icon && <span style={{ flexShrink: 0, fontSize: 14 }}>{crumb.icon}</span>}
        <span>{crumb.label}</span>
      </a>
    </li>
  );
};

BreadcrumbItem.displayName = 'BreadcrumbItem';

export default BreadcrumbItem;
