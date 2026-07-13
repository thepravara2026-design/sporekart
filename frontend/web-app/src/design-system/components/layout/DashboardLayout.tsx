import React from 'react';
import { AppShell } from './AppShell';
import { ContentContainer } from './ContentContainer';

export interface DashboardLayoutProps {
  children: React.ReactNode;
  header?: React.ReactNode;
  sidebar?: React.ReactNode;
  columns?: 2 | 3 | 4;
}

export const DashboardLayout: React.FC<DashboardLayoutProps> = ({
  children,
  header,
  sidebar,
  columns = 3,
}) => {
  const gridStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: `repeat(${columns}, 1fr)`,
    gap: 'var(--space-component-gap)',
  };

  return (
    <AppShell header={header} sidebar={sidebar}>
      <ContentContainer>
        <div className="sk-dashboard-layout__grid" style={gridStyle}>
          {children}
        </div>
      </ContentContainer>
    </AppShell>
  );
};

DashboardLayout.displayName = 'DashboardLayout';
export default DashboardLayout;
