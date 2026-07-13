import React from 'react';

export interface HeaderProps {
  variant?: 'primary' | 'secondary' | 'compact' | 'transparent';
  sticky?: boolean;
  logo?: React.ReactNode;
  brand?: string;
  children?: React.ReactNode;
  actions?: React.ReactNode;
  className?: string;
}

const variantStyles: Record<string, React.CSSProperties> = {
  primary: {
    background: 'var(--color-bg-surface-default)',
    borderBottom: '1px solid var(--color-border-default)',
  },
  secondary: {
    background: 'var(--color-bg-background)',
    borderBottom: '1px solid var(--color-border-default)',
  },
  compact: {
    background: 'var(--color-bg-surface-default)',
    borderBottom: '1px solid var(--color-border-default)',
  },
  transparent: {
    background: 'transparent',
    borderBottom: 'none',
  },
};

export const Header: React.FC<HeaderProps> = ({
  variant = 'primary',
  sticky = false,
  logo,
  brand,
  children,
  actions,
  className = '',
}) => {
  const headerStyle: React.CSSProperties = {
    ...variantStyles[variant],
    display: 'flex',
    alignItems: 'center',
    height: 'var(--layout-header-height)',
    padding: '0 var(--space-page-x)',
    gap: 'var(--space-inline-md)',
    ...(sticky ? { position: 'sticky', top: 0, zIndex: 'var(--z-header)' as unknown as number } : {}),
  };

  const brandStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
    fontWeight: 'var(--weight-semibold)',
    font: 'var(--text-h4)',
    color: 'var(--color-text-primary)',
    whiteSpace: 'nowrap',
  };

  const navStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-md)',
    flex: 1,
    justifyContent: 'center',
  };

  const actionsStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
    flexShrink: 0,
  };

  return (
    <header className={`sk-header sk-header--${variant} ${className}`.trim()} style={headerStyle}>
      <div className="sk-header__brand" style={brandStyle}>
        {logo && <span className="sk-header__logo">{logo}</span>}
        {brand && <span className="sk-header__brand-text">{brand}</span>}
      </div>
      {children && (
        <nav className="sk-header__nav" style={navStyle}>
          {children}
        </nav>
      )}
      {actions && (
        <div className="sk-header__actions" style={actionsStyle}>
          {actions}
        </div>
      )}
    </header>
  );
};

Header.displayName = 'Header';
export default Header;
