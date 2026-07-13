import React from 'react';
import { AppShell } from './AppShell';

export interface PublicLayoutProps {
  children: React.ReactNode;
  header?: React.ReactNode;
  footer?: React.ReactNode;
}

export const PublicLayout: React.FC<PublicLayoutProps> = ({
  children,
  header,
  footer,
}) => {
  return (
    <AppShell header={header}>
      {children}
      {footer}
    </AppShell>
  );
};

PublicLayout.displayName = 'PublicLayout';
export default PublicLayout;
