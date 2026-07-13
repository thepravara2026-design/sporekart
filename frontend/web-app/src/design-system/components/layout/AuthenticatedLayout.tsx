import React from 'react';
import { AppShell } from './AppShell';
import { ContentContainer } from './ContentContainer';

export interface AuthenticatedLayoutProps {
  children: React.ReactNode;
  header?: React.ReactNode;
  sidebar?: React.ReactNode;
}

export const AuthenticatedLayout: React.FC<AuthenticatedLayoutProps> = ({
  children,
  header,
  sidebar,
}) => {
  return (
    <AppShell header={header} sidebar={sidebar}>
      <ContentContainer>{children}</ContentContainer>
    </AppShell>
  );
};

AuthenticatedLayout.displayName = 'AuthenticatedLayout';
export default AuthenticatedLayout;
