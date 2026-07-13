import React from 'react';

export interface AppShellProps {
  children: React.ReactNode;
  header?: React.ReactNode;
  sidebar?: React.ReactNode;
  className?: string;
}

export const AppShell: React.FC<AppShellProps> = ({
  children,
  header,
  sidebar,
  className = '',
}) => {
  const rootStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateRows: 'auto 1fr',
    minHeight: '100vh',
  };

  const innerStyle: React.CSSProperties = {
    display: 'flex',
  };

  const sidebarStyle: React.CSSProperties = {
    width: 'var(--layout-sidebar-width)',
    flexShrink: 0,
  };

  const mainStyle: React.CSSProperties = {
    flex: 1,
    overflowY: 'auto',
    display: 'flex',
    flexDirection: 'column',
  };

  return (
    <div className={`sk-app-shell ${className}`.trim()} style={rootStyle}>
      {header && (
        <header className="sk-app-shell__header" style={{ height: 'var(--layout-header-height)' }}>
          {header}
        </header>
      )}
      <div className="sk-app-shell__inner" style={innerStyle}>
        {sidebar && (
          <aside className="sk-app-shell__sidebar" style={sidebarStyle}>
            {sidebar}
          </aside>
        )}
        <main className="sk-app-shell__main" style={mainStyle}>
          {children}
        </main>
      </div>
    </div>
  );
};

AppShell.displayName = 'AppShell';
export default AppShell;
