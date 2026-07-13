import React from 'react';

export interface PersistentDrawerProps {
  open: boolean;
  title?: string;
  children: React.ReactNode;
  width?: string;
  position?: 'left' | 'right';
  className?: string;
  style?: React.CSSProperties;
}

export const PersistentDrawer: React.FC<PersistentDrawerProps> = ({
  open,
  title,
  children,
  width = 'var(--layout-sidebar-width)',
  position = 'left',
  className = '',
  style,
}) => {
  const isLeft = position === 'left';

  const containerStyle: React.CSSProperties = {
    position: 'relative',
    width: open ? width : 0,
    minWidth: open ? width : 0,
    flexShrink: 0,
    transition: 'width var(--duration-normal) var(--easing-standard)',
    overflow: 'hidden',
    ...style,
  };

  const panelStyle: React.CSSProperties = {
    position: 'absolute',
    top: 0,
    [isLeft ? 'left' : 'right']: 0,
    bottom: 0,
    width,
    background: 'var(--color-bg-surface-default)',
    display: 'flex',
    flexDirection: 'column',
    borderRight: isLeft ? '1px solid var(--color-border-default)' : 'none',
    borderLeft: isLeft ? 'none' : '1px solid var(--color-border-default)',
    overflowY: 'auto',
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    padding: 'var(--space-stack-md) var(--space-page-x)',
    borderBottom: '1px solid var(--color-border-default)',
    flexShrink: 0,
    minHeight: 'var(--layout-header-height)',
  };

  const titleStyle: React.CSSProperties = {
    fontSize: 'var(--text-h4)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    margin: 0,
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    overflowY: 'auto',
    padding: 'var(--space-stack-md) var(--space-page-x)',
  };

  return (
    <div
      className={`sk-persistent-drawer ${className}`.trim()}
      style={containerStyle}
    >
      {open && (
        <div style={panelStyle} role="complementary" aria-label={title || 'Persistent panel'}>
          {title && (
            <div style={headerStyle}>
              <h2 style={titleStyle}>{title}</h2>
            </div>
          )}
          <div style={contentStyle}>
            {children}
          </div>
        </div>
      )}
    </div>
  );
};

PersistentDrawer.displayName = 'PersistentDrawer';
export default PersistentDrawer;
